package com.nutricional.core.service;

import com.nutricional.core.model.Paciente;
import com.nutricional.core.model.StatusAnamnese;
import com.nutricional.core.repository.PacienteRepository;
import com.nutricional.core.repository.NutricionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private NutricionistaRepository nutricionistaRepository;
    
    // Criar novo paciente
    public Paciente criar(Paciente paciente, Long nutricionistaId) {
        // Verificar se telefone já existe
        if (pacienteRepository.existsByTelefone(paciente.getTelefone())) {
            throw new RuntimeException("Telefone já cadastrado: " + paciente.getTelefone());
        }
        
        // Associar ao nutricionista
        return nutricionistaRepository.findById(nutricionistaId)
            .map(nutricionista -> {
                paciente.setNutricionista(nutricionista);
                return pacienteRepository.save(paciente);
            })
            .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado: " + nutricionistaId));
    }
    
    // Buscar por ID
    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }
    
    // Buscar por telefone
    public Optional<Paciente> buscarPorTelefone(String telefone) {
        return pacienteRepository.findByTelefone(telefone);
    }
    
    // Listar pacientes de um nutricionista
    public List<Paciente> listarPorNutricionista(Long nutricionistaId) {
        return pacienteRepository.findByNutricionistaId(nutricionistaId);
    }
    
    // Listar por status
    public List<Paciente> listarPorStatus(StatusAnamnese status) {
        return pacienteRepository.findByStatusAnamnese(status);
    }
    
    // Atualizar dados básicos
    public Paciente atualizar(Long id, Paciente dadosAtualizados) {
        return pacienteRepository.findById(id)
            .map(paciente -> {
                paciente.setNome(dadosAtualizados.getNome());
                paciente.setEmail(dadosAtualizados.getEmail());
                paciente.setIdade(dadosAtualizados.getIdade());
                paciente.setPeso(dadosAtualizados.getPeso());
                paciente.setAltura(dadosAtualizados.getAltura());
                return pacienteRepository.save(paciente);
            })
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado: " + id));
    }
    
    // Atualizar status da anamnese
    public Paciente atualizarStatusAnamnese(Long id, StatusAnamnese novoStatus) {
        return pacienteRepository.findById(id)
            .map(paciente -> {
                paciente.setStatusAnamnese(novoStatus);
                return pacienteRepository.save(paciente);
            })
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado: " + id));
    }
    
    // Dar consentimento LGPD
    public Paciente darConsentimentoLgpd(Long id) {
        return pacienteRepository.findById(id)
            .map(paciente -> {
                paciente.setConsentimentoLgpd(true);
                return pacienteRepository.save(paciente);
            })
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado: " + id));
    }
    
    // Deletar paciente
    public void deletar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado: " + id);
        }
        pacienteRepository.deleteById(id);
    }
    
    // Contar pacientes por nutricionista
    public Long contarPorNutricionista(Long nutricionistaId) {
        return pacienteRepository.countByNutricionistaId(nutricionistaId);
    }
}

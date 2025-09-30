package com.nutricional.core.service;

import com.nutricional.core.model.Nutricionista;
import com.nutricional.core.repository.NutricionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NutricionistaService {
    
    @Autowired
    private NutricionistaRepository nutricionistaRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Criar novo nutricionista
    public Nutricionista criar(Nutricionista nutricionista) {
        // Verificar se email já existe
        if (nutricionistaRepository.existsByEmail(nutricionista.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + nutricionista.getEmail());
        }
        
        // 🔒 CRIPTOGRAFAR SENHA COM BCRYPT
        String senhaCriptografada = passwordEncoder.encode(nutricionista.getSenha());
        nutricionista.setSenha(senhaCriptografada);
        
        return nutricionistaRepository.save(nutricionista);
    }
    
    // Validar login (verificar senha)
    public boolean validarLogin(String email, String senhaPlana) {
        return nutricionistaRepository.findByEmail(email)
            .map(nutricionista -> passwordEncoder.matches(senhaPlana, nutricionista.getSenha()))
            .orElse(false);
    }
    
    // Buscar por ID
    public Optional<Nutricionista> buscarPorId(Long id) {
        return nutricionistaRepository.findById(id);
    }
    
    // Buscar por email
    public Optional<Nutricionista> buscarPorEmail(String email) {
        return nutricionistaRepository.findByEmail(email);
    }
    
    // Listar todos ativos
    public List<Nutricionista> listarAtivos() {
        return nutricionistaRepository.findAllAtivos();
    }
    
    // Listar todos
    public List<Nutricionista> listarTodos() {
        return nutricionistaRepository.findAll();
    }
    
    // Atualizar dados (sem senha)
    public Nutricionista atualizar(Long id, Nutricionista dadosAtualizados) {
        return nutricionistaRepository.findById(id)
            .map(nutricionista -> {
                nutricionista.setNome(dadosAtualizados.getNome());
                nutricionista.setCrn(dadosAtualizados.getCrn());
                // Não atualizar email e senha aqui por segurança
                return nutricionistaRepository.save(nutricionista);
            })
            .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado: " + id));
    }
    
    // Alterar senha (método separado e seguro)
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Nutricionista nutricionista = nutricionistaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado: " + id));
        
        // Verificar senha atual
        if (!passwordEncoder.matches(senhaAtual, nutricionista.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }
        
        // Criptografar nova senha
        String novaSenhaCriptografada = passwordEncoder.encode(novaSenha);
        nutricionista.setSenha(novaSenhaCriptografada);
        
        nutricionistaRepository.save(nutricionista);
    }
    
    // Desativar (soft delete)
    public void desativar(Long id) {
        nutricionistaRepository.findById(id)
            .map(nutricionista -> {
                nutricionista.setAtivo(false);
                return nutricionistaRepository.save(nutricionista);
            })
            .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado: " + id));
    }
    
    // Contar total
    public long contarTotal() {
        return nutricionistaRepository.count();
    }
}

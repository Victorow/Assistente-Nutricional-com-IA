package com.nutricional.core.repository;

import com.nutricional.core.model.Paciente;
import com.nutricional.core.model.StatusAnamnese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    // Buscar por telefone
    Optional<Paciente> findByTelefone(String telefone);
    
    // Buscar pacientes de um nutricionista
    List<Paciente> findByNutricionistaId(Long nutricionistaId);
    
    // Buscar pacientes por status da anamnese
    List<Paciente> findByStatusAnamnese(StatusAnamnese status);
    
    // Buscar pacientes de um nutricionista com status específico
    @Query("SELECT p FROM Paciente p WHERE p.nutricionista.id = :nutricionistaId AND p.statusAnamnese = :status")
    List<Paciente> findByNutricionistaAndStatus(@Param("nutricionistaId") Long nutricionistaId, 
                                               @Param("status") StatusAnamnese status);
    
    // Verificar se telefone já existe
    boolean existsByTelefone(String telefone);
    
    // Contar pacientes por nutricionista
    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.nutricionista.id = :nutricionistaId")
    Long countByNutricionistaId(@Param("nutricionistaId") Long nutricionistaId);
}

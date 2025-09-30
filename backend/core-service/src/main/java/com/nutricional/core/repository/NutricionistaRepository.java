package com.nutricional.core.repository;

import com.nutricional.core.model.Nutricionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NutricionistaRepository extends JpaRepository<Nutricionista, Long> {
    
    // Buscar por email (para login)
    Optional<Nutricionista> findByEmail(String email);
    
    // Buscar por CRN
    Optional<Nutricionista> findByCrn(String crn);
    
    // Verificar se email já existe
    boolean existsByEmail(String email);
    
    // Buscar nutricionistas ativos
    @Query("SELECT n FROM Nutricionista n WHERE n.ativo = true")
    java.util.List<Nutricionista> findAllAtivos();
}

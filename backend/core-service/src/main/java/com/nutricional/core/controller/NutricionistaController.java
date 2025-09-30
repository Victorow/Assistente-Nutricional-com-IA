package com.nutricional.core.controller;

import com.nutricional.core.model.Nutricionista;
import com.nutricional.core.service.NutricionistaService;
import com.nutricional.core.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nutricionistas")
@CrossOrigin(origins = "*")
public class NutricionistaController {
    
    @Autowired
    private NutricionistaService nutricionistaService;
    
    @Autowired
    private PacienteService pacienteService;
    
    // GET /api/nutricionistas - Listar todos
    @GetMapping
    public ResponseEntity<List<Nutricionista>> listarTodos() {
        List<Nutricionista> nutricionistas = nutricionistaService.listarTodos();
        return ResponseEntity.ok(nutricionistas);
    }
    
    // POST /api/nutricionistas - Criar novo
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(@RequestBody Nutricionista nutricionista) {
        try {
            Nutricionista criado = nutricionistaService.criar(nutricionista);
            
            // Criar response manualmente para evitar problemas de tipos
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCESSO");
            response.put("message", "Nutricionista criado com sucesso");
            
            Map<String, Object> nutricData = new HashMap<>();
            nutricData.put("id", criado.getId());
            nutricData.put("nome", criado.getNome());
            nutricData.put("email", criado.getEmail());
            nutricData.put("crn", criado.getCrn());
            nutricData.put("ativo", criado.getAtivo());
            
            response.put("nutricionista", nutricData);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // GET /api/nutricionistas/{id} - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        return nutricionistaService.buscarPorId(id)
            .map(nutricionista -> {
                Map<String, Object> response = new HashMap<>();
                response.put("id", nutricionista.getId());
                response.put("nome", nutricionista.getNome());
                response.put("email", nutricionista.getEmail());
                response.put("crn", nutricionista.getCrn());
                response.put("ativo", nutricionista.getAtivo());
                response.put("criadoEm", nutricionista.getCriadoEm().toString());
                response.put("totalPacientes", pacienteService.contarPorNutricionista(id));
                
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/nutricionistas/login - Fazer login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");
        
        if (email == null || senha == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", "Email e senha são obrigatórios");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        boolean loginValido = nutricionistaService.validarLogin(email, senha);
        
        if (loginValido) {
            return nutricionistaService.buscarPorEmail(email)
                .map(nutricionista -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "SUCESSO");
                    response.put("message", "Login realizado com sucesso");
                    
                    Map<String, Object> nutricData = new HashMap<>();
                    nutricData.put("id", nutricionista.getId());
                    nutricData.put("nome", nutricionista.getNome());
                    nutricData.put("email", nutricionista.getEmail());
                    nutricData.put("crn", nutricionista.getCrn());
                    
                    response.put("nutricionista", nutricData);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("status", "ERRO");
                    errorResponse.put("message", "Erro interno");
                    return ResponseEntity.badRequest().body(errorResponse);
                });
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", "Email ou senha inválidos");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // PUT /api/nutricionistas/{id} - Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable Long id, @RequestBody Nutricionista nutricionista) {
        try {
            Nutricionista atualizado = nutricionistaService.atualizar(id, nutricionista);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCESSO");
            response.put("message", "Nutricionista atualizado com sucesso");
            
            Map<String, Object> nutricData = new HashMap<>();
            nutricData.put("id", atualizado.getId());
            nutricData.put("nome", atualizado.getNome());
            nutricData.put("crn", atualizado.getCrn());
            
            response.put("nutricionista", nutricData);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // GET /api/nutricionistas/stats - Estatísticas
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        long total = nutricionistaService.contarTotal();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_nutricionistas", total);
        stats.put("nutricionistas_ativos", nutricionistaService.listarAtivos().size());
        stats.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(stats);
    }
}

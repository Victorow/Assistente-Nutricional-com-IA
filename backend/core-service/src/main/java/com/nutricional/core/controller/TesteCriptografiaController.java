package com.nutricional.core.controller;

import com.nutricional.core.model.Nutricionista;
import com.nutricional.core.service.NutricionistaService;
import com.nutricional.core.util.CriptografiaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/teste-crypto")
@CrossOrigin(origins = "*")
public class TesteCriptografiaController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CriptografiaUtil criptografiaUtil;
    
    @Autowired
    private NutricionistaService nutricionistaService;
    
    // Testar criptografia de senha (BCrypt)
    @GetMapping("/senha")
    public Map<String, Object> testarSenha(@RequestParam(defaultValue = "123456") String senha) {
        Map<String, Object> resultado = new LinkedHashMap<>();
        
        // 1. Senha original
        resultado.put("senha_original", senha);
        
        // 2. Criptografar com BCrypt
        String senhaCriptografada = passwordEncoder.encode(senha);
        resultado.put("senha_bcrypt", senhaCriptografada);
        
        // 3. Verificar se a senha confere
        boolean senhaConfere = passwordEncoder.matches(senha, senhaCriptografada);
        resultado.put("senha_confere", senhaConfere);
        
        // 4. Testar senha errada
        boolean senhaErradaConfere = passwordEncoder.matches("senhaerrada", senhaCriptografada);
        resultado.put("senha_errada_confere", senhaErradaConfere);
        
        // 5. Informações adicionais
        resultado.put("algoritmo", "BCrypt");
        resultado.put("forca", "12 (muito seguro)");
        resultado.put("timestamp", LocalDateTime.now());
        
        return resultado;
    }
    
    // Testar criptografia de dados sensíveis (AES)
    @GetMapping("/dados")
    public Map<String, Object> testarDados(@RequestParam(defaultValue = "11999999999") String telefone) {
        Map<String, Object> resultado = new LinkedHashMap<>();
        
        // 1. Dados originais
        resultado.put("telefone_original", telefone);
        resultado.put("email_original", "teste@exemplo.com");
        
        // 2. Criptografar
        String telefoneCriptografado = criptografiaUtil.criptografar(telefone);
        String emailCriptografado = criptografiaUtil.criptografar("teste@exemplo.com");
        
        resultado.put("telefone_criptografado", telefoneCriptografado);
        resultado.put("email_criptografado", emailCriptografado);
        
        // 3. Descriptografar
        String telefoneDescriptografado = criptografiaUtil.descriptografar(telefoneCriptografado);
        String emailDescriptografado = criptografiaUtil.descriptografar(emailCriptografado);
        
        resultado.put("telefone_descriptografado", telefoneDescriptografado);
        resultado.put("email_descriptografado", emailDescriptografado);
        
        // 4. Verificar se é igual
        resultado.put("telefone_igual", telefone.equals(telefoneDescriptografado));
        resultado.put("email_igual", "teste@exemplo.com".equals(emailDescriptografado));
        
        // 5. Informações adicionais
        resultado.put("algoritmo", "AES-128");
        resultado.put("timestamp", LocalDateTime.now());
        
        return resultado;
    }
    
    // Testar criação de nutricionista com criptografia
    @PostMapping("/nutricionista")
    public Map<String, Object> testarNutricionista(@RequestBody Map<String, String> dados) {
        Map<String, Object> resultado = new LinkedHashMap<>();
        
        try {
            // 1. Dados originais
            String nome = dados.getOrDefault("nome", "Dr. Teste");
            String email = dados.getOrDefault("email", "teste" + System.currentTimeMillis() + "@exemplo.com");
            String senha = dados.getOrDefault("senha", "123456");
            String crn = dados.getOrDefault("crn", "CRN-123456");
            
            resultado.put("dados_enviados", Map.of(
                "nome", nome,
                "email", email, 
                "senha", senha,
                "crn", crn
            ));
            
            // 2. Criar nutricionista (senha será criptografada automaticamente)
            Nutricionista nutricionista = new Nutricionista(nome, email, senha, crn);
            Nutricionista salvo = nutricionistaService.criar(nutricionista);
            
            resultado.put("nutricionista_criado", Map.of(
                "id", salvo.getId(),
                "nome", salvo.getNome(),
                "email", salvo.getEmail(),
                "senha_bcrypt", salvo.getSenha(), // Vai mostrar a senha criptografada
                "crn", salvo.getCrn()
            ));
            
            // 3. Testar login
            boolean loginValido = nutricionistaService.validarLogin(email, senha);
            boolean loginInvalido = nutricionistaService.validarLogin(email, "senhaerrada");
            
            resultado.put("teste_login", Map.of(
                "login_correto", loginValido,
                "login_incorreto", loginInvalido
            ));
            
            resultado.put("status", "SUCESSO");
            
        } catch (Exception e) {
            resultado.put("status", "ERRO");
            resultado.put("erro", e.getMessage());
        }
        
        return resultado;
    }
    
    // Comparar múltiplas criptografias da mesma senha
    @GetMapping("/comparar-bcrypt")
    public Map<String, Object> compararBcrypt(@RequestParam(defaultValue = "123456") String senha) {
        Map<String, Object> resultado = new LinkedHashMap<>();
        
        resultado.put("senha_original", senha);
        
        // BCrypt sempre gera hashes diferentes mesmo para a mesma senha (por causa do salt)
        String hash1 = passwordEncoder.encode(senha);
        String hash2 = passwordEncoder.encode(senha);
        String hash3 = passwordEncoder.encode(senha);
        
        resultado.put("hash_1", hash1);
        resultado.put("hash_2", hash2); 
        resultado.put("hash_3", hash3);
        
        // Todos devem ser diferentes, mas todos devem validar a mesma senha
        resultado.put("hashes_sao_diferentes", !hash1.equals(hash2) && !hash2.equals(hash3));
        resultado.put("hash1_valida_senha", passwordEncoder.matches(senha, hash1));
        resultado.put("hash2_valida_senha", passwordEncoder.matches(senha, hash2));
        resultado.put("hash3_valida_senha", passwordEncoder.matches(senha, hash3));
        
        resultado.put("explicacao", "BCrypt sempre gera hashes diferentes (por causa do salt), mas todos validam a mesma senha");
        
        return resultado;
    }
}

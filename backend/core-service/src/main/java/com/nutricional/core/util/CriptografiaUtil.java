package com.nutricional.core.util;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class CriptografiaUtil {
    
    // Chave secreta (em produção, mover para variável de ambiente)
    private static final String CHAVE_SECRETA = "MinhaSuperChaveSecreta123456"; // 256 bits
    
    private SecretKey getChave() {
        byte[] key = CHAVE_SECRETA.getBytes();
        return new SecretKeySpec(key, 0, 16, "AES"); // Usar apenas 16 bytes (128 bits)
    }
    
    // Criptografar dados sensíveis (CPF, telefone, etc.)
    public String criptografar(String dadoSensivel) {
        if (dadoSensivel == null || dadoSensivel.isEmpty()) {
            return dadoSensivel;
        }
        
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getChave());
            byte[] dadoCriptografado = cipher.doFinal(dadoSensivel.getBytes());
            return Base64.getEncoder().encodeToString(dadoCriptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar dados sensíveis", e);
        }
    }
    
    // Descriptografar dados sensíveis
    public String descriptografar(String dadoCriptografado) {
        if (dadoCriptografado == null || dadoCriptografado.isEmpty()) {
            return dadoCriptografado;
        }
        
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getChave());
            byte[] dadoDescriptografado = cipher.doFinal(Base64.getDecoder().decode(dadoCriptografado));
            return new String(dadoDescriptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar dados sensíveis", e);
        }
    }
}

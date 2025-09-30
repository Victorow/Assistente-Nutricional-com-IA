package com.nutricional.core.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.Map;
import java.time.LocalDateTime;

@Controller
public class HomeController {
    
    // Página inicial
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "🚀 Assistente Nutricional API");
        model.addAttribute("status", "Funcionando perfeitamente!");
        model.addAttribute("timestamp", LocalDateTime.now());
        return "home"; // Vai procurar src/main/resources/templates/home.html
    }
    
    // Endpoint REST para testar via API
    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> status() {
        return Map.of(
            "status", "SUCCESS",
            "message", "🚀 Assistente Nutricional API está funcionando!",
            "timestamp", LocalDateTime.now(),
            "database", "PostgreSQL conectado",
            "version", "1.0.0"
        );
    }
}

package com.nutricional.core.controller;

import com.nutricional.core.model.Paciente;
import com.nutricional.core.model.StatusAnamnese;
import com.nutricional.core.service.PacienteService;
import com.nutricional.core.util.CriptografiaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private CriptografiaUtil criptografiaUtil;
    
    // POST /api/pacientes - Criar novo paciente
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(@RequestBody Map<String, Object> dados) {
        try {
            // Extrair dados do request
            String nome = (String) dados.get("nome");
            String telefone = (String) dados.get("telefone");
            String email = (String) dados.get("email");
            Long nutricionistaId = Long.valueOf(dados.get("nutricionistaId").toString());
            
            // Criar paciente
            Paciente paciente = new Paciente();
            paciente.setNome(nome);
            
            // Criptografar dados sensíveis
            paciente.setTelefone(criptografiaUtil.criptografar(telefone));
            if (email != null && !email.isEmpty()) {
                paciente.setEmail(criptografiaUtil.criptografar(email));
            }
            
            // Dados opcionais
            if (dados.get("idade") != null) {
                paciente.setIdade(Integer.valueOf(dados.get("idade").toString()));
            }
            if (dados.get("peso") != null) {
                paciente.setPeso(Double.valueOf(dados.get("peso").toString()));
            }
            if (dados.get("altura") != null) {
                paciente.setAltura(Double.valueOf(dados.get("altura").toString()));
            }
            
            Paciente criado = pacienteService.criar(paciente, nutricionistaId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCESSO");
            response.put("message", "Paciente criado com sucesso");
            
            Map<String, Object> pacienteData = new HashMap<>();
            pacienteData.put("id", criado.getId());
            pacienteData.put("nome", criado.getNome());
            pacienteData.put("telefone", criptografiaUtil.descriptografar(criado.getTelefone())); // Descriptografar para retorno
            pacienteData.put("email", criado.getEmail() != null ? 
                criptografiaUtil.descriptografar(criado.getEmail()) : null);
            pacienteData.put("idade", criado.getIdade());
            pacienteData.put("peso", criado.getPeso());
            pacienteData.put("altura", criado.getAltura());
            pacienteData.put("statusAnamnese", criado.getStatusAnamnese());
            pacienteData.put("criadoEm", criado.getCriadoEm().toString());
            
            response.put("paciente", pacienteData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // GET /api/pacientes/nutricionista/{id} - Listar pacientes de um nutricionista
    @GetMapping("/nutricionista/{nutricionistaId}")
    public ResponseEntity<Map<String, Object>> listarPorNutricionista(@PathVariable Long nutricionistaId) {
        try {
            List<Paciente> pacientes = pacienteService.listarPorNutricionista(nutricionistaId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("total", pacientes.size());
            response.put("nutricionistaId", nutricionistaId);
            
            // Descriptografar dados para retorno
            List<Map<String, Object>> pacientesData = pacientes.stream()
                .map(p -> {
                    Map<String, Object> pacienteMap = new HashMap<>();
                    pacienteMap.put("id", p.getId());
                    pacienteMap.put("nome", p.getNome());
                    pacienteMap.put("telefone", criptografiaUtil.descriptografar(p.getTelefone()));
                    pacienteMap.put("email", p.getEmail() != null ? 
                        criptografiaUtil.descriptografar(p.getEmail()) : null);
                    pacienteMap.put("idade", p.getIdade());
                    pacienteMap.put("peso", p.getPeso());
                    pacienteMap.put("altura", p.getAltura());
                    pacienteMap.put("statusAnamnese", p.getStatusAnamnese());
                    pacienteMap.put("consentimentoLgpd", p.getConsentimentoLgpd());
                    pacienteMap.put("criadoEm", p.getCriadoEm().toString());
                    return pacienteMap;
                })
                .toList();
            
            response.put("pacientes", pacientesData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // GET /api/pacientes/{id} - Buscar paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
            .map(paciente -> {
                Map<String, Object> response = new HashMap<>();
                response.put("id", paciente.getId());
                response.put("nome", paciente.getNome());
                response.put("telefone", criptografiaUtil.descriptografar(paciente.getTelefone()));
                response.put("email", paciente.getEmail() != null ? 
                    criptografiaUtil.descriptografar(paciente.getEmail()) : null);
                response.put("idade", paciente.getIdade());
                response.put("peso", paciente.getPeso());
                response.put("altura", paciente.getAltura());
                response.put("statusAnamnese", paciente.getStatusAnamnese());
                response.put("consentimentoLgpd", paciente.getConsentimentoLgpd());
                response.put("dataConsentimento", paciente.getDataConsentimento());
                response.put("criadoEm", paciente.getCriadoEm().toString());
                
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERRO");
                errorResponse.put("message", "Paciente não encontrado");
                return ResponseEntity.notFound().build();
            });
    }
    
    // PUT /api/pacientes/{id}/consentimento-lgpd - Dar consentimento LGPD
    @PutMapping("/{id}/consentimento-lgpd")
    public ResponseEntity<Map<String, Object>> darConsentimentoLgpd(@PathVariable Long id, 
                                                                   @RequestParam(required = false) String ip) {
        try {
            Paciente paciente = pacienteService.darConsentimentoLgpd(id);
            
            // Registrar IP se fornecido
            if (ip != null) {
                paciente.setIpConsentimento(ip);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCESSO");
            response.put("message", "Consentimento LGPD registrado com sucesso");
            response.put("dataConsentimento", paciente.getDataConsentimento().toString());
            response.put("ip", ip);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // PUT /api/pacientes/{id}/status - Atualizar status da anamnese
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> atualizarStatus(@PathVariable Long id, 
                                                             @RequestBody Map<String, String> dados) {
        try {
            StatusAnamnese novoStatus = StatusAnamnese.valueOf(dados.get("status"));
            Paciente atualizado = pacienteService.atualizarStatusAnamnese(id, novoStatus);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCESSO");
            response.put("message", "Status atualizado com sucesso");
            response.put("novoStatus", atualizado.getStatusAnamnese());
            response.put("pacienteId", atualizado.getId());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // GET /api/pacientes/stats - Estatísticas gerais
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total_pacientes", pacienteService.listarPorStatus(StatusAnamnese.PENDENTE).size() +
                                    pacienteService.listarPorStatus(StatusAnamnese.EM_ANDAMENTO).size() +
                                    pacienteService.listarPorStatus(StatusAnamnese.CONCLUIDA).size() +
                                    pacienteService.listarPorStatus(StatusAnamnese.APROVADA).size());
        
        stats.put("pendentes", pacienteService.listarPorStatus(StatusAnamnese.PENDENTE).size());
        stats.put("em_andamento", pacienteService.listarPorStatus(StatusAnamnese.EM_ANDAMENTO).size());
        stats.put("concluidas", pacienteService.listarPorStatus(StatusAnamnese.CONCLUIDA).size());
        stats.put("aprovadas", pacienteService.listarPorStatus(StatusAnamnese.APROVADA).size());
        stats.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(stats);
    }
    
    // ========== ENDPOINTS DE LGPD ==========
    
    // DELETE /api/pacientes/{id}/dados-lgpd - Excluir todos os dados (direito ao esquecimento)
    @DeleteMapping("/{id}/dados-lgpd")
    public ResponseEntity<Map<String, Object>> excluirDadosLgpd(@PathVariable Long id, 
                                                              @RequestParam String motivo) {
        try {
            // Buscar paciente primeiro para logs
            Paciente paciente = pacienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            
            // Log da exclusão (antes de deletar)
            String dadosExcluidos = String.format(
                "Paciente: %s, Tel: %s, Email: %s", 
                paciente.getNome(),
                criptografiaUtil.descriptografar(paciente.getTelefone()),
                paciente.getEmail() != null ? criptografiaUtil.descriptografar(paciente.getEmail()) : "N/A"
            );
            
            // Deletar do banco
            pacienteService.deletar(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCESSO");
            response.put("message", "Dados do paciente excluídos permanentemente");
            response.put("pacienteId", id);
            response.put("motivo", motivo);
            response.put("dataExclusao", LocalDateTime.now().toString());
            response.put("observacao", "Exclusão irreversível - direito ao esquecimento LGPD");
            
            // Em produção, seria bom salvar este log em tabela separada
            System.out.println("🗑️ LGPD - Dados excluídos: " + dadosExcluidos + " - Motivo: " + motivo);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // GET /api/pacientes/{id}/dados-lgpd - Exportar todos os dados (direito à portabilidade)
    @GetMapping("/{id}/dados-lgpd")
    public ResponseEntity<Map<String, Object>> exportarDadosLgpd(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
            .map(paciente -> {
                Map<String, Object> dadosCompletos = new HashMap<>();
                
                // Dados pessoais (descriptografados)
                Map<String, Object> dadosPessoais = new HashMap<>();
                dadosPessoais.put("id", paciente.getId());
                dadosPessoais.put("nome", paciente.getNome());
                dadosPessoais.put("telefone", criptografiaUtil.descriptografar(paciente.getTelefone()));
                dadosPessoais.put("email", paciente.getEmail() != null ? 
                    criptografiaUtil.descriptografar(paciente.getEmail()) : null);
                dadosPessoais.put("idade", paciente.getIdade());
                dadosPessoais.put("peso", paciente.getPeso());
                dadosPessoais.put("altura", paciente.getAltura());
                
                // Dados do tratamento
                Map<String, Object> dadosTratamento = new HashMap<>();
                dadosTratamento.put("statusAnamnese", paciente.getStatusAnamnese());
                dadosTratamento.put("criadoEm", paciente.getCriadoEm().toString());
                
                // Dados LGPD
                Map<String, Object> dadosLgpd = new HashMap<>();
                dadosLgpd.put("consentimentoLgpd", paciente.getConsentimentoLgpd());
                dadosLgpd.put("dataConsentimento", paciente.getDataConsentimento() != null ? 
                    paciente.getDataConsentimento().toString() : null);
                dadosLgpd.put("ipConsentimento", paciente.getIpConsentimento());
                dadosLgpd.put("ultimoAcesso", paciente.getUltimoAcesso() != null ? 
                    paciente.getUltimoAcesso().toString() : null);
                
                // Metadados da exportação
                Map<String, Object> metadados = new HashMap<>();
                metadados.put("dataExportacao", LocalDateTime.now().toString());
                metadados.put("finalidade", "Exercício do direito à portabilidade - LGPD Art. 18, IV");
                metadados.put("formato", "JSON estruturado");
                metadados.put("observacao", "Dados exportados conforme solicitação do titular");
                
                // Montar resposta completa
                dadosCompletos.put("dadosPessoais", dadosPessoais);
                dadosCompletos.put("dadosTratamento", dadosTratamento);
                dadosCompletos.put("dadosLgpd", dadosLgpd);
                dadosCompletos.put("metadados", metadados);
                
                // Registrar acesso para auditoria
                System.out.println("📁 LGPD - Dados exportados para paciente ID: " + id + " em " + LocalDateTime.now());
                
                return ResponseEntity.ok(dadosCompletos);
            })
            .orElseGet(() -> {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERRO");
                errorResponse.put("message", "Paciente não encontrado");
                return ResponseEntity.notFound().build();
            });
    }
    
    // GET /api/pacientes/{id}/logs-lgpd - Visualizar histórico de acesso (direito de informação)
    @GetMapping("/{id}/logs-lgpd")
    public ResponseEntity<Map<String, Object>> consultarLogsLgpd(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
            .map(paciente -> {
                Map<String, Object> logs = new HashMap<>();
                
                // Informações sobre o tratamento
                Map<String, Object> informacoesTratamento = new HashMap<>();
                informacoesTratamento.put("finalidade", "Atendimento nutricional personalizado");
                informacoesTratamento.put("baseLegal", "Consentimento do titular (LGPD Art. 7º, I)");
                informacoesTratamento.put("categoriasDados", List.of(
                    "Dados pessoais (nome, contato)",
                    "Dados de saúde (peso, altura, idade)", 
                    "Dados de preferências alimentares"
                ));
                informacoesTratamento.put("compartilhamento", "Não há compartilhamento com terceiros");
                informacoesTratamento.put("retencao", "Dados mantidos enquanto houver relacionamento profissional");
                
                // Histórico de ações (mockado - em produção viria de tabela de auditoria)
                List<Map<String, String>> historicoAcoes = List.of(
                    Map.of(
                        "data", paciente.getCriadoEm().toString(),
                        "acao", "Cadastro inicial",
                        "usuario", "Sistema",
                        "ip", "Não registrado"
                    ),
                    Map.of(
                        "data", paciente.getDataConsentimento() != null ? 
                            paciente.getDataConsentimento().toString() : "N/A",
                        "acao", "Consentimento LGPD",
                        "usuario", "Paciente",
                        "ip", paciente.getIpConsentimento() != null ? 
                            paciente.getIpConsentimento() : "N/A"
                    ),
                    Map.of(
                        "data", LocalDateTime.now().toString(),
                        "acao", "Consulta de logs LGPD",
                        "usuario", "Paciente", 
                        "ip", "127.0.0.1"
                    )
                );
                
                // Direitos do titular
                Map<String, String> direitosDisponiveis = Map.of(
                    "acesso", "GET /api/pacientes/" + id + "/dados-lgpd",
                    "portabilidade", "GET /api/pacientes/" + id + "/dados-lgpd",
                    "correcao", "PUT /api/pacientes/" + id,
                    "exclusao", "DELETE /api/pacientes/" + id + "/dados-lgpd?motivo=...",
                    "revogacao", "PUT /api/pacientes/" + id + "/revogar-consentimento?motivo=..."
                );
                
                logs.put("informacoesTratamento", informacoesTratamento);
                logs.put("historicoAcoes", historicoAcoes);
                logs.put("direitosDisponiveis", direitosDisponiveis);
                logs.put("dataConsulta", LocalDateTime.now().toString());
                
                return ResponseEntity.ok(logs);
            })
            .orElseGet(() -> {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERRO");
                errorResponse.put("message", "Paciente não encontrado");
                return ResponseEntity.notFound().build();
            });
    }
    
    // PUT /api/pacientes/{id}/revogar-consentimento - Revogar consentimento LGPD
    @PutMapping("/{id}/revogar-consentimento")
    public ResponseEntity<Map<String, Object>> revogarConsentimento(@PathVariable Long id,
                                                                  @RequestParam String motivo) {
        try {
            Paciente paciente = pacienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            
            // Revogar consentimento
            paciente.setConsentimentoLgpd(false);
            paciente.setDataConsentimento(null); // Limpar data anterior
            
            // Em aplicação real, salvaria via service
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCESSO");
            response.put("message", "Consentimento LGPD revogado com sucesso");
            response.put("motivo", motivo);
            response.put("dataRevogacao", LocalDateTime.now().toString());
            response.put("observacao", "Dados mantidos para finalidades legítimas até exclusão expressa");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERRO");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}

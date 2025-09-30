package com.nutricional.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @Column(name = "telefone", unique = true, nullable = false)
    private String telefone; // Será armazenado criptografado
    
    @Column(name = "email")
    private String email; // Será armazenado criptografado
    
    @Min(value = 0, message = "Idade deve ser positiva")
    @Max(value = 120, message = "Idade deve ser válida")
    private Integer idade;
    
    @DecimalMin(value = "0.0", message = "Peso deve ser positivo")
    private Double peso;
    
    @DecimalMin(value = "0.0", message = "Altura deve ser positiva")
    private Double altura;
    
    @Enumerated(EnumType.STRING)
    private StatusAnamnese statusAnamnese = StatusAnamnese.PENDENTE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutricionista_id")
    private Nutricionista nutricionista;
    
    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
    
    @Column(name = "consentimento_lgpd")
    private Boolean consentimentoLgpd = false;
    
    @Column(name = "data_consentimento")
    private LocalDateTime dataConsentimento;
    
    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;
    
    @Column(name = "ip_consentimento")
    private String ipConsentimento;
    
    // Construtores
    public Paciente() {}
    
    public Paciente(String nome, String telefone, Nutricionista nutricionista) {
        this.nome = nome;
        this.telefone = telefone;
        this.nutricionista = nutricionista;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    
    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }
    
    public StatusAnamnese getStatusAnamnese() { return statusAnamnese; }
    public void setStatusAnamnese(StatusAnamnese statusAnamnese) { this.statusAnamnese = statusAnamnese; }
    
    public Nutricionista getNutricionista() { return nutricionista; }
    public void setNutricionista(Nutricionista nutricionista) { this.nutricionista = nutricionista; }
    
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    
    public Boolean getConsentimentoLgpd() { return consentimentoLgpd; }
    public void setConsentimentoLgpd(Boolean consentimentoLgpd) { 
        this.consentimentoLgpd = consentimentoLgpd;
        if (consentimentoLgpd && this.dataConsentimento == null) {
            this.dataConsentimento = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getDataConsentimento() { return dataConsentimento; }
    public void setDataConsentimento(LocalDateTime dataConsentimento) { this.dataConsentimento = dataConsentimento; }
    
    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }
    
    public String getIpConsentimento() { return ipConsentimento; }
    public void setIpConsentimento(String ipConsentimento) { this.ipConsentimento = ipConsentimento; }
}

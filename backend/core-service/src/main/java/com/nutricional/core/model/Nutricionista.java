package com.nutricional.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nutricionistas")
public class Nutricionista {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Email(message = "Email deve ser válido")
    private String email;
    
    @Column(nullable = false)
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;
    
    @Column(nullable = false)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @Column(unique = true)
    private String crn; // Registro profissional
    
    @OneToMany(mappedBy = "nutricionista", cascade = CascadeType.ALL)
    private List<Paciente> pacientes;
    
    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    // Construtores
    public Nutricionista() {}
    
    public Nutricionista(String nome, String email, String senha, String crn) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.crn = crn;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCrn() { return crn; }
    public void setCrn(String crn) { this.crn = crn; }
    
    public List<Paciente> getPacientes() { return pacientes; }
    public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }
    
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}

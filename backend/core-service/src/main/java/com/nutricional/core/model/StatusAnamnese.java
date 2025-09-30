package com.nutricional.core.model;

public enum StatusAnamnese {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em Andamento"), 
    CONCLUIDA("Concluída"),
    APROVADA("Aprovada");
    
    private final String descricao;
    
    StatusAnamnese(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}

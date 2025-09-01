package com.github.pblcnr.seraquepaguei.enums;

public enum StatusConta {
    PENDENTE("Pendente"),
    PAGA("Paga"),
    VENCIDA("Vencida"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

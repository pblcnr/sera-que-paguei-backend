package com.github.pblcnr.seraquepaguei.dto.conta;

import com.github.pblcnr.seraquepaguei.entity.Conta;
import com.github.pblcnr.seraquepaguei.entity.User;
import com.github.pblcnr.seraquepaguei.enums.StatusConta;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaResponseDTO {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private StatusConta statusConta;
    private String categoria;
    private Boolean recorrente;
    private Long userId;

    public ContaResponseDTO() {
    }

    public ContaResponseDTO(Long id, String descricao, BigDecimal valor, LocalDate dataVencimento, LocalDate dataPagamento, StatusConta statusConta, String categoria, Boolean recorrente, Long userId) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.statusConta = statusConta;
        this.categoria = categoria;
        this.recorrente = recorrente;
        this.userId = userId;
    }

    public static ContaResponseDTO fromEntity(Conta conta) {
        if (conta == null) {
            throw new IllegalArgumentException("Conta não pode ser nula");
        }

        ContaResponseDTO dto = new ContaResponseDTO();
        dto.setId(conta.getId());
        dto.setDescricao(conta.getDescricao());
        dto.setValor(conta.getValor());
        dto.setDataVencimento(conta.getDataVencimento());
        dto.setDataPagamento(conta.getDataPagamento());
        dto.setStatusConta(conta.getStatus());
        dto.setCategoria(conta.getCategoria());
        dto.setRecorrente(conta.getRecorrente());
        dto.setUserId(conta.getUsuario().getId());

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Boolean getRecorrente() {
        return recorrente;
    }

    public void setRecorrente(Boolean recorrente) {
        this.recorrente = recorrente;
    }

    public StatusConta getStatusConta() {
        return statusConta;
    }

    public void setStatusConta(StatusConta statusConta) {
        this.statusConta = statusConta;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

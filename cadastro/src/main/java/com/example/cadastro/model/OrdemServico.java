package com.example.cadastro.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Servico servico;

    @ManyToOne
    private Funcionario funcionario;

    private String descricao;
    private BigDecimal valor;

    private LocalDateTime dataEmissao;

    private String status;
    private String formaPagamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getDataEmissaoAsDate() {
        return java.sql.Timestamp.valueOf(this.dataEmissao);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }


    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean ordemPadrao;


    public boolean isOrdemPadrao() {
        return ordemPadrao;
    }

    public void setOrdemPadrao(boolean ordemPadrao) {
        this.ordemPadrao = ordemPadrao;
    }
}

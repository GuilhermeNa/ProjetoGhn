package br.com.transporte.AppGhn.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Motorista implements Serializable {
    private LocalDate dataNascimento;
    private String img;
    private String nome;
    private String cpf;
    private String cnh;
    private LocalDate cnhValidade;
    private LocalDate dataContratacao;
    private BigDecimal salarioBase;
    private BigDecimal percentualComissao;
    private BigDecimal salarioRecebido;
    private int id;

    public Motorista(LocalDate dataNascimento, String img, String nome, String cpf, String cnh, LocalDate cnhValidade, LocalDate dataContratacao, BigDecimal salarioBase) {
        this.dataNascimento = dataNascimento;
        this.img = img;
        this.nome = nome;
        this.cpf = cpf;
        this.cnh = cnh;
        this.cnhValidade = cnhValidade;
        this.dataContratacao = dataContratacao;
        this.salarioBase = salarioBase;
    }

    public Motorista() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean temIdValido() {
        return id > 0;
    }

    public LocalDate getCnhValidade() {
        return cnhValidade;
    }

    public void setCnhValidade(LocalDate cnhValidade) {
        this.cnhValidade = cnhValidade;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public BigDecimal getPercentualComissao() {
        return percentualComissao;
    }

    public void setPercentualComissao(BigDecimal percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    public BigDecimal getSalarioRecebido() {
        return salarioRecebido;
    }

    public void setSalarioRecebido(BigDecimal salarioRecebido) {
        this.salarioRecebido = salarioRecebido;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

}


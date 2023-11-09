package br.com.transporte.appGhn.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Motorista implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String img, nome, cpf, cnh;
    private LocalDate cnhValidade, dataContratacao, dataNascimento;
    private BigDecimal salarioBase, percentualComissao, salarioRecebido;

    public Motorista() {}

    @NonNull
    @Override
    public String toString() {
        final String[] arrayDoNome = nome.split(" ");

        final String nome = arrayDoNome[0];
        final String sobreNome = arrayDoNome[1];

        final String nomeESobreNome;
        if(sobreNome != null){
            nomeESobreNome = nome + " " + sobreNome;
        } else {
            nomeESobreNome = nome;
        }

        return nomeESobreNome;
    }

    //----------------------------------------------------------------------------------------------


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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


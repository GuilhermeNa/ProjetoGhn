package br.com.transporte.appGhn.model.custos;

import androidx.room.Entity;

import java.io.Serializable;

import br.com.transporte.appGhn.model.abstracts.Custos;
import br.com.transporte.appGhn.model.enums.TipoCustoDePercurso;

@Entity
public class CustosDePercurso extends Custos implements Serializable {
    private String descricao;
    private TipoCustoDePercurso tipo;

    public CustosDePercurso() {}

    // ------------------------------ Getters e Setters --------------------------------------------

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoCustoDePercurso getTipo() {
        return tipo;
    }

    public void setTipo(TipoCustoDePercurso tipo) {
        this.tipo = tipo;
    }

}

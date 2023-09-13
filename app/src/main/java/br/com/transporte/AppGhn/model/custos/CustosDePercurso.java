package br.com.transporte.AppGhn.model.custos;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Custos;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;

@Entity
public class CustosDePercurso extends Custos implements Serializable {
    private String descricao;
    private TipoCustoDePercurso tipo;

    @Ignore
    public CustosDePercurso(LocalDate data, BigDecimal valorCusto, String descricao, TipoCustoDePercurso tipo, Long refCavalo) {
        super.setData(data);
        super.setValorCusto(valorCusto);
        this.descricao = descricao;
        this.tipo = tipo;
        super.setRefCavaloId(refCavalo);
    }

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

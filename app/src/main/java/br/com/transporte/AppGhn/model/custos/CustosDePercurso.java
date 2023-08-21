package br.com.transporte.AppGhn.model.custos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Custos;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;

public class CustosDePercurso extends Custos implements Serializable {
    private String descricao;
    private TipoCustoDePercurso tipo;

    public CustosDePercurso(LocalDate data, BigDecimal valorCusto, String descricao, TipoCustoDePercurso tipo, int refCavalo) {
        super.setData(data);
        super.setValorCusto(valorCusto);
        this.descricao = descricao;
        this.tipo = tipo;
        super.setRefCavalo(refCavalo);
    }

    public CustosDePercurso() {}

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

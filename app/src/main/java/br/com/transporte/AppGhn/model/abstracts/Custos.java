package br.com.transporte.AppGhn.model.abstracts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public abstract class Custos extends CustosEDespesas {
 /*   @PrimaryKey(autoGenerate = true)
    private Long id;*/
    private BigDecimal valorCusto;
    private boolean apenasAdmEdita;

    //------

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    public boolean isApenasAdmEdita() {
        return apenasAdmEdita;
    }

    public void setApenasAdmEdita(boolean apenasAdmEdita) {
        this.apenasAdmEdita = apenasAdmEdita;
    }

}

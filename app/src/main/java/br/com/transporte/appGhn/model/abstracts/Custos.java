package br.com.transporte.appGhn.model.abstracts;

import androidx.room.Entity;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public abstract class Custos extends CustosEDespesas implements Serializable {
    private BigDecimal valorCusto;
    private boolean apenasAdmEdita;

    //------

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }

    public boolean isApenasAdmEdita() {
        return apenasAdmEdita;
    }

    public void setApenasAdmEdita(boolean apenasAdmEdita) {
        this.apenasAdmEdita = apenasAdmEdita;
    }

}

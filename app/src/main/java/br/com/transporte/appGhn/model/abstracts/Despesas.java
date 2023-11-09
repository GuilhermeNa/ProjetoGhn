package br.com.transporte.appGhn.model.abstracts;

import androidx.room.Entity;

import java.math.BigDecimal;

import br.com.transporte.appGhn.model.enums.TipoDespesa;

@Entity
public abstract class Despesas extends CustosEDespesas {
    private BigDecimal valorDespesa;
    private TipoDespesa tipoDespesa;

    public BigDecimal getValorDespesa() {
        return valorDespesa;
    }

    public void setValorDespesa(BigDecimal valorDespesa) {
        this.valorDespesa = valorDespesa;
    }

    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

}

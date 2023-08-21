package br.com.transporte.AppGhn.model.abstracts;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.model.enums.TipoDespesa;

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

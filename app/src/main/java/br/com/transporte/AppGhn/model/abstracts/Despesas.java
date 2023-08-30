package br.com.transporte.AppGhn.model.abstracts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.model.enums.TipoDespesa;

@Entity
public abstract class Despesas extends CustosEDespesas {

    @PrimaryKey(autoGenerate = true)
    private Long id;

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

    public boolean temIdValido() {
        return id > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

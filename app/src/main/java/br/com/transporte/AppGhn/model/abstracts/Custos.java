package br.com.transporte.AppGhn.model.abstracts;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Custos extends CustosEDespesas {
    private BigDecimal valorCusto;
    private LocalDate data;
    private int id;
    private int refCavalo;

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }

    @Override
    public LocalDate getData() {
        return data;
    }

    @Override
    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRefCavalo() {
        return refCavalo;
    }

    public void setRefCavalo(int refCavalo) {
        this.refCavalo = refCavalo;
    }

}

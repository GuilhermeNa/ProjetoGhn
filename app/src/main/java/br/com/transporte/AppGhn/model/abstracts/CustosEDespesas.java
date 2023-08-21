package br.com.transporte.AppGhn.model.abstracts;

import java.time.LocalDate;

abstract class CustosEDespesas {
    private LocalDate data;
    private int refCavalo;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getRefCavalo() {
        return refCavalo;
    }

    public void setRefCavalo(int refCavalo) {
        this.refCavalo = refCavalo;
    }
}

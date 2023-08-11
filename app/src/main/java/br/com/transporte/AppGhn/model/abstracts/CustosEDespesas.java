package br.com.transporte.AppGhn.model.abstracts;

import java.time.LocalDate;

abstract class CustosEDespesas {
    private LocalDate data;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

}

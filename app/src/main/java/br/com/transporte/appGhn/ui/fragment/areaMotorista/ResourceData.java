package br.com.transporte.appGhn.ui.fragment.areaMotorista;

import java.time.LocalDate;

public class ResourceData {
    private final LocalDate dataInicial;
    private final LocalDate dataFinal;

    public ResourceData(LocalDate dataInicial, LocalDate dataFinal) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }
}

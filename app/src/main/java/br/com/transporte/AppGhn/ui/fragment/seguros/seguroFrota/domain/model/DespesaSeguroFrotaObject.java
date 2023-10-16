package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaSeguroFrotaObject {
    private String placa;
    private final BigDecimal valor;
    private final LocalDate dataInicial;
    private final LocalDate dataFinal;
    private final Long idSeguro;
    private final Long idCavalo;

    public DespesaSeguroFrotaObject(
            Long seguroId,
            Long refCavaloId,
            BigDecimal valorDespesa,
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {
        this.idSeguro = seguroId;
        this.idCavalo = refCavaloId;
        this.valor = valorDespesa;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public Long getIdSeguro() {
        return idSeguro;
    }

    public Long getIdCavalo() {
        return idCavalo;
    }
}

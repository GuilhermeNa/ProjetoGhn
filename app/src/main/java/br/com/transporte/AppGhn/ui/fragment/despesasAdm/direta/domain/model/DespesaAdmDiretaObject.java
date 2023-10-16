package br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaAdmDiretaObject {

    private final Long idCertificado;
    private final Long idCavalo;

    private String placa;
    private final BigDecimal valor;
    private final LocalDate data;
    private final String descricao;

    public DespesaAdmDiretaObject(
            Long id,
            Long refCavaloId,
            BigDecimal valorDespesa,
            LocalDate data,
            String descricao
    ) {
        this.idCertificado = id;
        this.idCavalo = refCavaloId;
        this.valor = valorDespesa;
        this.data = data;
        this.descricao = descricao;
    }

    public Long getIdCertificado() {
        return idCertificado;
    }

    public Long getIdCavalo() {
        return idCavalo;
    }

    public String getPlaca() {
        return placa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}

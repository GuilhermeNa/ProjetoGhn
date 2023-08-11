package br.com.transporte.AppGhn.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum TipoCertificado {

    CRONOTACOGRAFO("CRONOTACOGRAFO", TipoDespesa.DIRETA),
    AET_ESTADUAL("AET_ESTADUAL", TipoDespesa.DIRETA ),
    AET_FEDERAL("AET_FEDERAL", TipoDespesa.DIRETA),
    CRLV("CRLV", TipoDespesa.DIRETA),
    OUTROS_DIRETA("OUTROS_DIRETA", TipoDespesa.DIRETA),

    DIGITAL("DIGITAL", TipoDespesa.INDIRETA),
    ANTT("ANTT", TipoDespesa.INDIRETA),
    OUTROS_INDIRETA("OUTROS_INDIRETA", TipoDespesa.INDIRETA);

    private final String descricao;
    private final TipoDespesa tipoDespesa;

    TipoCertificado(String descricao, TipoDespesa tipoDespesa) {
        this.descricao = descricao;
        this.tipoDespesa = tipoDespesa;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public static List<String> listaCertificadosEmString() {
        List<String> lista = new ArrayList<>();

        lista.add(TipoCertificado.CRLV.descricao);
        lista.add(TipoCertificado.CRONOTACOGRAFO.descricao);
        lista.add(TipoCertificado.AET_ESTADUAL.descricao);
        lista.add(TipoCertificado.AET_FEDERAL.descricao);
        lista.add(TipoCertificado.OUTROS_DIRETA.descricao);
        lista.add(TipoCertificado.DIGITAL.descricao);
        lista.add(TipoCertificado.ANTT.descricao);
        lista.add(TipoCertificado.OUTROS_INDIRETA.descricao);

        return lista;
    }

    public static List<TipoCertificado> listaCertificados() {
        List<TipoCertificado> lista = new ArrayList<>();

        lista.add(TipoCertificado.CRLV);
        lista.add(TipoCertificado.CRONOTACOGRAFO);
        lista.add(TipoCertificado.AET_ESTADUAL);
        lista.add(TipoCertificado.AET_FEDERAL);
        lista.add(TipoCertificado.OUTROS_DIRETA);
        lista.add(TipoCertificado.DIGITAL);
        lista.add(TipoCertificado.ANTT);
        lista.add(TipoCertificado.OUTROS_INDIRETA);

        return lista;
    }

}

package br.com.transporte.AppGhn.model.enums;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum TipoDeImposto {

    IRRF("IRRF"),
    INSS("INSS"),
    FGTS("FGTS"),
    SIMPLES_NAC("SIMPLES NAC"),
    IPVA("IPVA"),
    ICMS("ICMS"),
    ISS("ISS"),
    PIS("PIS"),
    PASEP("PASEP"),
    CSLL("CSLL"),
    COFINS("COFINS"),
    IPI("IPI"),
    IRPJ("IRPJ");

    private final String descricao;

    TipoDeImposto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @NonNull
    public static List<String> listaDeImpostos() {
        List<String> lista = new ArrayList<>();
        lista.add(IRRF.getDescricao());
        lista.add(INSS.getDescricao());
        lista.add(FGTS.getDescricao());
        lista.add(SIMPLES_NAC.getDescricao());
        lista.add(IPVA.getDescricao());
        lista.add(ICMS.getDescricao());
        lista.add(ISS.getDescricao());
        lista.add(PIS.getDescricao());
        lista.add(PASEP.getDescricao());
        lista.add(CSLL.getDescricao());
        lista.add(COFINS.getDescricao());
        lista.add(IPI.getDescricao());
        lista.add(IRPJ.getDescricao());

        return lista;
    }
}

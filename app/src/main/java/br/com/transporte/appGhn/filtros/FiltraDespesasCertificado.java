package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.enums.TipoDespesa;

public class FiltraDespesasCertificado {

    public static List<DespesaCertificado> listaPorTipoDespesa(@NonNull List<DespesaCertificado> dataSet, TipoDespesa tipo) {
        return dataSet.stream()
                .filter(c -> c.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    /** @noinspection NumberEquality*/
    public static List<DespesaCertificado> listaPorCavaloId(@NonNull List<DespesaCertificado> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(c -> c.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<DespesaCertificado> listaPorAno(@NonNull List<DespesaCertificado> dataSet, int ano){
        return dataSet.stream()
                .filter(d -> d.getDataDeEmissao().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<DespesaCertificado> listaDoMesSolicitado(@NonNull List<DespesaCertificado> dataSet, int mes) {
        return dataSet.stream()
                .filter(d -> d.getDataDeEmissao().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

    public static List<DespesaCertificado> listaPorStatus(@NonNull List<DespesaCertificado> dataSet, boolean isValido) {
        if (isValido) return dataSet.stream()
                .filter(DespesaCertificado::isValido)
                .collect(Collectors.toList());
        else
            return dataSet;
    }

}
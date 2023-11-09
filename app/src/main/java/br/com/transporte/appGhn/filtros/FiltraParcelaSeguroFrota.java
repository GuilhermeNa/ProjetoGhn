package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;

public class FiltraParcelaSeguroFrota {

    /** @noinspection NumberEquality*/
    public static List<Parcela_seguroFrota> listaPorCavaloId(@NonNull List<Parcela_seguroFrota> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(p -> p.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaPorAno(@NonNull List<Parcela_seguroFrota> dataSet, int ano){
        return dataSet.stream()
                .filter(p -> p.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaPorTipo(@NonNull List<Parcela_seguroFrota> dataSet, TipoDespesa tipo){
        return dataSet.stream()
                .filter(p -> p.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaDoMesSolicitado(@NonNull List<Parcela_seguroFrota> dataSet, int mes){
        return dataSet.stream()
                .filter(p -> p.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

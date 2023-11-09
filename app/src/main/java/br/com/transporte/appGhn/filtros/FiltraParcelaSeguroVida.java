package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;

public class FiltraParcelaSeguroVida {

    public static List<Parcela_seguroVida> listaPorAno(@NonNull List<Parcela_seguroVida> dataSet, int ano) {
        return dataSet.stream()
                .filter(p -> p.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    /** @noinspection NumberEquality*/
    public static List<Parcela_seguroVida> listaPorCavaloId(@NonNull List<Parcela_seguroVida> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(p -> p.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroVida> listaDoMesSolicitado(@NonNull List<Parcela_seguroVida> dataSet, int mes){
        return dataSet.stream()
                .filter(p -> p.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }
}

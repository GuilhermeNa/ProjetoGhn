package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;

public class FiltraParcelaSeguroVida {

    public static List<Parcela_seguroVida> listaPorAno(@NonNull List<Parcela_seguroVida> dataSet, int ano) {
        return dataSet.stream()
                .filter(p -> p.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroVida> listaPorStatusDePagamento(@NonNull List<Parcela_seguroVida> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(Parcela::isPaga)
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(p -> !p.isPaga())
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroVida> listaPeloCavaloId(@NonNull List<Parcela_seguroVida> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(p -> p.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroVida> listaPorMes(@NonNull List<Parcela_seguroVida> dataSet, int mes){
        return dataSet.stream()
                .filter(p -> p.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }
}

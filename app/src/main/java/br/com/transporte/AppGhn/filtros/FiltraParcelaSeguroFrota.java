package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;

public class FiltraParcelaSeguroFrota {

    public static List<Parcela_seguroFrota> listaPeloIdDoSeguro(@NonNull List<Parcela_seguroFrota> dataSet, int seguroId) {
        return dataSet.stream()
                .filter(p -> p.getRefSeguro() == seguroId)
                .collect(Collectors.toList());
    }

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

    public static List<Parcela_seguroFrota> listaPorStatusDePagamento(@NonNull List<Parcela_seguroFrota> dataSet, boolean isPago){
        if(isPago) return dataSet.stream()
                .filter(Parcela::isPaga)
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(p -> !p.isPaga())
                .collect(Collectors.toList());
    }

}

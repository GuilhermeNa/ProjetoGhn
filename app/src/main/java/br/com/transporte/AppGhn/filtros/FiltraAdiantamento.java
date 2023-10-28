package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.Adiantamento;

public class FiltraAdiantamento {

    @NonNull
    public static Adiantamento localizaPeloId(@NonNull final List<Adiantamento> dataset, final Long adiantamentoId) {
        Adiantamento adiantamentoLocalizado = null;
        for (Adiantamento a : dataset) {
            if (a.getId() == adiantamentoId) {
                adiantamentoLocalizado = a;
            }
        }
      return adiantamentoLocalizado;
    }

    public static List<Adiantamento> listaPorCavaloId(@NonNull List<Adiantamento> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(adiantamento -> adiantamento.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<Adiantamento> listaPorStatus(@NonNull List<Adiantamento> dataSet, boolean isPago) {
        if(isPago)
        return dataSet.stream()
                .filter(Adiantamento::isAdiantamentoJaFoiDescontado)
                .collect(Collectors.toList());
        else
            return dataSet.stream()
                    .filter(a -> !a.isAdiantamentoJaFoiDescontado())
                    .collect(Collectors.toList());
    }

}

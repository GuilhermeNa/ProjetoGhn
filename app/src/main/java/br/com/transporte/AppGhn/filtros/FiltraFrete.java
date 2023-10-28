package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraFrete {
    @NonNull
    public static Frete localizaPeloId(@NonNull final List<Frete> dataSet, final Long freteId) {
        Frete freteLocalizado = null;
        for (Frete f : dataSet) {
            if (f.getId() == freteId) {
                freteLocalizado = f;
            }
        }
        return freteLocalizado;
    }

    public static List<Frete> listaPorData(@NonNull List<Frete> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(f -> DataUtil.verificaSeEstaNoRange(f.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorCavaloId(@NonNull List<Frete> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(f -> f.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorStatusDePagamentoDaComissao(@NonNull List<Frete> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(Frete::isComissaoJaFoiPaga)
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(f -> !f.isComissaoJaFoiPaga())
                .collect(Collectors.toList());
    }


    public static List<Frete> listaPorStatusDeRecebimentoDoFrete(@NonNull List<Frete> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(Frete::isFreteJaFoiPago)
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(f -> !f.isFreteJaFoiPago())
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorAno(@NonNull List<Frete> dataSet, int ano) {
        return dataSet.stream()
                .filter(f -> f.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<Frete> listaDoMesSolicitado(@NonNull List<Frete> dataSet, int mes) {
        return dataSet.stream()
                .filter(f -> f.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.util.DataUtil;

public class FiltraCustosAbastecimento {

    /** @noinspection NumberEquality*/
    public static List<CustosDeAbastecimento> listaPorCavaloId(@NonNull List<CustosDeAbastecimento> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(c -> c.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<CustosDeAbastecimento> listaPorData(@NonNull List<CustosDeAbastecimento> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(a -> DataUtil.verificaSeEstaNoRange(a.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<CustosDeAbastecimento> listaPorAno(@NonNull List<CustosDeAbastecimento> dataSet, int ano) {
        return dataSet.stream()
                .filter(c -> c.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<CustosDeAbastecimento> listaDoMesSolicitado(@NonNull List<CustosDeAbastecimento> dataSet, int mes) {
        return dataSet.stream()
                .filter(c -> c.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

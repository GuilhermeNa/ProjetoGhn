package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.util.DataUtil;

public class FiltraCustosManutencao {

    /** @noinspection NumberEquality*/
    public static List<CustosDeManutencao> listaPorCavaloId(@NonNull List<CustosDeManutencao> dataSet, Long cavaloId){
        return dataSet.stream()
                .filter(c -> c.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<CustosDeManutencao> listaPorData(
            @NonNull final List<CustosDeManutencao> dataSet,
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ){
        return dataSet.stream()
                .filter(c-> DataUtil.verificaSeEstaNoRange(c.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<CustosDeManutencao> listaPorAno(@NonNull List<CustosDeManutencao> dataSet, int ano){
        return dataSet.stream()
                .filter(c -> c.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<CustosDeManutencao> listaDoMesSolicitado(@NonNull List<CustosDeManutencao> dataSet, int mes){
        return dataSet.stream()
                .filter(c -> c.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

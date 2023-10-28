package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraDespesasImposto {

    public static List<DespesasDeImposto> listaFiltradaPorData(@NonNull List<DespesasDeImposto> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(d -> DataUtil.verificaSeEstaNoRange(d.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<DespesasDeImposto> listaPorCavaloId(@NonNull List<DespesasDeImposto> dataSet, Long cavaloId){
        return dataSet.stream()
                .filter(d -> d.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<DespesasDeImposto> listaPorTipo(@NonNull List<DespesasDeImposto> dataSet, TipoDespesa tipo){
        return dataSet.stream()
                .filter(d -> d.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    public static List<DespesasDeImposto> listaPorAno(@NonNull List<DespesasDeImposto> dataSet, int ano){
        return dataSet.stream()
                .filter(d -> d.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<DespesasDeImposto> listaPorMes(@NonNull List<DespesasDeImposto> dataSet, int mes){
        return dataSet.stream()
                .filter(d -> d.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

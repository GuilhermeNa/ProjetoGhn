package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.appGhn.util.DataUtil;

public class FiltraCustosPercurso {

    /** @noinspection NumberEquality*/
    @NonNull
    public static CustosDePercurso localizaPeloId(@NonNull final List<CustosDePercurso> dataSet, final Long custoId) {
        CustosDePercurso custoLocalizado = null;
        for (CustosDePercurso c : dataSet) {
            if (c.getId() == custoId) {
                custoLocalizado = c;
            }
        }
        return custoLocalizado;
    }

    /** @noinspection NumberEquality*/
    public static List<CustosDePercurso> listaPorCavaloId(@NonNull List<CustosDePercurso> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(c -> c.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<CustosDePercurso> listaPorTipo(@NonNull List<CustosDePercurso> dataSet, TipoCustoDePercurso tipo) {
        return dataSet.stream()
                .filter(c -> c.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    public static List<CustosDePercurso> listaPorData(@NonNull List<CustosDePercurso> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(c -> DataUtil.verificaSeEstaNoRange(c.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<CustosDePercurso> listaPorAno(@NonNull List<CustosDePercurso> dataSet, int ano) {
        return dataSet.stream()
                .filter(c -> c.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<CustosDePercurso> listaDoMesSolicitado(@NonNull List<CustosDePercurso> dataSet, int mes) {
        return dataSet.stream()
                .filter(c -> c.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

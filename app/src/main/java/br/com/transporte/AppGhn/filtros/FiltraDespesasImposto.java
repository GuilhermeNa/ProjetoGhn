package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.DespesasImpostoDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraDespesasImposto {
    private static final DespesasImpostoDAO dao = new DespesasImpostoDAO();

    @NonNull
    public static DespesasDeImposto localizaPeloId(int despesaId) throws ObjetoNaoEncontrado {
        DespesasDeImposto despesaLocalizada = null;

        for (DespesasDeImposto d : dao.listaTodos()) {
            if (d.getId() == despesaId) {
                despesaLocalizada = d;
            }
        }

        if (despesaLocalizada != null) {
            return despesaLocalizada;
        }

        throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public static List<DespesasDeImposto> listaFiltradaPorData(@NonNull List<DespesasDeImposto> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(d -> DataUtil.verificaSeEstaNoRange(d.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<DespesasDeImposto> listaPorCavaloId(@NonNull List<DespesasDeImposto> dataSet, int cavaloId){
        return dataSet.stream()
                .filter(d -> d.getRefCavalo() == cavaloId)
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

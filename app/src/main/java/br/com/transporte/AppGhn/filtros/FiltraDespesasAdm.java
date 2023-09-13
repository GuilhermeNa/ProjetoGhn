package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraDespesasAdm {
    private static final DespesasAdmDAO dao = new DespesasAdmDAO();

    @NonNull
    public static DespesaAdm localizaPeloId(int despesaId) throws ObjetoNaoEncontrado {
        DespesaAdm despesaLocalizada = null;

        for (DespesaAdm d : dao.listaTodos()) {
            if (d.getId() == despesaId) {
                despesaLocalizada = d;
            }
        }

        if (despesaLocalizada != null) {
            return despesaLocalizada;
        }

        throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public static List<DespesaAdm> listaPorCavaloId(@NonNull List<DespesaAdm> dataSet, Long cavaloId){
        return dataSet.stream()
                .filter(d -> d.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<DespesaAdm> listaPorData(@NonNull List<DespesaAdm> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(d -> DataUtil.verificaSeEstaNoRange(d.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<DespesaAdm> listaPorTipo(@NonNull List<DespesaAdm> dataSet, TipoDespesa tipo){
        return dataSet.stream()
                .filter(d -> d.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    public static List<DespesaAdm> listaPorAno(@NonNull List<DespesaAdm> dataSet, int ano){
        return dataSet.stream()
                .filter(d -> d.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<DespesaAdm> listaPorMes(@NonNull List<DespesaAdm> dataSet, int mes){
        return dataSet.stream()
                .filter(d -> d.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }






}

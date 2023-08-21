package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.DespesasCertificadoDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class FiltraDespesasCertificado {
    private static final DespesasCertificadoDAO dao = new DespesasCertificadoDAO();


    @NonNull
    public static DespesaCertificado localizaPeloId(int despesaId) throws ObjetoNaoEncontrado {
        DespesaCertificado despesaLocalizada = null;

        for (DespesaCertificado d : dao.listaTodos()) {
            if (d.getId() == despesaId) {
                despesaLocalizada = d;
            }
        }

        if (despesaLocalizada != null) {
            return despesaLocalizada;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    public static List<DespesaCertificado> listaPorTipoDespesa(@NonNull List<DespesaCertificado> dataSet, TipoDespesa tipo) {
        return dataSet.stream()
                .filter(c -> c.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    public static List<DespesaCertificado> listaPorCavaloId(@NonNull List<DespesaCertificado> dataSet, int cavaloId) {
        return dataSet.stream()
                .filter(c -> c.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<DespesaCertificado> listaPorAno(@NonNull List<DespesaCertificado> dataSet, int ano){
        return dataSet.stream()
                .filter(d -> d.getDataDeEmissao().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<DespesaCertificado> listaPorMes(@NonNull List<DespesaCertificado> dataSet, int mes) {
        return dataSet.stream()
                .filter(d -> d.getDataDeEmissao().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}
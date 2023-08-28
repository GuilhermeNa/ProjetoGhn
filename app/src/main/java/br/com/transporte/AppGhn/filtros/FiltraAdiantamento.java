package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.Adiantamento;

public class FiltraAdiantamento {
    private static final AdiantamentoDAO dao = new AdiantamentoDAO();

    @NonNull
    public static Adiantamento localizaPeloId(int adiantamentoId) throws ObjetoNaoEncontrado {
        Adiantamento adiantamentoLocalizado = null;

        for (Adiantamento a : dao.listaTodos()) {
            if (a.getId() == adiantamentoId) {
                adiantamentoLocalizado = a;
            }
        }

        if (adiantamentoLocalizado != null) return adiantamentoLocalizado;
        else throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public static List<Adiantamento> listaPorCavaloId(@NonNull List<Adiantamento> dataSet, int cavaloId) {
        return dataSet.stream()
                .filter(adiantamento -> adiantamento.getRefCavalo() == cavaloId)
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

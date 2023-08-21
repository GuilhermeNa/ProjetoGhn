package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.Adiantamento;

public class FiltroAdiantamento {
    private static final AdiantamentoDAO dao = new AdiantamentoDAO();

    @NonNull
    public static Adiantamento localizaPeloId(int adiantamentoId) throws ObjetoNaoEncontrado {
        Adiantamento adiantamentoLocalizado = null;

        for (Adiantamento a : dao.listaTodos()) {
            if (a.getId() == adiantamentoId) {
                adiantamentoLocalizado = a;
            }
        }

        if (adiantamentoLocalizado != null) {
            return adiantamentoLocalizado;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Adiantamento> listaPorCavaloId(int cavaloId) {
        return dao.listaTodos().stream()
                .filter(adiantamento -> adiantamento.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Adiantamento> listaPorCavaloEAberto(int cavaloId) {
        return listaPorCavaloId(cavaloId).stream()
                .filter(a -> !a.isAdiantamentoJaFoiPago())
                .collect(Collectors.toList());
    }


}

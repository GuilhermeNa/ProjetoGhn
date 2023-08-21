package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.DespesasSeguroDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;

public class FiltraDespesasSeguro {
    private static final DespesasSeguroDAO dao = new DespesasSeguroDAO();

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static DespesaComSeguro localizaPeloId(int despesaId) throws ObjetoNaoEncontrado {
        DespesaComSeguro despesaLocalizada = null;

        for (DespesaComSeguro d : dao.listaTodos()) {
            if (d.getId() == despesaId) {
                despesaLocalizada = d;
            }
        }

        if (despesaLocalizada != null) {
            return despesaLocalizada;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<DespesaComSeguroFrota> listaFrota_valida() {
        return Collections.singletonList((DespesaComSeguroFrota) dao.listaTodos().stream()
                .filter(d -> d instanceof DespesaComSeguroFrota && d.isValido())
                .collect(Collectors.toList()));
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<DespesaComSeguroDeVida> listaVida_valida() {
        return Collections.singletonList((DespesaComSeguroDeVida) dao.listaTodos().stream()
                .filter(d -> d instanceof DespesaComSeguroDeVida && d.isValido())
                .collect(Collectors.toList()));
    }

}

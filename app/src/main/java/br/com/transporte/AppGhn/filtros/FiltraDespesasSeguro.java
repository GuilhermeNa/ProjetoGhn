package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

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

        throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    @NonNull
    public static List<DespesaComSeguroFrota> listaFrota_valida(List<DespesaComSeguroFrota> dataSet) {
        return dataSet.stream()
                .filter(DespesaComSeguro::isValido)
                .collect(Collectors.toList());
    }

    public static List<DespesaComSeguroFrota> listaFrota_ano(List<DespesaComSeguroFrota> dataSet, int ano){
        return dataSet.stream()
                .filter(s -> s.getData().getYear() == ano)
                .collect(Collectors.toList());
    }


    @NonNull
    public static List<DespesaComSeguroDeVida> listaVida_valida() {
        return Collections.singletonList((DespesaComSeguroDeVida) dao.listaTodos().stream()
                .filter(d -> d instanceof DespesaComSeguroDeVida && d.isValido())
                .collect(Collectors.toList()));
    }

    public static List<DespesaComSeguroDeVida> listaVida_ano(@NonNull List<DespesaComSeguroDeVida> dataSet, int ano){
        return dataSet.stream()
                .filter(s -> s.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

}

package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import android.content.Context;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;

public abstract class BarChartExtension {
    private static final List<String> meses =
            Arrays.asList("Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                    "Jul", "Ago", "Set", "Out", "Nov", "Dez");
    private TipoDeRequisicao tipo;

    public static List<String> getMeses() {
        return meses;
    }

    @NonNull
    public static List<BigDecimal> removeValoresVaziosDaLista(List<BigDecimal> listaRecebida) {
        List<BigDecimal> listaDeDadosAtualizada = new ArrayList<>(listaRecebida);

        for (int i = 11; i > -1; i--) {
            int compare = listaRecebida.get(i).compareTo(BigDecimal.ZERO);
            if (compare == 0) {
                listaDeDadosAtualizada.remove(i);
            } else {
                break;
            }
        }
        return listaDeDadosAtualizada;
    }

    public static List<String> removeMesesVaziosDaLista(List<BigDecimal> listaRecebida) {
        List<String> listaDeMeses = new ArrayList<>(BarChartExtension.getMeses());

        for (int i = 11; i > -1; i--) {
            int compare = listaRecebida.get(i).compareTo(BigDecimal.ZERO);
            if (compare == 0) {
                listaDeMeses.remove(i);
            } else {
                break;
            }
        }

        return listaDeMeses;
    }

    @NonNull
    public static List<BigDecimal> filtraListaParaExibicao(int ano, TipoDeRequisicao tipo, Long id, Context context) {
        List<BigDecimal> listaInterna = new ArrayList<>();
        BarCharCalculosExtension barCharCalculosExtension = new BarCharCalculosExtension(context);

        HashMap<Integer, BigDecimal> mapComValoresMensaisCalculados = barCharCalculosExtension.getHashMap_ChaveMes_ValorResultado(ano, tipo, id);

        for (int i = 0; i < mapComValoresMensaisCalculados.size(); i++) {
            listaInterna.add(mapComValoresMensaisCalculados.get(i));
        }

        return listaInterna;
    }


}

package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedBarChartData;

public class ManipulaBarChartResourceExt {

    private static final List<String> meses =
            Arrays.asList("Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                    "Jul", "Ago", "Set", "Out", "Nov", "Dez");

    protected List<BigDecimal> removeValoresVaziosDaListaParaQueUltimoMesExibidoSejaUltimoMesComValorApurado(
            final List<BigDecimal> listaRecebida
    ) {
        for (int i = 11; i > -1; i--) {
            int compare = listaRecebida.get(i).compareTo(BigDecimal.ZERO);
            if (compare == 0) {
                listaRecebida.remove(i);
            } else {
                break;
            }
        }
        return listaRecebida;
    }

    @NonNull
    protected List<String> criaListaDeMeses(final List<BigDecimal> barChartMappedData) {
        final List<String> listaDeMeses = new ArrayList<>(meses);

        for (int i = 11; i > -1; i--) {
            int compare = barChartMappedData.get(i).compareTo(BigDecimal.ZERO);
            if (compare == 0) {
                listaDeMeses.remove(i);
            } else {
                break;
            }
        }
        return listaDeMeses;
    }

    protected int getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(@NonNull int monthValue) {
        return monthValue - 1;
    }

    @NonNull
    protected List<BigDecimal> adicionaValoresDoHashEmUmaListaMapeada(@NonNull HashMap<Integer, BigDecimal> hashMap) {
        final List<BigDecimal> listaComValoresDeCadaMes =
                new ArrayList<>();

        for (int i = 0; i < hashMap.size(); i++) {
            listaComValoresDeCadaMes.add(hashMap.get(i));
        }
        return listaComValoresDeCadaMes;
    }

    @NonNull
    protected MappedBarChartData mapeiaBarChartData(List<BigDecimal> listaComValoresDeCadaMes) {
        final MappedBarChartData mappedBarChartData =
                new MappedBarChartData();

        final List<String> listaDeMeses =
                criaListaDeMeses(listaComValoresDeCadaMes);
        mappedBarChartData.setListaDeMeses(listaDeMeses);

        final List<BigDecimal> listaDeValores =
                removeValoresVaziosDaListaParaQueUltimoMesExibidoSejaUltimoMesComValorApurado(listaComValoresDeCadaMes);
        mappedBarChartData.setListaDeValores(listaDeValores);

        return mappedBarChartData;
    }


}

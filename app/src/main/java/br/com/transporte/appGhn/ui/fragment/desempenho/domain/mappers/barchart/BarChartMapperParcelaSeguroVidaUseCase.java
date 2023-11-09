package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.barchart;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.buscaData.HashMapSimula12MesesExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedBarChartData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.ManipulaBarChartResourceExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BarChartMapperParcelaSeguroVidaUseCase extends ManipulaBarChartResourceExt {
    private final List<Parcela_seguroVida> dataSetResource;

    public BarChartMapperParcelaSeguroVidaUseCase(@NonNull ResourceData resource) {
        this.dataSetResource = resource.getDataSetDespesaSeguroVida();
    }

    //----------------------------------------------------------------------------------------------

    public MappedBarChartData mapeiaListaParaSerExibidaNoBarChart() {
        final HashMap<Integer, BigDecimal> hashMap =
                HashMapSimula12MesesExt.criaHashComRangeDe0a11();

        usaHashMapCom12PosicoesParaSepararOsValoresMensalmente(hashMap);

        final List<BigDecimal> listaComValoresDeCadaMes =
                adicionaValoresDoHashEmUmaListaMapeada(hashMap);

        return  mapeiaBarChartData(listaComValoresDeCadaMes);
    }

    private void usaHashMapCom12PosicoesParaSepararOsValoresMensalmente(@NonNull final HashMap<Integer, BigDecimal> hashMap) {
        for (Parcela_seguroVida p : dataSetResource) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(p.getData().getMonthValue());
            final BigDecimal valor = p.getValor();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).add(valor);
            hashMap.replace(keyMes, resultado);
        }
    }

}

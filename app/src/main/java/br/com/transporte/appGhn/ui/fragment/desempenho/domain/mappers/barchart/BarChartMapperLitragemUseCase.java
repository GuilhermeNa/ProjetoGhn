package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.barchart;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.buscaData.HashMapSimula12MesesExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedBarChartData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.ManipulaBarChartResourceExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BarChartMapperLitragemUseCase extends ManipulaBarChartResourceExt {
    private final List<CustosDeAbastecimento> dataSetResource;

    public BarChartMapperLitragemUseCase(@NonNull ResourceData resource) {
        this.dataSetResource = resource.getDataSetAbastecimento();
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
        for (CustosDeAbastecimento c : dataSetResource) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(c.getData().getMonthValue());
            final BigDecimal valor = c.getQuantidadeLitros();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).add(valor);
            hashMap.replace(keyMes, resultado);
        }
    }

}

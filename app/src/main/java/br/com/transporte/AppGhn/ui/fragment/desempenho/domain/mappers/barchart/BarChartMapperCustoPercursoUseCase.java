package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.HashMapSimula12MesesExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedBarChartData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.ManipulaBarChartResourceExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class BarChartMapperCustoPercursoUseCase extends ManipulaBarChartResourceExt {
    private final List<CustosDePercurso> dataSetResource;

    public BarChartMapperCustoPercursoUseCase(@NonNull ResourceData resource) {
        this.dataSetResource = resource.getDataSetCustoPercurso();
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
        for (CustosDePercurso c : dataSetResource) {
            final int keyMes = getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(c.getData().getMonthValue());
            final BigDecimal valor = c.getValorCusto();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).add(valor);
            hashMap.replace(keyMes, resultado);
        }
    }

}
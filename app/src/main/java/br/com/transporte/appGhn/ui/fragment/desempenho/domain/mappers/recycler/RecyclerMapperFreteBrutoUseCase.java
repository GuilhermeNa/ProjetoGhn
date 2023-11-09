package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.appGhn.util.CalculoUtil;

public class RecyclerMapperFreteBrutoUseCase {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperFreteBrutoUseCase(
            final ResourceData resource,
            final DataRequest request
    ) {
        this.resourceData = resource;
        this.dataRequest = request;
    }

    //----------------------------------------------------------------------------------------------

    public List<MappedRecylerData> mapeiaCavaloDesempenhoParaExibirNaRecycler(final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final boolean solicitouBuscaMensal =
                VerificaRequisicaoExt.seBuscaMesEspecifico(dataRequest);

        if(solicitouBuscaMensal){
             return mapeiaQuantoTemBuscaMensalDefinida(listaCavaloDesempenhoMapper);
        } else {
            return mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(listaCavaloDesempenhoMapper);
        }

    }

    private List<MappedRecylerData> mapeiaQuantoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final List<Frete> dataSetDoMesSolicitado =
                FiltraFrete.listaDoMesSolicitado(resourceData.getDataSetFrete(), dataRequest.getMes());

        final BigDecimal valorTotal =
                CalculoUtil.somaFreteBruto(dataSetDoMesSolicitado);

        BigDecimal valorPorCavalo;
        List<Frete> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraFrete.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaFreteBruto(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    private List<MappedRecylerData> mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final BigDecimal valorTotal =
                CalculoUtil.somaFreteBruto(resourceData.getDataSetFrete());

        BigDecimal valorPorCavalo;
        List<Frete> dataSet;
        for(MappedRecylerData c: listaCavaloDesempenhoMapper){
            dataSet =
                    FiltraFrete.listaPorCavaloId(resourceData.getDataSetFrete(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaFreteBruto(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

}

package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.appGhn.util.CalculoUtil;

public class RecyclerMapperLitragemUseCase {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperLitragemUseCase(
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
            return mapeiaQuandoTemBuscaMensalDefinida(listaCavaloDesempenhoMapper);
        } else {
            return mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(listaCavaloDesempenhoMapper);
        }

    }

    private List<MappedRecylerData> mapeiaQuandoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final List<CustosDeAbastecimento> dataSetDoMesSolicitado =
                FiltraCustosAbastecimento.listaDoMesSolicitado(resourceData.getDataSetAbastecimento(), dataRequest.getMes());

        final BigDecimal valorTotal =
                CalculoUtil.somaLitragemTotal(dataSetDoMesSolicitado);

        BigDecimal valorPorCavalo;
        List<CustosDeAbastecimento> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosAbastecimento.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaLitragemTotal(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    private List<MappedRecylerData> mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final BigDecimal valorTotal =
                CalculoUtil.somaLitragemTotal(resourceData.getDataSetAbastecimento());

        BigDecimal valorPorCavalo;
        List<CustosDeAbastecimento> dataSet;
        for(MappedRecylerData c: listaCavaloDesempenhoMapper){
            dataSet =
                    FiltraCustosAbastecimento.listaPorCavaloId(resourceData.getDataSetAbastecimento(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaLitragemTotal(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

}

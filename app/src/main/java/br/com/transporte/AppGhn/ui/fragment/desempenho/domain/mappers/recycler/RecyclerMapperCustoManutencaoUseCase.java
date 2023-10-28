package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.recycler;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedRecylerData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class RecyclerMapperCustoManutencaoUseCase {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperCustoManutencaoUseCase(
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
        final List<CustosDeManutencao> dataSetDoMesSolicitado =
                FiltraCustosManutencao.listaDoMesSolicitado(resourceData.getDataSetCustoManutencao(), dataRequest.getMes());

        final BigDecimal valorTotal =
                CalculoUtil.somaCustosDeManutencao(dataSetDoMesSolicitado);

        BigDecimal valorPorCavalo;
        List<CustosDeManutencao> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosManutencao.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDeManutencao(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    private List<MappedRecylerData> mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final BigDecimal valorTotal =
                CalculoUtil.somaCustosDeManutencao(resourceData.getDataSetCustoManutencao());

        BigDecimal valorPorCavalo;
        List<CustosDeManutencao> dataSet;
        for(MappedRecylerData c: listaCavaloDesempenhoMapper){
            dataSet =
                    FiltraCustosManutencao.listaPorCavaloId(resourceData.getDataSetCustoManutencao(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDeManutencao(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

}

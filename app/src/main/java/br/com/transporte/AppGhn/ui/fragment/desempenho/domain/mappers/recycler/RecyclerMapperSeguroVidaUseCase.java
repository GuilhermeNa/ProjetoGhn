package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.recycler;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.filtros.FiltraParcelaSeguroVida;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedRecylerData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.ManipulaCavaloDesempenhoExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class RecyclerMapperSeguroVidaUseCase extends ManipulaCavaloDesempenhoExt {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperSeguroVidaUseCase(
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

    @NonNull
    @Contract("_ -> param1")
    private List<MappedRecylerData> mapeiaQuandoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final List<Parcela_seguroVida> dataSetDoMesSolicitado =
                FiltraParcelaSeguroVida.listaDoMesSolicitado(resourceData.getDataSetDespesaSeguroVida(), dataRequest.getMes());

        final List<Parcela_seguroVida> dataSet =
                FiltraParcelaSeguroVida.listaPorCavaloId(dataSetDoMesSolicitado, 0L);

        final BigDecimal valorTotal =
                CalculoUtil.somaParcelas_seguroVida(dataSet);

        final BigDecimal valorPorCavalo =
                getRateio(valorTotal, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    @NonNull
    @Contract("_ -> param1")
    private List<MappedRecylerData> mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final BigDecimal valorTotal =
                CalculoUtil.somaParcelas_seguroVida(resourceData.getDataSetDespesaSeguroVida());

        final BigDecimal valorPorCavalo =
                getRateio(valorTotal, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        for(MappedRecylerData c: listaCavaloDesempenhoMapper){
            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
        }

        return listaCavaloDesempenhoMapper;
    }

}

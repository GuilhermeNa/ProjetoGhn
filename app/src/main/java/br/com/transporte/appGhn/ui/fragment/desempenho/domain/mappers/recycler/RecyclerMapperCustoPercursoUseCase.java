package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.appGhn.util.CalculoUtil;

public class RecyclerMapperCustoPercursoUseCase {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperCustoPercursoUseCase(
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
        final List<CustosDePercurso> dataSetDoMesSolicitado =
                FiltraCustosPercurso.listaDoMesSolicitado(resourceData.getDataSetCustoPercurso(), dataRequest.getMes());

        final BigDecimal valorTotal =
                CalculoUtil.somaCustosDePercurso(dataSetDoMesSolicitado);

        BigDecimal valorPorCavalo;
        List<CustosDePercurso> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosPercurso.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDePercurso(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    private List<MappedRecylerData> mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        final BigDecimal valorTotal =
                CalculoUtil.somaCustosDePercurso(resourceData.getDataSetCustoPercurso());

        BigDecimal valorPorCavalo;
        List<CustosDePercurso> dataSet;
        for(MappedRecylerData c: listaCavaloDesempenhoMapper){
            dataSet =
                    FiltraCustosPercurso.listaPorCavaloId(resourceData.getDataSetCustoPercurso(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDePercurso(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

}

package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.ManipulaCavaloDesempenhoExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.appGhn.util.CalculoUtil;

public class RecyclerMapperDespesaCertificadoUseCase extends ManipulaCavaloDesempenhoExt {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperDespesaCertificadoUseCase(
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
        final boolean solicitaRateio =
                VerificaRequisicaoExt.seSolicitaRateioDeDespesas(dataRequest);

        if (solicitouBuscaMensal && solicitaRateio){
            return mapeiaQuantoTemBuscaMensalComRateioDeDespesasIndiretas(listaCavaloDesempenhoMapper);

        } else if(solicitouBuscaMensal){
            return mapeiaQuandoTemBuscaMensalSemRateioDeDespesasIndiretas(listaCavaloDesempenhoMapper);

        } else if (solicitaRateio){
            return mapeiaTodoOAnoComRateioDeDespesasIndiretas(listaCavaloDesempenhoMapper);

        } else {
            return mapeiaTodoOAnoSemRateioDeDespesasIndiretas(listaCavaloDesempenhoMapper);

        }

    }

    @NonNull
    @Contract("_ -> param1")
    private List<MappedRecylerData> mapeiaQuantoTemBuscaMensalComRateioDeDespesasIndiretas(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<DespesaCertificado> dataSetDoMesSolicitado =
                FiltraDespesasCertificado.listaDoMesSolicitado(resourceData.getDataSetDespesaCertificado(), dataRequest.getMes());

        final List<DespesaCertificado> dataSetDespesasDiretas =
                FiltraDespesasCertificado.listaPorTipoDespesa(dataSetDoMesSolicitado, DIRETA);

        final List<DespesaCertificado> dataSetDespesasIndiretas =
                FiltraDespesasCertificado.listaPorTipoDespesa(dataSetDoMesSolicitado, INDIRETA);

        final BigDecimal valorAcumuladoParaRateio =
                CalculoUtil.somaDespesasCertificado(dataSetDespesasIndiretas);

        final BigDecimal valorRateio =
                getRateio(valorAcumuladoParaRateio, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasCertificado(dataSetDoMesSolicitado);

        BigDecimal valorPorCavalo;
        List<DespesaCertificado> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasCertificado.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasCertificado(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.adicionaValor(valorRateio);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    @NonNull
    @Contract("_ -> param1")
    private List<MappedRecylerData> mapeiaQuandoTemBuscaMensalSemRateioDeDespesasIndiretas(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<DespesaCertificado> dataSetDoMesSolicitado =
                FiltraDespesasCertificado.listaDoMesSolicitado(resourceData.getDataSetDespesaCertificado(), dataRequest.getMes());

        final List<DespesaCertificado> dataSetDespesasDiretas =
                FiltraDespesasCertificado.listaPorTipoDespesa(dataSetDoMesSolicitado, DIRETA);

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasCertificado(dataSetDespesasDiretas);

        BigDecimal valorPorCavalo;
        List<DespesaCertificado> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasCertificado.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasCertificado(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    @NonNull
    @Contract("_ -> param1")
    private List<MappedRecylerData> mapeiaTodoOAnoComRateioDeDespesasIndiretas(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<DespesaCertificado> dataSetDespesasDiretas =
                FiltraDespesasCertificado.listaPorTipoDespesa(resourceData.getDataSetDespesaCertificado(), DIRETA);

        final List<DespesaCertificado> dataSetDespesasIndiretas =
                FiltraDespesasCertificado.listaPorTipoDespesa(resourceData.getDataSetDespesaCertificado(), INDIRETA);

        final BigDecimal valorAcumuladoParaRateio =
                CalculoUtil.somaDespesasCertificado(dataSetDespesasIndiretas);

        final BigDecimal valorRateio =
                getRateio(valorAcumuladoParaRateio, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasCertificado(resourceData.getDataSetDespesaCertificado());

        BigDecimal valorPorCavalo;
        List<DespesaCertificado> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasCertificado.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasCertificado(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.adicionaValor(valorRateio);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

    @NonNull
    @Contract("_ -> param1")
    private List<MappedRecylerData> mapeiaTodoOAnoSemRateioDeDespesasIndiretas(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<DespesaCertificado> dataSetDespesasDiretas =
                FiltraDespesasCertificado.listaPorTipoDespesa(resourceData.getDataSetDespesaCertificado(), DIRETA);

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasCertificado(dataSetDespesasDiretas);

        BigDecimal valorPorCavalo;
        List<DespesaCertificado> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasCertificado.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasCertificado(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

}

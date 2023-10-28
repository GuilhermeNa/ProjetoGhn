package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.recycler;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedRecylerData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.ManipulaCavaloDesempenhoExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class RecyclerMapperDespesaImpostoUseCase extends ManipulaCavaloDesempenhoExt {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperDespesaImpostoUseCase(
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
        final List<DespesasDeImposto> dataSetDoMesSolicitado =
                FiltraDespesasImposto.listaPorMes(resourceData.getDataSetDespesaImposto(), dataRequest.getMes());

        final List<DespesasDeImposto> dataSetDespesasDiretas =
                FiltraDespesasImposto.listaPorTipo(dataSetDoMesSolicitado, DIRETA);

        final List<DespesasDeImposto> dataSetDespesasIndiretas =
                FiltraDespesasImposto.listaPorTipo(dataSetDoMesSolicitado, INDIRETA);

        final BigDecimal valorAcumuladoParaRateio =
                CalculoUtil.somaDespesaImposto(dataSetDespesasIndiretas);

        final BigDecimal valorRateio =
                getRateio(Objects.requireNonNull(valorAcumuladoParaRateio), BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesaImposto(dataSetDoMesSolicitado);

        BigDecimal valorPorCavalo;
        List<DespesasDeImposto> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasImposto.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesaImposto(dataSet);

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
        final List<DespesasDeImposto> dataSetDoMesSolicitado =
                FiltraDespesasImposto.listaPorMes(resourceData.getDataSetDespesaImposto(), dataRequest.getMes());

        final List<DespesasDeImposto> dataSetDespesasDiretas =
                FiltraDespesasImposto.listaPorTipo(dataSetDoMesSolicitado, DIRETA);

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesaImposto(dataSetDespesasDiretas);

        BigDecimal valorPorCavalo;
        List<DespesasDeImposto> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasImposto.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesaImposto(dataSet);

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
        final List<DespesasDeImposto> dataSetDespesasDiretas =
                FiltraDespesasImposto.listaPorTipo(resourceData.getDataSetDespesaImposto(), DIRETA);

        final List<DespesasDeImposto> dataSetDespesasIndiretas =
                FiltraDespesasImposto.listaPorTipo(resourceData.getDataSetDespesaImposto(), INDIRETA);

        final BigDecimal valorAcumuladoParaRateio =
                CalculoUtil.somaDespesaImposto(dataSetDespesasIndiretas);

        final BigDecimal valorRateio =
                getRateio(Objects.requireNonNull(valorAcumuladoParaRateio), BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesaImposto(resourceData.getDataSetDespesaImposto());

        BigDecimal valorPorCavalo;
        List<DespesasDeImposto> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasImposto.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesaImposto(dataSet);

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
        final List<DespesasDeImposto> dataSetDespesasDiretas =
                FiltraDespesasImposto.listaPorTipo(resourceData.getDataSetDespesaImposto(), DIRETA);

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesaImposto(dataSetDespesasDiretas);

        BigDecimal valorPorCavalo;
        List<DespesasDeImposto> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasImposto.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesaImposto(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

}

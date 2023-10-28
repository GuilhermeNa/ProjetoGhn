package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.recycler;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedRecylerData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.ManipulaCavaloDesempenhoExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class RecyclerMapperDespesaAdmUseCase extends ManipulaCavaloDesempenhoExt {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;

    public RecyclerMapperDespesaAdmUseCase(
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
        final List<DespesaAdm> dataSetDoMesSolicitado =
                FiltraDespesasAdm.listaPorMes(resourceData.getDataSetDespesaAdm(), dataRequest.getMes());

        final List<DespesaAdm> dataSetDespesasDiretas =
                FiltraDespesasAdm.listaPorTipo(dataSetDoMesSolicitado, DIRETA);

        final List<DespesaAdm> dataSetDespesasIndiretas =
                FiltraDespesasAdm.listaPorTipo(dataSetDoMesSolicitado, INDIRETA);

        final BigDecimal valorAcumuladoParaRateio =
                CalculoUtil.somaDespesasAdm(dataSetDespesasIndiretas);

        final BigDecimal valorRateio =
                getRateio(valorAcumuladoParaRateio, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasAdm(dataSetDoMesSolicitado);

        BigDecimal valorPorCavalo;
        List<DespesaAdm> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasAdm.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasAdm(dataSet);

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
        final List<DespesaAdm> dataSetDoMesSolicitado =
                FiltraDespesasAdm.listaPorMes(resourceData.getDataSetDespesaAdm(), dataRequest.getMes());

        final List<DespesaAdm> dataSetDespesasDiretas =
                FiltraDespesasAdm.listaPorTipo(dataSetDoMesSolicitado, DIRETA);

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasAdm(dataSetDespesasDiretas);

        BigDecimal valorPorCavalo;
        List<DespesaAdm> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasAdm.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasAdm(dataSet);

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
        final List<DespesaAdm> dataSetDespesasDiretas =
                FiltraDespesasAdm.listaPorTipo(resourceData.getDataSetDespesaAdm(), DIRETA);

        final List<DespesaAdm> dataSetDespesasIndiretas =
                FiltraDespesasAdm.listaPorTipo(resourceData.getDataSetDespesaAdm(), INDIRETA);

        final BigDecimal valorAcumuladoParaRateio =
                CalculoUtil.somaDespesasAdm(dataSetDespesasIndiretas);

        final BigDecimal valorRateio =
                getRateio(valorAcumuladoParaRateio, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasAdm(resourceData.getDataSetDespesaAdm());

        BigDecimal valorPorCavalo;
        List<DespesaAdm> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasAdm.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasAdm(dataSet);

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
        final List<DespesaAdm> dataSetDespesasDiretas =
                FiltraDespesasAdm.listaPorTipo(resourceData.getDataSetDespesaAdm(), DIRETA);

        final BigDecimal valorTotal =
                CalculoUtil.somaDespesasAdm(dataSetDespesasDiretas);

        BigDecimal valorPorCavalo;
        List<DespesaAdm> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasAdm.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasAdm(dataSet);

            c.adicionaValor(valorPorCavalo);
            c.definePercentual(valorTotal);
            dataSet.clear();
        }

        return listaCavaloDesempenhoMapper;
    }

}

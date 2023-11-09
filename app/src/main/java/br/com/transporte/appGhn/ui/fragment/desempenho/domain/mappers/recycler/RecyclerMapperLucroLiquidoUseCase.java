package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.recycler;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import br.com.transporte.appGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.appGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.appGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.appGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.filtros.FiltraParcelaSeguroFrota;
import br.com.transporte.appGhn.filtros.FiltraParcelaSeguroVida;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.ManipulaCavaloDesempenhoExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.VerificaRequisicaoExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;
import br.com.transporte.appGhn.util.CalculoUtil;

public class RecyclerMapperLucroLiquidoUseCase extends ManipulaCavaloDesempenhoExt {
    private final ResourceData resourceData;
    private final DataRequest dataRequest;
    private BigDecimal valorTotal;

    public RecyclerMapperLucroLiquidoUseCase(
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

        if (solicitouBuscaMensal) {
            return mapeiaQuandoTemBuscaMensalDefinida(listaCavaloDesempenhoMapper);
        } else {
            return mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(listaCavaloDesempenhoMapper);
        }

    }

    //----------------------------------------------------------------------------------------------

    private List<MappedRecylerData> mapeiaQuandoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        valorTotal = BigDecimal.ZERO;

        insereFreteLiquidoMensal(listaCavaloDesempenhoMapper);
        insereComissaoMensal(listaCavaloDesempenhoMapper);
        insereCustoAbastecimentoMensal(listaCavaloDesempenhoMapper);
        insereCustoPercursoMensal(listaCavaloDesempenhoMapper);
        insereCustoManutencaoMensal(listaCavaloDesempenhoMapper);
        insereDespesaCertificadoMensal(listaCavaloDesempenhoMapper);
        insereDespesaImpostoMensal(listaCavaloDesempenhoMapper);
        insereDespesaAdmMensal(listaCavaloDesempenhoMapper);
        insereSeguroFrotaMensal(listaCavaloDesempenhoMapper);
        insereSeguroVidaMensal(listaCavaloDesempenhoMapper);

        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            c.definePercentual(valorTotal);
        }

        return listaCavaloDesempenhoMapper;
    }

    private void insereSeguroVidaMensal(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<Parcela_seguroVida> dataSetDoMesSolicitado =
                FiltraParcelaSeguroVida.listaDoMesSolicitado(resourceData.getDataSetDespesaSeguroVida(), dataRequest.getMes());

        final BigDecimal valorAcumuladoComSeguroVida =
                CalculoUtil.somaParcelas_seguroVida(dataSetDoMesSolicitado);

        final BigDecimal valorPorCavalo =
                getRateio(valorAcumuladoComSeguroVida, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
        }
    }

    private void insereSeguroFrotaMensal(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<Parcela_seguroFrota> dataSetDoMesSolicitado =
                FiltraParcelaSeguroFrota.listaDoMesSolicitado(resourceData.getDataSetDespesaSeguroFrota(), dataRequest.getMes());

        BigDecimal valorPorCavalo;
        List<Parcela_seguroFrota> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraParcelaSeguroFrota.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaParcelas_seguroFrota(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereDespesaAdmMensal(
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

        BigDecimal valorPorCavalo;
        List<DespesaAdm> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasAdm.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasAdm(dataSet);

            c.subtraiValor(valorPorCavalo);
            c.subtraiValor(valorRateio);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorRateio);
            dataSet.clear();
        }
    }

    private void insereDespesaImpostoMensal(
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

        BigDecimal valorPorCavalo;
        List<DespesasDeImposto> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasImposto.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesaImposto(dataSet);

            c.subtraiValor(valorPorCavalo);
            c.subtraiValor(valorRateio);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorRateio);
            dataSet.clear();
        }
    }

    private void insereDespesaCertificadoMensal(
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

        BigDecimal valorPorCavalo;
        List<DespesaCertificado> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasCertificado.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasCertificado(dataSet);

            c.subtraiValor(valorPorCavalo);
            c.subtraiValor(valorRateio);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorRateio);
            dataSet.clear();
        }

    }

    private void insereCustoManutencaoMensal(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<CustosDeManutencao> dataSetDoMesSolicitado =
                FiltraCustosManutencao.listaDoMesSolicitado(resourceData.getDataSetCustoManutencao(), dataRequest.getMes());

        BigDecimal valorPorCavalo;
        List<CustosDeManutencao> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosManutencao.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDeManutencao(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereCustoPercursoMensal(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<CustosDePercurso> dataSetDoMesSolicitado =
                FiltraCustosPercurso.listaDoMesSolicitado(resourceData.getDataSetCustoPercurso(), dataRequest.getMes());

        BigDecimal valorPorCavalo;
        List<CustosDePercurso> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosPercurso.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDePercurso(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereCustoAbastecimentoMensal(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<CustosDeAbastecimento> dataSetDoMesSolicitado =
                FiltraCustosAbastecimento.listaDoMesSolicitado(resourceData.getDataSetAbastecimento(), dataRequest.getMes());

        BigDecimal valorPorCavalo;
        List<CustosDeAbastecimento> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosAbastecimento.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDeAbastecimento(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereComissaoMensal(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<Frete> dataSetDoMesSolicitado =
                FiltraFrete.listaDoMesSolicitado(resourceData.getDataSetFrete(), dataRequest.getMes());

        BigDecimal valorPorCavalo;
        List<Frete> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraFrete.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaComissao(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereFreteLiquidoMensal(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final List<Frete> dataSetDoMesSolicitado =
                FiltraFrete.listaDoMesSolicitado(resourceData.getDataSetFrete(), dataRequest.getMes());

        BigDecimal valorPorCavalo;
        List<Frete> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraFrete.listaPorCavaloId(dataSetDoMesSolicitado, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaFreteLiquido(dataSet);

            c.adicionaValor(valorPorCavalo);
            valorTotal = valorTotal.add(valorPorCavalo);
            dataSet.clear();
        }
    }

    //----------------------------------------------------------------------------------------------

    private List<MappedRecylerData> mapeiaTodoOAnoQuandoNaoTemBuscaMensalDefinida(@NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper) {
        valorTotal = BigDecimal.ZERO;

        insereFreteLiquidoAnual(listaCavaloDesempenhoMapper);
        insereComissaoAnual(listaCavaloDesempenhoMapper);
        insereCustoAbastecimentoAnual(listaCavaloDesempenhoMapper);
        insereCustoPercursoAnual(listaCavaloDesempenhoMapper);
        insereCustoManutencaoAnual(listaCavaloDesempenhoMapper);
        insereDespesaCertificadoAnual(listaCavaloDesempenhoMapper);
        insereDespesaImpostoAnual(listaCavaloDesempenhoMapper);
        insereDespesaAdmAnual(listaCavaloDesempenhoMapper);
        insereSeguroFrotaAnual(listaCavaloDesempenhoMapper);
        insereSeguroVidaAnual(listaCavaloDesempenhoMapper);

        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            c.definePercentual(valorTotal);
        }

        return listaCavaloDesempenhoMapper;
    }

    private void insereDespesaAdmAnual(
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

        BigDecimal valorPorCavalo;
        List<DespesaAdm> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasAdm.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasAdm(dataSet);

            c.subtraiValor(valorPorCavalo);
            c.subtraiValor(valorRateio);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorRateio);
            dataSet.clear();
        }
    }

    private void insereDespesaImpostoAnual(
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

        BigDecimal valorPorCavalo;
        List<DespesasDeImposto> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasImposto.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesaImposto(dataSet);

            c.subtraiValor(valorPorCavalo);
            c.subtraiValor(valorRateio);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorRateio);
            dataSet.clear();
        }
    }

    private void insereDespesaCertificadoAnual(
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

        BigDecimal valorPorCavalo;
        List<DespesaCertificado> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraDespesasCertificado.listaPorCavaloId(dataSetDespesasDiretas, c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaDespesasCertificado(dataSet);

            c.subtraiValor(valorPorCavalo);
            c.subtraiValor(valorRateio);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorRateio);
            dataSet.clear();
        }

    }

    private void insereSeguroVidaAnual(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        final BigDecimal valorAcumuladoComSeguroVida =
                CalculoUtil.somaParcelas_seguroVida(resourceData.getDataSetDespesaSeguroVida());

        final BigDecimal valorPorCavalo =
                getRateio(valorAcumuladoComSeguroVida, BigDecimal.valueOf(listaCavaloDesempenhoMapper.size()));

        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
        }
    }

    private void insereSeguroFrotaAnual(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        BigDecimal valorPorCavalo;
        List<Parcela_seguroFrota> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraParcelaSeguroFrota.listaPorCavaloId(resourceData.getDataSetDespesaSeguroFrota(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaParcelas_seguroFrota(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereCustoManutencaoAnual(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        BigDecimal valorPorCavalo;
        List<CustosDeManutencao> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosManutencao.listaPorCavaloId(resourceData.getDataSetCustoManutencao(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDeManutencao(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }


    }

    private void insereCustoPercursoAnual(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        BigDecimal valorPorCavalo;
        List<CustosDePercurso> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosPercurso.listaPorCavaloId(resourceData.getDataSetCustoPercurso(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDePercurso(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereCustoAbastecimentoAnual(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        BigDecimal valorPorCavalo;
        List<CustosDeAbastecimento> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraCustosAbastecimento.listaPorCavaloId(resourceData.getDataSetAbastecimento(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaCustosDeAbastecimento(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereComissaoAnual(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        BigDecimal valorPorCavalo;
        List<Frete> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraFrete.listaPorCavaloId(resourceData.getDataSetFrete(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaComissao(dataSet);

            c.subtraiValor(valorPorCavalo);
            valorTotal = valorTotal.subtract(valorPorCavalo);
            dataSet.clear();
        }
    }

    private void insereFreteLiquidoAnual(
            @NonNull final List<MappedRecylerData> listaCavaloDesempenhoMapper
    ) {
        BigDecimal valorPorCavalo;
        List<Frete> dataSet;
        for (MappedRecylerData c : listaCavaloDesempenhoMapper) {
            dataSet =
                    FiltraFrete.listaPorCavaloId(resourceData.getDataSetFrete(), c.getCavaloId());
            valorPorCavalo = CalculoUtil.somaFreteLiquido(dataSet);

            c.adicionaValor(valorPorCavalo);
            valorTotal = valorTotal.add(valorPorCavalo);
            dataSet.clear();
        }
    }

}

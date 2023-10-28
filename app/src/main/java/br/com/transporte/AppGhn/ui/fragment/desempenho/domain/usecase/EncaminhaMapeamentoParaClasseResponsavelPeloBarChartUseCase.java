package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.usecase;

import androidx.annotation.NonNull;

import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperComissaoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperCustoAbastecimentoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperCustoManutencaoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperCustoPercursoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperDespesaAdmUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperDespesaCertificadoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperDespesaImpostoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperFreteBrutoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperFreteLiquidoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperLitragemUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperLucroLiquidoUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperParcelaSeguroFrotaUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart.BarChartMapperParcelaSeguroVidaUseCase;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedBarChartData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class EncaminhaMapeamentoParaClasseResponsavelPeloBarChartUseCase {
    private final ResourceData resourceData;

    public EncaminhaMapeamentoParaClasseResponsavelPeloBarChartUseCase(ResourceData resourceData) {
        this.resourceData = resourceData;
    }

    //----------------------------------------------------------------------------------------------

    public MappedBarChartData run(@NonNull final DataRequest dataRequest) {
        switch (dataRequest.getTipo()) {
            case FRETE_BRUTO:
                return mapeiaFreteBruto();

            case FRETE_LIQUIDO:
                return mapeiaFreteLiquido();

            case COMISSAO:
                return mapeiaCustoComissao();

            case CUSTOS_ABASTECIMENTO:
                return mapeiaCustoAbastecimento();

            case LITRAGEM:
                return mapeiaLitragem();

            case CUSTOS_PERCURSO:
                return mapeiaCustoPercurso();

            case CUSTOS_MANUTENCAO:
                return mapeiaCustoManutencao();

            case LUCRO_LIQUIDO:
                return mapeiaLucroLiquido();

            case DESPESA_CERTIFICADOS:
                return mapeiaDespesaCertificado();

            case DESPESAS_IMPOSTOS:
                return mapeiaDespesaImposto();

            case DESPESAS_ADM:
                return mapeiaDespesaAdm();

            case DESPESA_SEGURO_FROTA:
                return mapeiaDespesaSeguroFrota();

            case DESPESA_SEGURO_VIDA:
                return mapeiaDespesaSeguroVida();
        }
        return null;
    }

    private MappedBarChartData mapeiaFreteBruto() {
        final BarChartMapperFreteBrutoUseCase mapper =
                new BarChartMapperFreteBrutoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaFreteLiquido() {
        final BarChartMapperFreteLiquidoUseCase mapper =
                new BarChartMapperFreteLiquidoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaCustoComissao() {
      final BarChartMapperComissaoUseCase mapper =
              new BarChartMapperComissaoUseCase(resourceData);
      return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaCustoAbastecimento() {
        final BarChartMapperCustoAbastecimentoUseCase mapper =
                new BarChartMapperCustoAbastecimentoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaLitragem() {
        final BarChartMapperLitragemUseCase mapper =
                new BarChartMapperLitragemUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaCustoPercurso() {
        final BarChartMapperCustoPercursoUseCase mapper =
                new BarChartMapperCustoPercursoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaCustoManutencao() {
        final BarChartMapperCustoManutencaoUseCase mapper =
                new BarChartMapperCustoManutencaoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaLucroLiquido() {
        final BarChartMapperLucroLiquidoUseCase mapper =
                new BarChartMapperLucroLiquidoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaDespesaCertificado() {
        final BarChartMapperDespesaCertificadoUseCase mapper =
                new BarChartMapperDespesaCertificadoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaDespesaImposto() {
        final BarChartMapperDespesaImpostoUseCase mapper =
                new BarChartMapperDespesaImpostoUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaDespesaAdm() {
        final BarChartMapperDespesaAdmUseCase mapper =
                new BarChartMapperDespesaAdmUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaDespesaSeguroFrota() {
        final BarChartMapperParcelaSeguroFrotaUseCase mapper =
                new BarChartMapperParcelaSeguroFrotaUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

    private MappedBarChartData mapeiaDespesaSeguroVida() {
        final BarChartMapperParcelaSeguroVidaUseCase mapper =
                new BarChartMapperParcelaSeguroVidaUseCase(resourceData);
        return mapper.mapeiaListaParaSerExibidaNoBarChart();
    }

}

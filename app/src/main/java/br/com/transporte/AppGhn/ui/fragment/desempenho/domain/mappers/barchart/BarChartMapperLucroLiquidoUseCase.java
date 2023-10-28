package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.barchart;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData.HashMapSimula12MesesExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedBarChartData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.ManipulaBarChartResourceExt;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class BarChartMapperLucroLiquidoUseCase extends ManipulaBarChartResourceExt {
    private final ResourceData dataResource;

    public BarChartMapperLucroLiquidoUseCase(@NonNull ResourceData dataResource) {
        this.dataResource = dataResource;
    }

    //----------------------------------------------------------------------------------------------

    public MappedBarChartData mapeiaListaParaSerExibidaNoBarChart() {
        final HashMap<Integer, BigDecimal> hashMap =
                HashMapSimula12MesesExt.criaHashComRangeDe0a11();

        usaHashMapCom12PosicoesParaSepararOsValoresMensalmente(hashMap);

        final List<BigDecimal> listaComValoresDeCadaMes =
                adicionaValoresDoHashEmUmaListaMapeada(hashMap);

        return mapeiaBarChartData(listaComValoresDeCadaMes);
    }

    private void usaHashMapCom12PosicoesParaSepararOsValoresMensalmente(@NonNull final HashMap<Integer, BigDecimal> hashMap) {
        insereFreteLiquidoEComissao(hashMap);
        insereCustoAbastecimento(hashMap);
        insereCustoPercurso(hashMap);
        insereCustoManutencao(hashMap);
        insereDespesaCertificado(hashMap);
        insereDespesaImposto(hashMap);
        insereDespesaAdm(hashMap);
        insereSeguroFrota(hashMap);
        insereSeguroVida(hashMap);
    }

    private void insereSeguroVida(final HashMap<Integer, BigDecimal> hashMap) {
        for (Parcela_seguroVida p : dataResource.getDataSetDespesaSeguroVida()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(p.getData().getMonthValue());

            final BigDecimal valor = p.getValor();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

    private void insereSeguroFrota(final HashMap<Integer, BigDecimal> hashMap) {
        for (Parcela_seguroFrota p : dataResource.getDataSetDespesaSeguroFrota()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(p.getData().getMonthValue());

            final BigDecimal valor = p.getValor();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

    private void insereDespesaAdm(final HashMap<Integer, BigDecimal> hashMap) {
        for (DespesaAdm d : dataResource.getDataSetDespesaAdm()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(d.getData().getMonthValue());

            final BigDecimal valor = d.getValorDespesa();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

    private void insereDespesaImposto(final HashMap<Integer, BigDecimal> hashMap) {
        for (DespesasDeImposto d : dataResource.getDataSetDespesaImposto()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(d.getData().getMonthValue());

            final BigDecimal valor = d.getValorDespesa();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

    private void insereDespesaCertificado(final HashMap<Integer, BigDecimal> hashMap) {
        for (DespesaCertificado d : dataResource.getDataSetDespesaCertificado()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(d.getData().getMonthValue());

            final BigDecimal valor = d.getValorDespesa();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

    private void insereCustoManutencao(final HashMap<Integer, BigDecimal> hashMap) {
        for (CustosDeManutencao c : dataResource.getDataSetCustoManutencao()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(c.getData().getMonthValue());

            final BigDecimal valor = c.getValorCusto();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

    private void insereCustoPercurso(final HashMap<Integer, BigDecimal> hashMap) {
        for (CustosDePercurso c : dataResource.getDataSetCustoPercurso()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(c.getData().getMonthValue());

            final BigDecimal valor = c.getValorCusto();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

    private void insereFreteLiquidoEComissao(final HashMap<Integer, BigDecimal> hashMap) {
        for (Frete f : dataResource.getDataSetFrete()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(f.getData().getMonthValue());

            final BigDecimal valorDoFreteLiquido = f.getFreteLiquidoAReceber();
            final BigDecimal resultadoParcial =
                    Objects.requireNonNull(hashMap.get(keyMes)).add(valorDoFreteLiquido);

            final BigDecimal valorDaComissao = f.getComissaoAoMotorista();
            final BigDecimal resultadoFinal = resultadoParcial.subtract(valorDaComissao);

            hashMap.replace(keyMes, resultadoFinal);
        }
    }

    private void insereCustoAbastecimento(final HashMap<Integer, BigDecimal> hashMap) {
        for (CustosDeAbastecimento c : dataResource.getDataSetAbastecimento()) {
            final int keyMes =
                    getKeyMesAjustadaComMenosUmParaTerCompatibilidadeComHash(c.getData().getMonthValue());

            final BigDecimal valor = c.getValorCusto();
            final BigDecimal resultado =
                    Objects.requireNonNull(hashMap.get(keyMes)).subtract(valor);

            hashMap.replace(keyMes, resultado);
        }
    }

}

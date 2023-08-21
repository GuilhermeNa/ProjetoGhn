package br.com.transporte.AppGhn.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;

public class CalculoUtil {

    public static BigDecimal somaFreteBruto(@NonNull List<Frete> dataSet) {
        return dataSet.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getFreteBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaFreteLiquido(@NonNull List<Frete> dataSet) {
        return dataSet.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getFreteLiquidoAReceber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaCustosDePercurso(@NonNull List<CustosDePercurso> dataSet) {
        return dataSet.stream()
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaComissao(@NonNull List<Frete> dataSet) {
        return dataSet.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaCustosDeManutencao(@NonNull List<CustosDeManutencao> dataSet) {
        return dataSet.stream()
                .map(CustosDeManutencao::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaCustosDeAbastecimento(@NonNull List<CustosDeAbastecimento> dataSet) {
        return dataSet.stream()
                .map(CustosDeAbastecimento::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaDespesasAdm(@NonNull List<DespesaAdm> dataSet) {
        return dataSet.stream()
                .map(DespesaAdm::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaDespesasCertificado(@NonNull List<DespesaCertificado> dataSet) {
        return dataSet.stream()
                .map(DespesaCertificado::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaParcelasSeguro(@NonNull List<ParcelaDeSeguro> dataSet) {
        return dataSet.stream()
                .map(ParcelaDeSeguro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Nullable
    public static BigDecimal somaDespesaImposto(@NonNull List<DespesasDeImposto> dataSet) {
        return dataSet.stream()
                .map(DespesasDeImposto::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

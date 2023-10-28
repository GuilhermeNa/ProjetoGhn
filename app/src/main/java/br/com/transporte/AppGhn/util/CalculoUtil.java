package br.com.transporte.AppGhn.util;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.DEBUG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;

public class CalculoUtil {

    //--------------------------------------------------------------------------------------------//
    //                                          Frete                                             //
    //--------------------------------------------------------------------------------------------//

    public static BigDecimal somaFreteBruto(@NonNull List<Frete> dataSet) {
        return dataSet.stream()
                .map(Frete::getFreteBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaFreteLiquido(@NonNull List<Frete> dataSet) {
        return dataSet.stream()
                .map(Frete::getFreteLiquidoAReceber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaComissao(@NonNull List<Frete> dataSet) {
        return dataSet.stream()
                .map(Frete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaComissaoPorStatus(List<Frete> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(Frete::isComissaoJaFoiPaga)
                .map(Frete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return dataSet.stream()
                .filter(f -> !f.isComissaoJaFoiPaga())
                .map(Frete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaDescontoNoFrete(@NonNull List<Frete> dataSet) {
        return dataSet.stream()
                .map(Frete::getDescontos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //--------------------------------------------------------------------------------------------//
    //                                          Custos                                            //
    //--------------------------------------------------------------------------------------------//

    public static BigDecimal somaCustosDePercurso(@NonNull List<CustosDePercurso> dataSet) {
        return dataSet.stream()
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaCustosDeManutencao(List<CustosDeManutencao> dataSet) {
        if (dataSet != null) {
            return dataSet.stream()
                    .map(CustosDeManutencao::getValorCusto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            Log.d(DEBUG, "CalculoUtil : somaCustosDeManutencao -> dataSet recebido é nulo");
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal somaCustosDeAbastecimento(@NonNull List<CustosDeAbastecimento> dataSet) {
        return dataSet.stream()
                .map(CustosDeAbastecimento::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaLitragemTotal(@NonNull List<CustosDeAbastecimento> dataSet) {
        return dataSet.stream()
                .map(CustosDeAbastecimento::getQuantidadeLitros)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //--------------------------------------------------------------------------------------------//
    //                                          Despesas                                          //
    //--------------------------------------------------------------------------------------------//

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

    public static BigDecimal somaParcelas_seguroFrota(@NonNull List<Parcela_seguroFrota> dataSet) {
        return dataSet.stream()
                .map(Parcela_seguroFrota::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaParcelas_seguroVida(@NonNull List<Parcela_seguroVida> dataSet) {
        return dataSet.stream()
                .map(Parcela_seguroVida::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Nullable
    public static BigDecimal somaDespesaImposto(@NonNull List<DespesasDeImposto> dataSet) {
        return dataSet.stream()
                .map(DespesasDeImposto::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //--------------------------------------------------------------------------------------------//
    //                                          Adiantamento                                      //
    //--------------------------------------------------------------------------------------------//

    @Nullable
    public static BigDecimal somaAdiantamentoPorStatus(@NonNull List<Adiantamento> dataSet, boolean isDescontado) {
        if (isDescontado) return dataSet.stream()
                .filter(Adiantamento::isAdiantamentoJaFoiDescontado)
                .map(Adiantamento::restaReembolsar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return dataSet.stream()
                .filter(a -> !a.isAdiantamentoJaFoiDescontado())
                .map(Adiantamento::restaReembolsar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal somaAdiantamentoPorUltimoValorAbatido(@NonNull List<Adiantamento> dataSet) {
        return dataSet.stream()
                .map(Adiantamento::getUltimoValorAbatido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    //--------------------------------------------------------------------------------------------//
    //                                     Recebimento Frete                                      //
    //--------------------------------------------------------------------------------------------//

    public static BigDecimal somaValorTotalRecebido(@NonNull List<RecebimentoDeFrete> dataSet) {
        return dataSet.stream()
                .map(RecebimentoDeFrete::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}

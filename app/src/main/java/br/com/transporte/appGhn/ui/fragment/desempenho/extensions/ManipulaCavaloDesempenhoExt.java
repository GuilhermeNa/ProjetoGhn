package br.com.transporte.appGhn.ui.fragment.desempenho.extensions;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;

public class ManipulaCavaloDesempenhoExt {

    protected BigDecimal getRateio(
            @NonNull final BigDecimal valor,
            final BigDecimal quantidadeCavalos
            ) {

        return valor.divide(quantidadeCavalos, 2, RoundingMode.HALF_EVEN);
    }

    protected void adicionaValorAoObjeto(
            final BigDecimal valor,
            @NonNull final MappedRecylerData c
    ) {
        c.adicionaValor(valor);
        c.adicionaAoSaldoAcumulado(valor);
    }

    protected void subtraiValor(
            final BigDecimal valor,
            @NonNull final MappedRecylerData c
    ) {
        c.subtraiValor(valor);
        c.removeDoSaldoAcumulado(valor);
    }

    protected void adicionaRateio(
            final BigDecimal valorRateio,
            final @NonNull MappedRecylerData c
    ) {
        c.adicionaValor(valorRateio);
        c.adicionaAoSaldoAcumulado(valorRateio);
    }

    protected void removeRateio(
            final BigDecimal valorRateio,
            @NonNull final MappedRecylerData c
    ) {
        c.subtraiValor(valorRateio);
        c.removeDoSaldoAcumulado(valorRateio);
    }

}

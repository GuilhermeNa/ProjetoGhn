package br.com.transporte.AppGhn.ui.fragment.areaMotorista.resumo;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class CalculaCardFrete {

    public interface CalculaUiFreteCallback {
        void freteLiquido(BigDecimal valor);

        void desconto(BigDecimal valor);

        void freteBruto(BigDecimal valor);
    }

    public void run(
            final List<Frete> dataSet,
            @NonNull final CalculaUiFreteCallback callback
    ) {
        BigDecimal somaFreteLiquidoAReceber = CalculoUtil.somaFreteLiquido(dataSet);
        BigDecimal somaDescontosNoFrete = CalculoUtil.somaDescontoNoFrete(dataSet);
        BigDecimal somaFreteBruto = CalculoUtil.somaFreteBruto(dataSet);
        callback.freteLiquido(somaFreteLiquidoAReceber);
        callback.desconto(somaDescontosNoFrete);
        callback.freteBruto(somaFreteBruto);
    }

}

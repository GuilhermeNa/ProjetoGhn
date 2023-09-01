package br.com.transporte.AppGhn.model.helpers;

import static br.com.transporte.AppGhn.util.BigDecimalConstantes.BIG_DECIMAL_CEM;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.transporte.AppGhn.exception.ValorInvalidoException;

public class FreteHelper {

    public static BigDecimal vinculaComissaoAplicada(BigDecimal comissaoPercentualAplicada) throws ValorInvalidoException {
        int compare = BIG_DECIMAL_CEM.compareTo(comissaoPercentualAplicada);
        if (compare <= 0) {
            throw new ValorInvalidoException("Comissão não pode ser superior a 100%");
        } else {
            return comissaoPercentualAplicada;
        }
    }

    public static BigDecimal calculaComissao(@NonNull BigDecimal comissaoPercentualAplicada, BigDecimal freteBruto) {
        return comissaoPercentualAplicada.divide(BIG_DECIMAL_CEM, 2, RoundingMode.HALF_EVEN)
                .multiply(freteBruto).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculaFreteLiquidoAReceber(@NonNull BigDecimal freteBruto, BigDecimal descontos, BigDecimal seguroDeCarga) {
        return freteBruto.subtract(descontos).subtract(seguroDeCarga);
    }

}

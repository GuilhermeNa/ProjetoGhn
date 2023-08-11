package br.com.transporte.AppGhn.model.despesas;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class ParcelaDeSeguro extends Parcela {

    public ParcelaDeSeguro(int numeroDaParcela, LocalDate data, BigDecimal valor, boolean valido, int idRefSeguro, boolean paga, TipoDespesa tipo, int refCavalo) {
        super();
        super.setNumeroDaParcela(numeroDaParcela);
        super.setData(data);
        super.setValor(valor);
        super.setValido(valido);
        super.setRefSeguro(idRefSeguro);
        super.setPaga(paga);
        super.setTipoDespesa(tipo);
        super.setRefCavalo(refCavalo);
    }


    public ParcelaDeSeguro() {

    }
}

package br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.extensions;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.domain.model.DespesaAdmDiretaObject;

public class CalculaDespesaAdmDiretaTotalExt {

    public static BigDecimal getValor(@NonNull final List<DespesaAdmDiretaObject> objDespesaAdmDireta) {
        return objDespesaAdmDireta.stream()
                .map(DespesaAdmDiretaObject::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

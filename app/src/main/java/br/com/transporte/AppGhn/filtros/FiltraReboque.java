package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.SemiReboque;

public class FiltraReboque {

    @NonNull
    public static List<SemiReboque> listaPorCavaloId(@NonNull List<SemiReboque> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(r -> r.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

}

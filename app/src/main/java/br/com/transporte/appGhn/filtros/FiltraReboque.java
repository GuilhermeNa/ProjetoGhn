package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.SemiReboque;

public class FiltraReboque {

    /** @noinspection NumberEquality*/
    @NonNull
    public static List<SemiReboque> listaPorCavaloId(@NonNull List<SemiReboque> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(r -> r.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

}

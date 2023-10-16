package br.com.transporte.AppGhn.filtros;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.model.Motorista;

public class FiltraMotorista {

    @NonNull
    public static Motorista localizaPeloId(
            @NonNull final List<Motorista> dataSet,
            final Long morotistaId
    ) {
        Motorista motoristaLocalizado = null;
        for (Motorista m : dataSet) {
            if (m.getId() == morotistaId) {
                motoristaLocalizado = m;
            }
        }
        return motoristaLocalizado;
    }

    @NonNull
    public static List<String> listaDeNomes(@NonNull List<Motorista> listaDeMotoristas) {
        List<String> dataSet = new ArrayList<>();

        for (Motorista m : listaDeMotoristas) {
            dataSet.add(m.getNome());
        }

        return dataSet;
    }

    public static Motorista localizaPeloNome(@NonNull List<Motorista> dataSet, String nome) {
        Motorista motoristaEncontrado = null;
        for (Motorista m : dataSet) {
            if (m.getNome().toUpperCase(Locale.ROOT).equals(nome.toUpperCase(Locale.ROOT))) {
                motoristaEncontrado = m;
            }
        }
        return motoristaEncontrado;
    }
}
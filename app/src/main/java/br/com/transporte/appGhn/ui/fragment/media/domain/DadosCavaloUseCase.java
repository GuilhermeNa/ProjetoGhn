package br.com.transporte.appGhn.ui.fragment.media.domain;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;

public class DadosCavaloUseCase {

    public static Cavalo getCavaloSelecionado(
            @NonNull final List<Cavalo> dataSet,
            final int posicaoDoCavaloSelecionado
    ) {
        return dataSet.get(posicaoDoCavaloSelecionado);
    }

    public static void removeCavaloSelecionadoDaLista(
            @NonNull final List<Cavalo> dataSet,
            final int posicaoDoCavaloSelecionado
    ) {
        dataSet.remove(posicaoDoCavaloSelecionado);
    }

}

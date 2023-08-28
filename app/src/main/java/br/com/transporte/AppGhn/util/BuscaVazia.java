package br.com.transporte.AppGhn.util;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.view.View;

import androidx.annotation.NonNull;

public class BuscaVazia {

    public static void configura(int listaSize, View view, View recycler) {
        boolean buscaEncontrouResultado = verificaSeTemConteudoNaLista(listaSize);

        if (buscaEncontrouResultado)
            exibeResultadoDaBusca(view, recycler);
        else
            exibeAlertaDeBuscaSemResultado(view, recycler);
    }

    private static boolean verificaSeTemConteudoNaLista(int listaSize) {
        return listaSize > 0;
    }

    private static void exibeResultadoDaBusca(@NonNull View view, @NonNull View recycler) {
        view.setVisibility(INVISIBLE);
        recycler.setVisibility(VISIBLE);
    }

    private static void exibeAlertaDeBuscaSemResultado(@NonNull View view, @NonNull View recycler) {
        view.setVisibility(VISIBLE);
        recycler.setVisibility(INVISIBLE);
    }


}

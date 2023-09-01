package br.com.transporte.AppGhn.util;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_GONE;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExibirResultadoDaBusca_sucessoOuAlerta {

    public static void configura(int listaSize, @Nullable View alerta, View recycler, String VISIBILIDADE) {
        boolean buscaEncontrouResultado = verificaSeTemConteudoNaLista(listaSize);

        if (buscaEncontrouResultado)
            exibeResultadoDaBusca(alerta, recycler);
        else
            exibeAlertaDeBuscaSemResultado(alerta, recycler, VISIBILIDADE);
    }

    private static boolean verificaSeTemConteudoNaLista(int listaSize) {
        return listaSize > 0;
    }

    private static void exibeResultadoDaBusca(@Nullable View alerta, @NonNull View recycler) {
        if (alerta != null) alerta.setVisibility(GONE);
        recycler.setVisibility(VISIBLE);
    }

    private static void exibeAlertaDeBuscaSemResultado(@Nullable View alerta, @NonNull View recycler, String VISIBILIDADE) {
        if (alerta != null) alerta.setVisibility(VISIBLE);
        switch (VISIBILIDADE){
            case VIEW_INVISIBLE:
                recycler.setVisibility(INVISIBLE);
                break;
            case VIEW_GONE:
                recycler.setVisibility(GONE);
                break;
        }
    }
}

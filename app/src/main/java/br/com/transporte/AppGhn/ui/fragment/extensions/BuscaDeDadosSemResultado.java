package br.com.transporte.AppGhn.ui.fragment.extensions;

import static android.view.View.INVISIBLE;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BuscaDeDadosSemResultado {

    public static void substituiRecyclerPorAviso(int listSize, RecyclerView recycler, LinearLayout layout) {
        boolean buscaSemResultado;
        buscaSemResultado = listSize == 0;

        if (buscaSemResultado) {
            recycler.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        } else {
            if (recycler.getVisibility() == View.GONE) {
                recycler.setVisibility(View.VISIBLE);
                layout.setVisibility(INVISIBLE);
            }
        }
    }
}

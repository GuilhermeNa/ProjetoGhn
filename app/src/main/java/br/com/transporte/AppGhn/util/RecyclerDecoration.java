package br.com.transporte.AppGhn.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import br.com.transporte.AppGhn.R;

public class RecyclerDecoration {

    public static void linhaHorizontal (Context context, RecyclerView recycler) {
        Drawable divider = ContextCompat.getDrawable(context, R.drawable.divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(divider);
        recycler.addItemDecoration(itemDecoration);
    }

}

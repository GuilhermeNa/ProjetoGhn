package br.com.transporte.AppGhn.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class ImagemUtil {

    public static Drawable pegaDrawable(@NonNull Activity context, String nome) {
        Resources resources = context.getResources();
        int idDrawable = resources.getIdentifier(nome, "drawable", context.getPackageName());
        Drawable drawable = resources.getDrawable(idDrawable);
        return drawable;
    }

}

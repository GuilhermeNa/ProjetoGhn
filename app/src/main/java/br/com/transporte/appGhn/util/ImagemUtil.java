package br.com.transporte.appGhn.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class ImagemUtil {

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable pegaDrawable(@NonNull Activity context, String nome) {
        Resources resources = context.getResources();
        @SuppressLint("DiscouragedApi") int idDrawable = resources.getIdentifier(nome, "drawable", context.getPackageName());
        return resources.getDrawable(idDrawable);
    }

}

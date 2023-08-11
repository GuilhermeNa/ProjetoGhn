package br.com.transporte.AppGhn.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class ImagemUtil {

    public static Drawable pegaDrawable(Activity context, String nome) {
        Resources resources = context.getResources();
        int idDrawable = resources.getIdentifier(nome, "drawable", context.getPackageName());
        Drawable drawable = resources.getDrawable(idDrawable);
        return drawable;
    }


}

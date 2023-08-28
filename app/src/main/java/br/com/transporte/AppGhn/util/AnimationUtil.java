package br.com.transporte.AppGhn.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationUtil {
    public static void defineAnimacao(Context context, int animId, View campo){
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        campo.startAnimation(animation);
    }
}

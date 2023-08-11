package br.com.transporte.AppGhn.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MensagemUtil {

    public static void snackBar(View view, String mensagem) {
        Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"));
        snackbar.setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    public static void toast(Context context, String mensagem){
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }
}

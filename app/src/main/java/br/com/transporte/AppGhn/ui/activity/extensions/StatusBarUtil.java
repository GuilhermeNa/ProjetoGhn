package br.com.transporte.AppGhn.ui.activity.extensions;

import android.content.Context;
import android.view.Window;

import androidx.core.content.ContextCompat;

import br.com.transporte.AppGhn.R;

public class StatusBarUtil {

    public static void setStatusBarColor(Context context, Window window) {
        int color = ContextCompat.getColor(context, R.color.midnightblue);
        window.setStatusBarColor(color);

    }
}

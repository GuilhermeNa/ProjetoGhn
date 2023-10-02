package br.com.transporte.AppGhn.ui.activity.areaMotoristaActivity;

import android.widget.ImageView;
import android.widget.TextView;

class BottomAppBarBtn {
    private final ImageView imgView;
    private final TextView txtView;
    private final int indice;

    BottomAppBarBtn(ImageView imgView, TextView txtView, int indice) {
        this.imgView = imgView;
        this.txtView = txtView;
        this.indice = indice;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public TextView getTxtView() {
        return txtView;
    }

    public int getIndice() {
        return indice;
    }
}

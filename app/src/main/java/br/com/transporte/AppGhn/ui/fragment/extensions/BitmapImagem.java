package br.com.transporte.AppGhn.ui.fragment.extensions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import br.com.transporte.AppGhn.model.Motorista;

public abstract class BitmapImagem {

    public static Bitmap decodificaBitmapEmString(Motorista motorista) {
        if(motorista.getImg() == null){
            throw new NullPointerException("Não foi possivel carregar a imagem");
        }
        byte[] decode = Base64.decode(motorista.getImg(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    public static String codificaBitmapEmString(Bitmap imagem) {
        byte[] fotoEmBytes;
        ByteArrayOutputStream streamDaFotoEmBytes = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.PNG, 70, streamDaFotoEmBytes);
        fotoEmBytes = streamDaFotoEmBytes.toByteArray();
        return Base64.encodeToString(fotoEmBytes, Base64.DEFAULT);
    }
}
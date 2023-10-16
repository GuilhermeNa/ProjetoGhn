package br.com.transporte.AppGhn.util;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class MascaraDataUtil {

    public static void MascaraSimples(EditText campo, String formato) {
        campo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                SimpleMaskFormatter smf = new SimpleMaskFormatter(formato);
                MaskTextWatcher mtw = new MaskTextWatcher(campo, smf);
                campo.addTextChangedListener(mtw);
            }
        });
    }

    public static void MascaraData(@NonNull EditText campo) {
        campo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                MaskPattern mp03 = new MaskPattern("[0-3]");
                MaskPattern mp09 = new MaskPattern("[0-9]");
                MaskPattern mp01 = new MaskPattern("[0-1]");
                MaskFormatter mf = new MaskFormatter("[0-3][0-9]/[0-1][0-9]/[0-9][0-9]");
                mf.registerPattern(mp01);
                mf.registerPattern(mp03);
                mf.registerPattern(mp09);

                MaskTextWatcher mtw = new MaskTextWatcher(campo, mf);
                campo.addTextChangedListener(mtw);
            }
        });
    }

}

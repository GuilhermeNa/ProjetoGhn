package br.com.transporte.AppGhn.util;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormataNumerosUtil {

    @NonNull
    public static String formataMoedaPadraoBr(BigDecimal preco) {
        NumberFormat formatoBr = DecimalFormat.getCurrencyInstance(new Locale("pt", "br"));
        return formatoBr.format(preco).replace("R$", "R$ ");
    }


    public static String formataNumero(BigDecimal numero){
        DecimalFormat formato = new DecimalFormat("#,###.00");
        return formato.format(numero);
    }




}

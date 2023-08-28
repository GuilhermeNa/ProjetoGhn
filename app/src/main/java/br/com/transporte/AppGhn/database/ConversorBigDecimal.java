package br.com.transporte.AppGhn.database;


import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ConversorBigDecimal {

    @TypeConverter
    public String bdParaString(BigDecimal valor) {
        if (valor != null)
            return valor.toPlainString();
        else
            return "0.00";
    }

    @TypeConverter
    public BigDecimal stringParaBD(String string) {
        if (string != null)
            return new BigDecimal(string);
        else
            return new BigDecimal(BigInteger.ZERO);
    }


}

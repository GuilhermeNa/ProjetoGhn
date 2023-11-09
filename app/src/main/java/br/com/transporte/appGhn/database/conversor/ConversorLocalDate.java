package br.com.transporte.appGhn.database.conversor;

import androidx.room.TypeConverter;

import java.time.LocalDate;

import br.com.transporte.appGhn.util.ConverteDataUtil;


public class ConversorLocalDate {

    @TypeConverter
    public String paraString(LocalDate date) {
        if (date != null)
            return ConverteDataUtil.dataParaString(date);
        else
            return null;
    }

    @TypeConverter
    public LocalDate paraDate(String date) {
        if (date != null)
            return ConverteDataUtil.stringParaData(date);
        else
            return null;
    }


}

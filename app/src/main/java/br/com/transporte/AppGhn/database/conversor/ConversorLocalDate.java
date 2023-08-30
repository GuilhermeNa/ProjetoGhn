package br.com.transporte.AppGhn.database.conversor;

import androidx.room.TypeConverter;

import java.time.LocalDate;

import br.com.transporte.AppGhn.util.ConverteDataUtil;


public class ConversorLocalDate {

    @TypeConverter
    public String paraString(LocalDate date){
        return ConverteDataUtil.dataParaString(date);
    }

    @TypeConverter
    public LocalDate paraDate(String date){
        if(date != null){
            return ConverteDataUtil.stringParaData(date);
        }
        return null;
    }



}

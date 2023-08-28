package br.com.transporte.AppGhn.database;

import android.text.format.DateUtils;

import androidx.room.TypeConverter;

import java.time.LocalDate;

import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;


public class ConversorLocalDate {

    @TypeConverter
    public String dateToString(LocalDate date){
        return ConverteDataUtil.dataParaString(date);
    }

    @TypeConverter
    public LocalDate stringToDate(String date){
        if(date != null){
            return ConverteDataUtil.stringParaData(date);
        }
        return null;
    }



}

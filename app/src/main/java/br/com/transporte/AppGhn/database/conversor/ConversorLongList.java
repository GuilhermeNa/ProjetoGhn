package br.com.transporte.AppGhn.database.conversor;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ConversorLongList {
    @TypeConverter
    public String toString(List<Long> lista) {
        if(lista == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Long>>(){
        }.getType();
        return gson.toJson(lista, type);
    }

    @TypeConverter
    public List<Long> toList(String lista){
        if(lista == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Long>>(){
        }.getType();
        return gson.fromJson(lista, type);
    }

}

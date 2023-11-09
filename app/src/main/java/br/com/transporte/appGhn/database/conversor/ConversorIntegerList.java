package br.com.transporte.appGhn.database.conversor;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ConversorIntegerList {
    @TypeConverter
    public String paraString(List<Integer> lista){
        if(lista == null){
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>(){
        }.getType();
        return gson.toJson(lista, type);
    }

    @TypeConverter
    public List<Integer> paraInteger(String lista){
        if(lista == null){
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>(){
        }.getType();
        return gson.fromJson(lista, type);
    }
}

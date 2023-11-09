package br.com.transporte.appGhn.database.conversor;

import static br.com.transporte.appGhn.model.enums.TipoCustoManutencao.EXTRAORDINARIA;
import static br.com.transporte.appGhn.model.enums.TipoCustoManutencao.PERIODICA;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.appGhn.model.enums.TipoCustoManutencao;

public class ConversorTipoCustoManutencao {

    @TypeConverter
    public String paraString(@NonNull TipoCustoManutencao tipo){
        switch (tipo){
            case PERIODICA:
                return PERIODICA.getDescricao();

            case EXTRAORDINARIA:
                return EXTRAORDINARIA.getDescricao();
        }
        return null;
    }

    @TypeConverter
    public TipoCustoManutencao paraTipo(@NonNull String tipo) {
        switch (tipo){
            case "PERIODICA":
                return PERIODICA;

            case "EXTRAORDINARIA":
                return EXTRAORDINARIA;
        }
        return null;
    }



}

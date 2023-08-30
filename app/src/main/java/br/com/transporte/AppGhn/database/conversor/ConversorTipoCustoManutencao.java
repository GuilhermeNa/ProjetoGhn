package br.com.transporte.AppGhn.database.conversor;

import static br.com.transporte.AppGhn.model.enums.TipoAbastecimento.PARCIAL;
import static br.com.transporte.AppGhn.model.enums.TipoAbastecimento.TOTAL;
import static br.com.transporte.AppGhn.model.enums.TipoCustoManutencao.EXTRAORDINARIA;
import static br.com.transporte.AppGhn.model.enums.TipoCustoManutencao.PERIODICA;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.AppGhn.model.enums.TipoAbastecimento;
import br.com.transporte.AppGhn.model.enums.TipoCustoManutencao;

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

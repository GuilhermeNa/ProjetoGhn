package br.com.transporte.AppGhn.database.conversor;

import static br.com.transporte.AppGhn.model.enums.TipoAbastecimento.PARCIAL;
import static br.com.transporte.AppGhn.model.enums.TipoAbastecimento.TOTAL;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.AppGhn.model.enums.TipoAbastecimento;

public class ConversorTipoAbastecimento {

    @TypeConverter
    public String paraString(@NonNull TipoAbastecimento tipo){
        switch (tipo){
            case PARCIAL:
                return PARCIAL.getDescricao();

            case TOTAL:
                return TOTAL.getDescricao();
        }
        return null;
    }

    @TypeConverter
    public TipoAbastecimento paraTipo(@NonNull String tipo) {
        switch (tipo){
            case "PARCIAL":
                return PARCIAL;

            case "TOTAL":
                return TOTAL;
        }
        return null;
    }

}

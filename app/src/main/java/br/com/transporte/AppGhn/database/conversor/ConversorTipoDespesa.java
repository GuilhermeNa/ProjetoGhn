package br.com.transporte.AppGhn.database.conversor;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class ConversorTipoDespesa {

    @TypeConverter
    public String paraString(@NonNull TipoDespesa tipo) {
        switch (tipo) {
            case DIRETA:
                return DIRETA.getDescricao();

            case INDIRETA:
                return INDIRETA.getDescricao();
        }
        return null;
    }

    @TypeConverter
    public TipoDespesa paraTipo(@NonNull String tipo) {
        switch (tipo) {
            case "DIRETA":
                return DIRETA;

            case "INDIRETA":
                return INDIRETA;
        }
        return null;
    }

}

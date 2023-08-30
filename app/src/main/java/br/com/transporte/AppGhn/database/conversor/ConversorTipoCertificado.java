package br.com.transporte.AppGhn.database.conversor;

import static br.com.transporte.AppGhn.model.enums.TipoCertificado.AET_ESTADUAL;
import static br.com.transporte.AppGhn.model.enums.TipoCertificado.AET_FEDERAL;
import static br.com.transporte.AppGhn.model.enums.TipoCertificado.ANTT;
import static br.com.transporte.AppGhn.model.enums.TipoCertificado.CRLV;
import static br.com.transporte.AppGhn.model.enums.TipoCertificado.CRONOTACOGRAFO;
import static br.com.transporte.AppGhn.model.enums.TipoCertificado.DIGITAL;
import static br.com.transporte.AppGhn.model.enums.TipoCertificado.OUTROS_DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoCertificado.OUTROS_INDIRETA;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.AppGhn.model.enums.TipoCertificado;

public class ConversorTipoCertificado {

    @TypeConverter
    public String paraString(@NonNull TipoCertificado tipo) {
        switch (tipo) {
            case CRONOTACOGRAFO:
                return CRONOTACOGRAFO.getDescricao();
            case AET_ESTADUAL:
                return AET_ESTADUAL.getDescricao();
            case AET_FEDERAL:
                return AET_FEDERAL.getDescricao();
            case CRLV:
                return CRLV.getDescricao();
            case OUTROS_DIRETA:
                return OUTROS_DIRETA.getDescricao();
            case DIGITAL:
                return DIGITAL.getDescricao();
            case ANTT:
                return ANTT.getDescricao();
            case OUTROS_INDIRETA:
                return OUTROS_INDIRETA.getDescricao();
        }
        return null;
    }

    @TypeConverter
    public TipoCertificado paraTipo(@NonNull String tipo) {
        switch (tipo) {
            case "CRONOTACOGRAFO":
                return CRONOTACOGRAFO;
            case "AET_ESTADUAL":
                return AET_ESTADUAL;
            case "AET_FEDERAL":
                return AET_FEDERAL;
            case "CRLV":
                return CRLV;
            case "OUTROS_DIRETA":
                return OUTROS_DIRETA;
            case "DIGITAL":
                return DIGITAL;
            case "ANTT":
                return ANTT;
            case "OUTROS_INDIRETA":
                return OUTROS_INDIRETA;
        }
        return null;
    }

}

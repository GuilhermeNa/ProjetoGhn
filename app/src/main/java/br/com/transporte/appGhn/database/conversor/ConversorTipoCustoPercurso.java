package br.com.transporte.appGhn.database.conversor;

import static br.com.transporte.appGhn.model.enums.TipoCustoDePercurso.NAO_REEMBOLSAVEL;
import static br.com.transporte.appGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;
import static br.com.transporte.appGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_JA_PAGO;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.appGhn.model.enums.TipoCustoDePercurso;

public class ConversorTipoCustoPercurso {

    @TypeConverter
    public String paraString(@NonNull TipoCustoDePercurso tipo){
        switch (tipo){
            case NAO_REEMBOLSAVEL:
                return NAO_REEMBOLSAVEL.getDescricao();

            case REEMBOLSAVEL_JA_PAGO:
                return REEMBOLSAVEL_JA_PAGO.getDescricao();

            case REEMBOLSAVEL_EM_ABERTO:
                return REEMBOLSAVEL_EM_ABERTO.getDescricao();
        }
        return null;
    }

    @TypeConverter
    public TipoCustoDePercurso paraTipo(@NonNull String tipo){
            switch (tipo){
                case "NAO_REEMBOLSAVEL":
                    return NAO_REEMBOLSAVEL;

                case "REEMBOLSAVEL_EM_ABERTO":
                    return REEMBOLSAVEL_EM_ABERTO;

                case "REEMBOLSAVEL_JA_PAGO":
                    return REEMBOLSAVEL_JA_PAGO;
        }
        return null;
    }
}

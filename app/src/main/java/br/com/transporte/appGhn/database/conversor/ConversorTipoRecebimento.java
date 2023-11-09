package br.com.transporte.appGhn.database.conversor;

import static br.com.transporte.appGhn.model.enums.TipoRecebimentoFrete.ADIANTAMENTO;
import static br.com.transporte.appGhn.model.enums.TipoRecebimentoFrete.SALDO;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.appGhn.model.enums.TipoRecebimentoFrete;

public class ConversorTipoRecebimento {

    @TypeConverter
    public String paraString(@NonNull TipoRecebimentoFrete tipo) {
        switch (tipo) {
            case SALDO:
                return SALDO.getDescricao();

            case ADIANTAMENTO:
                return ADIANTAMENTO.getDescricao();
        }
        return null;
    }

    @TypeConverter
    public TipoRecebimentoFrete paraTipo(@NonNull String tipo) {
        switch (tipo) {
            case "SALDO":
                return SALDO;
            case "ADIANTAMENTO":
                return ADIANTAMENTO;
        }
        return null;
    }

}

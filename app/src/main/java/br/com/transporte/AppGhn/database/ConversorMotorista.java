package br.com.transporte.AppGhn.database;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.filtros.FiltraMotorista;
import br.com.transporte.AppGhn.model.Motorista;

public class ConversorMotorista {

    @TypeConverter
    public int motoristaToId(@NonNull Motorista motorista) {
        return motorista.getId();
    }

    @TypeConverter
    public Motorista idToMotorista(int motoristaId){
        Motorista motoristaEncontrado = null;

        try{
            motoristaEncontrado = FiltraMotorista.localizaPeloId(motoristaId);
            return motoristaEncontrado;
        } catch (ObjetoNaoEncontrado e) {
            e.printStackTrace();
            return null;
        }
    }
}

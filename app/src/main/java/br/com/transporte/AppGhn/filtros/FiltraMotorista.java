package br.com.transporte.AppGhn.filtros;


import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.Motorista;

public class FiltraMotorista {
    private static Motorista motoristaLocalizado;
    //private final static MotoristaDAO dao = new MotoristaDAO();
    
    @NonNull
    public static Motorista localizaPeloId(int morotistaId, Context context) throws ObjetoNaoEncontrado {
        RoomMotoristaDao dao = GhnDataBase.getInstance(context).getRoomMotoristaDao();
        motoristaLocalizado = buscaPorMotoristaNoDatabase(morotistaId, dao);
        return verificaNulidadeDoObjeto();
    }

    public static List<String> listaDeNomes(List<Motorista> listaDeMotoristas){
        List<String> dataSet = new ArrayList<>();

        for (Motorista m: listaDeMotoristas){
            dataSet.add(m.getNome());
        }

        return dataSet;
    }


    @NonNull
    private static Motorista verificaNulidadeDoObjeto() throws ObjetoNaoEncontrado {
        if (motoristaLocalizado != null) {
            return motoristaLocalizado;
        }
        throw new ObjetoNaoEncontrado(OBJETO_NULL);
    }

    private static Motorista buscaPorMotoristaNoDatabase(int morotistaId, @NonNull RoomMotoristaDao dao) {
        Motorista motoristaLocalizado = null;
        
        for (Motorista m : dao.todos()) {
            if (m.getId() == morotistaId) {
                motoristaLocalizado = m;
            }
        }
        return motoristaLocalizado;
    }

}
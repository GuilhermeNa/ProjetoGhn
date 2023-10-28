package br.com.transporte.AppGhn.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.ui.fragment.desempenho.domain.mappers.MappedRecylerData;

public class CavaloDesempenhoDao {

    private final static List<MappedRecylerData> dao = new ArrayList<>();

    //---------------------------------- Manipula dao ----------------------------------------------

    public void adiciona(MappedRecylerData detalhes){
        dao.add(detalhes);
    }

    public void edita(MappedRecylerData detalhes) {
        MappedRecylerData detalheLocalizado = localizaPeloid(detalhes.getCavaloId());
        if(detalheLocalizado != null){
            int posicaoDetalhe = dao.indexOf(detalheLocalizado);
            dao.set(posicaoDetalhe, detalhes);
        }
    }

    public void deleta(Long detalheId) {
        MappedRecylerData detalheLocalizado = localizaPeloid(detalheId);
        if(detalheLocalizado != null){
            dao.remove(detalheLocalizado);
        }
    }

    public void clear(){
            dao.clear();
        MappedRecylerData.resetaAcumulado();
    }

    //---------------------------------- Retorna Lista ---------------------------------------------

    public List<MappedRecylerData> listaTodos(){
        return new ArrayList<>(dao);
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    public MappedRecylerData localizaPeloid(Long detalheId){
        MappedRecylerData detalheLocalizado = null;
        for(MappedRecylerData d: dao){
            if(d.getCavaloId() == detalheId){
                detalheLocalizado = d;
            }
        }
        return detalheLocalizado;
    }

}

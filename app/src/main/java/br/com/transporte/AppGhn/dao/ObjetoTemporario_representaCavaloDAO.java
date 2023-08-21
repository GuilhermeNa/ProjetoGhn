package br.com.transporte.AppGhn.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.temporarios.ObjetoTemporario_representaCavalo;

public class ObjetoTemporario_representaCavaloDAO {

    private final static List<ObjetoTemporario_representaCavalo> dao = new ArrayList<>();

    //---------------------------------- Manipula dao ----------------------------------------------

    public void adiciona(ObjetoTemporario_representaCavalo detalhes){
        dao.add(detalhes);
    }

    public void edita(ObjetoTemporario_representaCavalo detalhes) {
        ObjetoTemporario_representaCavalo detalheLocalizado = localizaPeloid(detalhes.getId());
        if(detalheLocalizado != null){
            int posicaoDetalhe = dao.indexOf(detalheLocalizado);
            dao.set(posicaoDetalhe, detalhes);
        }
    }

    public void deleta(int detalheId) {
        ObjetoTemporario_representaCavalo detalheLocalizado = localizaPeloid(detalheId);
        if(detalheLocalizado != null){
            dao.remove(detalheLocalizado);
        }
    }

    public void clear(){
        dao.clear();
        ObjetoTemporario_representaCavalo.resetaAcumulado();
    }

    //---------------------------------- Retorna Lista ---------------------------------------------

    public List<ObjetoTemporario_representaCavalo> listaTodos(){
        return new ArrayList<>(dao);
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    public ObjetoTemporario_representaCavalo localizaPeloid(int detalheId){
        ObjetoTemporario_representaCavalo detalheLocalizado = null;
        for(ObjetoTemporario_representaCavalo d: dao){
            if(d.getId() == detalheId){
                detalheLocalizado = d;
            }
        }
        return detalheLocalizado;
    }

}

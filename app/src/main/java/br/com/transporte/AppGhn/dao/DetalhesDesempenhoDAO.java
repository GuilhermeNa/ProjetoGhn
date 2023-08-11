package br.com.transporte.AppGhn.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.temporarios.DetalhesDesempenho;

public class DetalhesDesempenhoDAO {

    private final static List<DetalhesDesempenho> dao = new ArrayList<>();

    //---------------------------------- Manipula dao ----------------------------------------------

    public void adiciona(DetalhesDesempenho detalhes){
        dao.add(detalhes);
    }

    public void edita(DetalhesDesempenho detalhes) {
        DetalhesDesempenho detalheLocalizado = localizaPeloid(detalhes.getId());
        if(detalheLocalizado != null){
            int posicaoDetalhe = dao.indexOf(detalheLocalizado);
            dao.set(posicaoDetalhe, detalhes);
        }
    }

    public void deleta(int detalheId) {
        DetalhesDesempenho detalheLocalizado = localizaPeloid(detalheId);
        if(detalheLocalizado != null){
            dao.remove(detalheLocalizado);
        }
    }

    public void clear(){
        dao.clear();
        DetalhesDesempenho.resetaAcumulado();
    }

    //---------------------------------- Retorna Lista ---------------------------------------------

    public List<DetalhesDesempenho> listaTodos(){
        return new ArrayList<>(dao);
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    public DetalhesDesempenho localizaPeloid(int detalheId){
        DetalhesDesempenho detalheLocalizado = null;
        for(DetalhesDesempenho d: dao){
            if(d.getId() == detalheId){
                detalheLocalizado = d;
            }
        }
        return detalheLocalizado;
    }

}

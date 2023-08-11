package br.com.transporte.AppGhn.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.SemiReboque;

public class SemiReboqueDAO {
    private final static List<SemiReboque> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


    public void adiciona(SemiReboque sr){
        dao.add(sr);
        sr.setId(contadorDeIds);
        contadorDeIds++;
    }

    public void deleta(int idSr) {
        SemiReboque srEncontrado = localizaPeloId(idSr);
        if(srEncontrado != null){
            dao.remove(srEncontrado);
        }
    }

    public void edita(SemiReboque sr) {
        SemiReboque srEncontrado = localizaPeloId(sr.getId());
        if(srEncontrado != null){
            int posicaoSr = dao.indexOf(srEncontrado);
            dao.set(posicaoSr, sr);
        }
    }


    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<SemiReboque> listaTodos(){
        return new ArrayList<>(dao);
    }

    public List<SemiReboque> listaPorIdDeCavalo(int cavaloId){
        List<SemiReboque> lista = new ArrayList<>();
        for(SemiReboque s: dao){
            if(s.getReferenciaCavalo() == cavaloId){
                lista.add(s);
            }
        }
        return lista;
    }



    //---------------------------------- Outros Metodos ---------------------------------------------


    public SemiReboque localizaPeloId(int idSr){
        SemiReboque srEncontrado = null;
        for (SemiReboque sr: dao) {
            if(sr.getId() == idSr){
                srEncontrado = sr;
            }
        }
        return srEncontrado;
    }


}

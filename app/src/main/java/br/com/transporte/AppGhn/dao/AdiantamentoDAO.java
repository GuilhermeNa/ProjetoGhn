package br.com.transporte.AppGhn.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.Adiantamento;

public class AdiantamentoDAO {
    private final static List<Adiantamento> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


    public void adiciona(Adiantamento adiantamento){
        dao.add(adiantamento);
        adiantamento.setId(contadorDeIds);
        contadorDeIds++;
    }

    public void edita(Adiantamento adiantamento) {
        Adiantamento adiantamentoEncontrado = localizaPeloId(adiantamento.getId());
        if(adiantamentoEncontrado != null){
            int posicaoAdiantamento = dao.indexOf(adiantamentoEncontrado);
            dao.set(posicaoAdiantamento, adiantamento);
        }
    }

    public void deleta(int adiantamentoId) {
        Adiantamento adiantamentoEncontrado = localizaPeloId(adiantamentoId);
        if(adiantamentoEncontrado != null){
            dao.remove(adiantamentoEncontrado);
        }
    }


    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<Adiantamento> listaTodos() {
       return new ArrayList<>(dao);
    }

    public List<Adiantamento> listaPorCavalo(int cavaloId) {
        List<Adiantamento> lista = new ArrayList<>();
        for(Adiantamento a: dao){
            if(a.getRefCavalo() == cavaloId){
                lista.add(a);
            }
        }
        return lista;
    }

    public List<Adiantamento> listaPorCavaloEAberto(int cavaloId) {
        List<Adiantamento> listaPorPlaca = listaPorCavalo(cavaloId);
        List<Adiantamento> listaEmAberto = new ArrayList<>();

        for(Adiantamento a: listaPorPlaca){
            if(!a.isAdiantamentoJaFoiDescontado()){
                listaEmAberto.add(a);
            }
        }
        return listaEmAberto;
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public Adiantamento localizaPeloId(int id){
        Adiantamento adiantamentoEncontrado = null;
        for(Adiantamento a: dao){
            if(a.getId() == id){
                adiantamentoEncontrado = a;
            }
        }
        return adiantamentoEncontrado;
    }


}

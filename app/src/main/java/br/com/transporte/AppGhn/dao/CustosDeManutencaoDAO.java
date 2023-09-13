package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;

public class CustosDeManutencaoDAO {
    private final static List<CustosDeManutencao> dao = new ArrayList<>();
    private static int contadorDeIds = 1;

    public CustosDeManutencaoDAO() {}

    //---------------------------------- Manipula dao ----------------------------------------------

   /* public void adiciona(CustosDeManutencao manutencao){
        manutencao.setId(contadorDeIds);
        dao.add(manutencao);
        contadorDeIds++;
    }

    public void edita(CustosDeManutencao manutencao) {
        CustosDeManutencao manutencaoLocalizada = localizaPeloid(manutencao.getId());
        if(manutencaoLocalizada != null){
            int posicaoManutencao = dao.indexOf(manutencaoLocalizada);
            dao.set(posicaoManutencao, manutencao);
        }
    }

    public void deleta(int manutencaoId) {
        CustosDeManutencao manutencaoLocalizada = localizaPeloid(manutencaoId);
        if(manutencaoLocalizada != null){
            dao.remove(manutencaoLocalizada);
        }
    }*/

    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<CustosDeManutencao> listaTodos(){
        return new ArrayList<>(dao);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDeManutencao> listaFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        List<CustosDeManutencao> lista = new ArrayList<>();

        for(CustosDeManutencao c: dao){
            if(!c.getData().isBefore(dataInicial) && !c.getData().isAfter(dataFinal)){
                lista.add(c);
            }
        }
        return lista;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDeManutencao> listaFiltradaPorPlacaEData(Long cavaloId, LocalDate dataInicial, LocalDate dataFinal){
        List<CustosDeManutencao> listaPorPlaca = listaFiltradaPorCavalo(cavaloId);
        List<CustosDeManutencao> listaPorData= new ArrayList<>();

        for(CustosDeManutencao c: listaPorPlaca){
            if(!c.getData().isBefore(dataInicial) && !c.getData().isAfter(dataFinal)){
                listaPorData.add(c);
            }
        }
        return listaPorData;
    }


    public List<CustosDeManutencao> listaFiltradaPorCavalo(Long cavaloId){
        List<CustosDeManutencao> lista = new ArrayList<>();
        for(CustosDeManutencao c: dao){
            if(c.getRefCavaloId() == cavaloId){
                lista.add(c);
            }
        }
        return lista;
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public CustosDeManutencao localizaPeloid(int manutencaoId){
        CustosDeManutencao manutencaoLocalizada = null;
        for(CustosDeManutencao c: dao){
            if(c.getId() == manutencaoId){
                manutencaoLocalizada = c;
            }
        }
        return manutencaoLocalizada;
    }

}

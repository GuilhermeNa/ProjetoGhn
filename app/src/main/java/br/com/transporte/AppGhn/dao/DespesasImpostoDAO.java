package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;

public class DespesasImpostoDAO {
    private final static ArrayList<DespesasDeImposto> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


   /* public void adiciona(DespesasDeImposto imposto) {
        dao.add(imposto);
        imposto.setId(contadorDeIds);
        contadorDeIds++;
    }

    public void edita(DespesasDeImposto imposto) {
        DespesasDeImposto despesaEncontrada = localizaPeloid(imposto.getId());
        if (despesaEncontrada != null) {
            int posicaoImposto = dao.indexOf(despesaEncontrada);
            dao.set(posicaoImposto, imposto);
        }
    }

    public void deleta(int id) {
        DespesasDeImposto impostoEncontrado = localizaPeloid(id);
        if(impostoEncontrado != null){
            dao.remove(impostoEncontrado);
        }
    }*/


    //---------------------------------- Retorna Listas ---------------------------------------------

    public List<DespesasDeImposto> listaTodos(){
        return new ArrayList<>(dao);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<DespesasDeImposto> listaFiltradaPorData(LocalDate data01, LocalDate data02) {
        ArrayList<DespesasDeImposto> lista = new ArrayList<>();
        for (DespesasDeImposto d : dao) {
            if (!d.getData().isBefore(data01) && !d.getData().isAfter(data02)) {
                lista.add(d);
            }
        }
        return lista;
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public DespesasDeImposto localizaPeloid(int idImposto) {
        DespesasDeImposto impostoEncontrado = null;
        for (DespesasDeImposto d : dao) {
            if (d.getId() == idImposto) {
                impostoEncontrado = d;
            }
        }
        return impostoEncontrado;
    }

}

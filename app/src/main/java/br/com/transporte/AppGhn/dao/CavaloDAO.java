package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.Cavalo;

public class CavaloDAO {
    private final static List<Cavalo> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


  /*  public void adiciona(Cavalo cavalo) {
        cavalo.setId(contadorDeIds);
        dao.add(cavalo);
        contadorDeIds++;
    }

    public void edita(Cavalo cavalo) {
        Cavalo cavaloEncontrado = localizaPeloId(cavalo.getId());
        if (cavaloEncontrado != null) {
            int posicaoCavalo = dao.indexOf(cavaloEncontrado);
            dao.set(posicaoCavalo, cavalo);
        }
    }

    public void deleta(int id) {
        Cavalo cavaloEncontrado = localizaPeloId(id);
        if (cavaloEncontrado != null) {
            dao.remove(cavaloEncontrado);
        }
    }*/


    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<Cavalo> listaTodos() {
        return new ArrayList<>(dao);
    }

    public List<Cavalo> listaValidos(){
        List<Cavalo> collect = dao.stream()
                .filter(Cavalo::isValido)
                .collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    public List<String> listaPlacas() {
        List<String> lista = new ArrayList<>();
        for (Cavalo c : dao) {
            lista.add(c.getPlaca());
        }
        return lista;
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public Cavalo localizaPeloId(Long idCavalo) {
        Cavalo cavaloEncontrado = null;
        for (Cavalo c : dao) {
            if (c.getId() == idCavalo) {
                cavaloEncontrado = c;
            }
        }
        return cavaloEncontrado;
    }

    public Cavalo retornaCavaloAtravesDaPlaca(String placa) {
        Cavalo cavaloEncontrado = null;
        placa = placa.toUpperCase(Locale.ROOT);
        for (Cavalo c : dao) {
            if (c.getPlaca().toUpperCase(Locale.ROOT).equals(placa)) {
                cavaloEncontrado = c;
            }
        }
        return cavaloEncontrado;
    }

}

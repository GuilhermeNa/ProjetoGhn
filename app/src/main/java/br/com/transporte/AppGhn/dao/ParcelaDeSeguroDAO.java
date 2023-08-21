package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.ParcelaDeSeguro;

public class ParcelaDeSeguroDAO {
    private static final List<ParcelaDeSeguro> dao = new ArrayList<>();
    private static int contadorDeIds = 1;

    public void adiciona(ParcelaDeSeguro parcela){
        parcela.setId(contadorDeIds);
        dao.add(parcela);
        contadorDeIds++;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void edita(ParcelaDeSeguro parcela){
        Optional<ParcelaDeSeguro> parcelaOptional = localizaPeloId(parcela.getId());
        parcelaOptional.ifPresent(p -> {
            int posicao = dao.indexOf(p);
            dao.set(posicao, parcela);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void delete(int parcelaId){
        Optional<ParcelaDeSeguro> parcelaOptional = localizaPeloId(parcelaId);
        parcelaOptional.ifPresent(dao::remove);
    }

    public List<ParcelaDeSeguro> listaTodos() {
        return new ArrayList<>(dao);
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<ParcelaDeSeguro> listaParcelasDoSeguro(int id) {
        return dao.stream()
                .filter(p -> p.getRefSeguro() == id)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<ParcelaDeSeguro> listaParcelasDoCavalo(int id) {
        return dao.stream()
                .filter(p -> p.getRefCavalo() == id)
                .collect(Collectors.toList());
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Optional<ParcelaDeSeguro> localizaPeloId(int parcelaId){
        return dao.stream()
                .filter(p -> p.getId() == parcelaId)
                .findAny();
    }

}

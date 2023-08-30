package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.model.ParcelaDeSeguro;

public class ParcelaDeSeguroDAO {
    private static final List<ParcelaDeSeguro> dao = new ArrayList<>();
    private static int contadorDeIds = 1;

    public void adiciona(@NonNull ParcelaDeSeguro parcela){
        parcela.setId(contadorDeIds);
        dao.add(parcela);
        contadorDeIds++;
    }

    public void edita(@NonNull ParcelaDeSeguro parcela){
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

    public List<ParcelaDeSeguro> listaParcelasDoSeguro(int id) {
        return dao.stream()
                .filter(p -> p.getRefSeguro() == id)
                .collect(Collectors.toList());
    }

    public List<ParcelaDeSeguro> listaParcelasDoCavalo(int id) {
        return dao.stream()
                .filter(p -> p.getRefCavaloId() == id)
                .collect(Collectors.toList());
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    private Optional<ParcelaDeSeguro> localizaPeloId(int parcelaId){
        return dao.stream()
                .filter(p -> p.getId() == parcelaId)
                .findAny();
    }

}

package br.com.transporte.AppGhn.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.model.Motorista;

public class MotoristaDAO {
    private final static List<Motorista> dao = new ArrayList<>();
    private static int contadorDeIds = 1;

    //---------------------------------- Manipula dao ----------------------------------------------

    public void adiciona(Motorista motorista) {
        dao.add(motorista);
        motorista.setId(contadorDeIds);
        contadorDeIds++;
    }

    public void edita(Motorista motorista) {
        Motorista motoristaEncontrado = localizaPeloId(motorista.getId());
        if (motoristaEncontrado != null) {
            int posicaoMotorista = dao.indexOf(motoristaEncontrado);
            dao.set(posicaoMotorista, motorista);
        }
    }

    public void deleta(int id) {
        Motorista motoristaEncontrado = localizaPeloId(id);
        if(motoristaEncontrado != null){
            dao.remove(motoristaEncontrado);
        }
    }

    //---------------------------------- Retorna Listas ---------------------------------------------

    public List<Motorista> listaTodos() {
        return new ArrayList<>(dao);
    }

    public List<String> listaNomes() {
        List<String> lista = new ArrayList<>();
        for (Motorista m: dao) {
            String nomeEmUpper = m.getNome().toUpperCase(Locale.ROOT);
            lista.add(nomeEmUpper);
        }
        return lista;
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    public Motorista localizaPeloId(int idMotorista) {
        Motorista motoristaLocalizado = null;
        for (Motorista m : dao) {
            if (m.getId() == idMotorista) {
                motoristaLocalizado = m;
            }
        }
        return motoristaLocalizado;
    }

    public Motorista localizaPeloNome(String nome) {
        Motorista motoristaEncontrado = null;
        for (Motorista m: dao) {
            if(m.getNome().toUpperCase(Locale.ROOT).equals(nome.toUpperCase(Locale.ROOT))){
                motoristaEncontrado = m;
            }
        }
        return motoristaEncontrado;
    }

}

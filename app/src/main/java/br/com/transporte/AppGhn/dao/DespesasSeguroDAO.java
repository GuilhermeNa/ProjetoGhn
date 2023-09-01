package br.com.transporte.AppGhn.dao;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;

public class DespesasSeguroDAO {
    private final static List<DespesaComSeguro> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------

 /*   public void adiciona(DespesaComSeguro seguros) {
        dao.add(seguros);
        seguros.setId(contadorDeIds);
        contadorDeIds++;
    }

    public void edita(DespesaComSeguro seguro) {
        DespesaComSeguro seguroLocalizado = localizaPeloId(seguro.getId());
        if (seguroLocalizado != null) {
            int posicaoSeguro = dao.indexOf(seguroLocalizado);
            dao.set(posicaoSeguro, seguro);
        }
    }

    public void deleta(int idSeguro) {
        DespesaComSeguro seguroLocalizado = localizaPeloId(idSeguro);
        if (seguroLocalizado != null) {
            dao.remove(seguroLocalizado);
        }
    }*/


    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<DespesaComSeguro> listaTodos() {
        return new ArrayList<>(dao);
    }

    public List<DespesaComSeguroFrota> listaSegurosFrota(){
        List<DespesaComSeguroFrota> lista = new ArrayList<>();
        for (DespesaComSeguro d: dao){
            if(d instanceof DespesaComSeguroFrota && d.isValido()){
                lista.add((DespesaComSeguroFrota) d);
            }
        }
        return lista;
    }

    public List<DespesaComSeguroDeVida> listaSegurosVidaValidos(){
        List<DespesaComSeguroDeVida> lista = new ArrayList<>();
        for (DespesaComSeguro d: dao){
            if(d instanceof DespesaComSeguroDeVida && d.isValido()){
                lista.add((DespesaComSeguroDeVida) d);
            }
        }
        return lista;
    }

    //---------------------------------- Outros Metodos ---------------------------------------------


    public DespesaComSeguro localizaPeloId(int seguroId) {
        DespesaComSeguro seguroLocalizado = null;
        for (DespesaComSeguro d : dao) {
            if (d.getId() == seguroId) {
                seguroLocalizado = d;
            }
        }
        return seguroLocalizado;
    }

    public DespesaComSeguro localizaSeguroPeloCavalo(int id){
        Log.d("teste", "Cavalo Id ->" +id);
        Optional<DespesaComSeguro> seguroOptional = dao.stream()
                .filter(s -> s.getTipoDespesa() == DIRETA)
                .filter(s -> s.getRefCavalo() == id)
                .filter(DespesaComSeguro::isValido)
                .findAny();
        Log.d("teste", "Optional ->" +seguroOptional);

        return seguroOptional.orElseGet(()->{
            Log.d("teste", "Retornou objeto vazio");
            return null;
        });
    }

}

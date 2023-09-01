package br.com.transporte.AppGhn.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete;

public class RecebimentoFreteDAO {
    private static final List<RecebimentoDeFrete> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------

/*
    public void adiciona(RecebimentoDeFrete recebimento) {
        recebimento.setId(contadorDeIds);
        dao.add(recebimento);
        contadorDeIds++;
    }

    public void edita(RecebimentoDeFrete recebimento) {
        RecebimentoDeFrete recebimentoLocalizado = localizaPeloId(recebimento.getId());
        if(recebimentoLocalizado != null){
            int posicaoRecebimento = dao.indexOf(recebimentoLocalizado);
            dao.set(posicaoRecebimento, recebimento);
        }
    }

    public void deleta(RecebimentoDeFrete recebimento) {
        RecebimentoDeFrete recebimentoLocalizado = localizaPeloId(recebimento.getId());
        if(recebimentoLocalizado != null){
            dao.remove(recebimentoLocalizado);
        }
    }*/

    public void deletaLista(List<RecebimentoDeFrete> listaFiltrada) {
        for(RecebimentoDeFrete r: listaFiltrada){
            dao.remove(r);
        }
    }

    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<RecebimentoDeFrete> listaPorIdFrete(int freteId) {
        List<RecebimentoDeFrete> lista = new ArrayList<>();
        for (RecebimentoDeFrete r : dao) {
            if (r.getRefFreteId() == freteId) {
                lista.add(r);
            }
        }
        return lista;
    }

    public List<RecebimentoDeFrete> listaTodos() {
        return new ArrayList<>(dao);
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public RecebimentoDeFrete localizaPeloId(int recebimentoId) {
        RecebimentoDeFrete recebimentoLocalizado = null;
        for(RecebimentoDeFrete r: dao){
            if(r.getId() == recebimentoId){
                recebimentoLocalizado = r;
            }
        }
        return recebimentoLocalizado;
    }

    public RecebimentoDeFrete retornaAdiantamento(int freteId) {
        List<RecebimentoDeFrete> lista = listaPorIdFrete(freteId);
        RecebimentoDeFrete recebimentoLocalizado = null;
        for (RecebimentoDeFrete r : lista) {
            if (r.getTipoRecebimentoFrete() == TipoRecebimentoFrete.ADIANTAMENTO) {
                recebimentoLocalizado = r;
            }
        }
        return recebimentoLocalizado;
    }

    public RecebimentoDeFrete retornaSaldo(int freteId) {
        List<RecebimentoDeFrete> lista = listaPorIdFrete(freteId);
        RecebimentoDeFrete recebimentoLocalizado = null;
        for (RecebimentoDeFrete r : lista) {
            if (r.getTipoRecebimentoFrete() == TipoRecebimentoFrete.SALDO) {
                recebimentoLocalizado = r;
            }
        }
        return recebimentoLocalizado;
    }

    public BigDecimal valorRecebido(int freteId){
        BigDecimal total = new BigDecimal("0.0");
        List<RecebimentoDeFrete> lista = listaPorIdFrete(freteId);
        for(RecebimentoDeFrete r: lista){
            total = total.add(r.getValor());

        }
        return total;
    }


}

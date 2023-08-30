package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.Frete;

public class FreteDAO {
    private final static List<Frete> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------


    public void adiciona(Frete frete) {
        frete.setId(contadorDeIds);
        dao.add(frete);
        contadorDeIds++;
    }

    public void edita(Frete novoFrete) {
        Frete freteEncontrado = localizaPeloId(novoFrete.getId());
        if (freteEncontrado != null) {
            int posicaoFrete = dao.indexOf(freteEncontrado);
            novoFrete.getAdmFrete().calculaComissaoELiquido(novoFrete);
            dao.set(posicaoFrete, novoFrete);
        }
    }

    public void deleta(int freteId) {
        Frete freteEncontrado = localizaPeloId(freteId);
        if (freteEncontrado != null) {
            dao.remove(freteEncontrado);
        }
    }


    //---------------------------------- Retorna Listas ---------------------------------------------


    public List<Frete> listaTodos() {
        return new ArrayList<>(dao);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Frete> listaFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        List<Frete> lista = new ArrayList<>();
        for (Frete f : dao) {
            if (!f.getData().isBefore(dataInicial) && !f.getData().isAfter(dataFinal)) {
                lista.add(f);
            }
        }
        return lista;
    }

    public List<Frete> listaFiltradaPorCavalo(int cavaloId) {
        List<Frete> lista = new ArrayList<>();
        for (Frete f : dao) {
            if (f.getRefCavaloId() == cavaloId) {
                lista.add(f);
            }
        }
        return lista;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Frete> listaFiltradaPorCavaloEData(int cavaloId, LocalDate data01, LocalDate data02) {
        List<Frete> listaPorCavalo = listaFiltradaPorCavalo(cavaloId);
        List<Frete> listaFiltrada = new ArrayList<>();

        for (Frete f : listaPorCavalo) {
            if (!f.getData().isBefore(data01) && !f.getData().isAfter(data02)) {
                listaFiltrada.add(f);
            }
        }
        return listaFiltrada;
    }

    public List<Frete> listaPorPlacaEComissaoAberta(int cavaloId) {
        List<Frete> listaPorCavalo = listaFiltradaPorCavalo(cavaloId);
        List<Frete> listaEmAberto = new ArrayList<>();

        for (Frete f : listaPorCavalo) {
            if (!f.getAdmFrete().isComissaoJaFoiPaga()) {
                listaEmAberto.add(f);
            }
        }
        return listaEmAberto;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Frete> listaFiltradaPorStatusEmAberto(LocalDate dataInicio, LocalDate dataFim) {
        List<Frete> listaPorData = listaFiltradaPorData(dataInicio, dataFim);
        List<Frete> lista = new ArrayList<>();

        for (Frete f : listaPorData) {
            if(!f.getAdmFrete().isFreteJaFoiPago()){
                lista.add(f);
            }
        }
        return lista;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Frete> listaFiltradaPorStatusJaRecebido(LocalDate dataInicio, LocalDate dataFim) {
        List<Frete> listaPorData = listaFiltradaPorData(dataInicio, dataFim);
        List<Frete> lista = new ArrayList<>();

        for (Frete f : listaPorData) {
            if(f.getAdmFrete().isFreteJaFoiPago()){
                lista.add(f);
            }
        }
        return lista;
    }


    //---------------------------------- Outros Metodos ---------------------------------------------


    public Frete localizaPeloId(int freteId) {
        Frete freteLocalizado = null;
        for (Frete f : dao) {
            if (f.getId() == freteId) {
                freteLocalizado = f;
            }
        }
        return freteLocalizado;
    }


}

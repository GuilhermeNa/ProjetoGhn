package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class DespesasAdmDAO {
    private final static  List<DespesaAdm> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------

    public void adiciona(DespesaAdm despesa) {
        dao.add(despesa);
        despesa.setId(contadorDeIds);
        contadorDeIds++;
    }

    public void edita(DespesaAdm despesa) {
        DespesaAdm despesaLocalizada = localizaPeloId(despesa.getId());
        if (despesaLocalizada != null) {
            int posicaoDespesa = dao.indexOf(despesaLocalizada);
            dao.set(posicaoDespesa, despesa);
        }
    }

    public void deleta(int despesaId) {
        DespesaAdm despesaEncontrada = localizaPeloId(despesaId);
        if (despesaEncontrada != null) {
            dao.remove(despesaEncontrada);
        }
    }


    //---------------------------------- Retorna Listas ---------------------------------------------

    public List<DespesaAdm> listaTodos(){
        return new ArrayList<>(dao);
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<DespesaAdm> listaFiltradaPorTipoEData(TipoDespesa tipo, LocalDate dataInicial, LocalDate dataFinal) {
        List<DespesaAdm> listaDespesaDireta = listaDespesasPorTipo(tipo);
        List<DespesaAdm> lista = new ArrayList<>();

        for (DespesaAdm d : listaDespesaDireta) {
            if (!d.getData().isBefore(dataInicial) && !d.getData().isAfter(dataFinal)) {
                lista.add(d);
            }
        }
        return lista;
    }

    public List<DespesaAdm> listaDespesasPorTipo(TipoDespesa tipo){
        List<DespesaAdm> lista = new ArrayList<>();
        for (DespesaAdm d: dao){
            if(d.getTipoDespesa() == tipo){
                lista.add((DespesaAdm) d);
            }
        }
        return lista;
    }



    //---------------------------------- Outros Metodos ---------------------------------------------


    public DespesaAdm localizaPeloId(int despesaId) {
        DespesaAdm despesalocalizada = null;
        for (DespesaAdm d : dao) {
            if (d.getId() == despesaId) {
                despesalocalizada = d;
            }
        }
        return despesalocalizada;
    }

}


package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.Salario;

public class SalarioDAO {
    private final static List<Salario> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------

    public void adiciona(Salario salario) {
        salario.setId(contadorDeIds);
        dao.add(salario);
        contadorDeIds++;
    }

    public void edita(Salario salario) {
        Salario salarioEncontrado = localizaPeloId(salario.getId());
        if (salarioEncontrado != null) {
            int posicaoSalario = dao.indexOf(salarioEncontrado);
            dao.set(posicaoSalario, salario);
        }
    }

    public void deleta(int salarioId) {
        Salario salarioEncontrado = localizaPeloId(salarioId);
        if (salarioEncontrado != null) {
            dao.remove(salarioEncontrado);
        }
    }


    //---------------------------------- Retorna Listas --------------------------------------------

    public List<Salario> listaTodos(){
        return new ArrayList<>(dao);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Salario> listaFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal){
        List<Salario> lista = new ArrayList<>();
        for (Salario s : dao) {
            if (!s.getData().isBefore(dataInicial) && !s.getData().isAfter(dataFinal)) {
                lista.add(s);
            }
        }
        return lista;
    }


    //---------------------------------- Outros Metodos --------------------------------------------

    public Salario localizaPeloId(int salarioId) {
        Salario salarioEncontrado = null;
        for (Salario s : dao) {
            if (s.getId() == salarioId) {
                salarioEncontrado = s;
            }
        }
        return salarioEncontrado;
    }
}

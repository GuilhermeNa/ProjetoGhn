package br.com.transporte.AppGhn.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeSalario;

public class SalarioDAO {
    private final static List<CustosDeSalario> dao = new ArrayList<>();
    private static int contadorDeIds = 1;


    //---------------------------------- Manipula dao ----------------------------------------------

    public void adiciona(CustosDeSalario salario) {
        salario.setId(contadorDeIds);
        dao.add(salario);
        contadorDeIds++;
    }

    public void edita(CustosDeSalario salario) {
        CustosDeSalario salarioEncontrado = localizaPeloId(salario.getId());
        if (salarioEncontrado != null) {
            int posicaoSalario = dao.indexOf(salarioEncontrado);
            dao.set(posicaoSalario, salario);
        }
    }

    public void deleta(int salarioId) {
        CustosDeSalario salarioEncontrado = localizaPeloId(salarioId);
        if (salarioEncontrado != null) {
            dao.remove(salarioEncontrado);
        }
    }


    //---------------------------------- Retorna Listas --------------------------------------------

    public List<CustosDeSalario> listaTodos(){
        return new ArrayList<>(dao);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CustosDeSalario> listaFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal){
        List<CustosDeSalario> lista = new ArrayList<>();
        for (CustosDeSalario s : dao) {
            if (!s.getData().isBefore(dataInicial) && !s.getData().isAfter(dataFinal)) {
                lista.add(s);
            }
        }
        return lista;
    }


    //---------------------------------- Outros Metodos --------------------------------------------

    public CustosDeSalario localizaPeloId(int salarioId) {
        CustosDeSalario salarioEncontrado = null;
        for (CustosDeSalario s : dao) {
            if (s.getId() == salarioId) {
                salarioEncontrado = s;
            }
        }
        return salarioEncontrado;
    }
}

package br.com.transporte.AppGhn.model.custos;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.abstracts.Custos;

public class CustosDeSalario extends Custos {
    private int refMotorista;
    private final List<Integer> refAdiantamentos = new ArrayList<>();
    private final List<Integer> refReembolsos = new ArrayList<>();
    private final List<Integer> refFretes = new ArrayList<>();

    public void setRefMotorista(int refMotorista) {
        this.refMotorista = refMotorista;
    }

    public List<Integer> getRefAdiantamentos() {
        return refAdiantamentos;
    }

    public List<Integer> getRefReembolsos() {
        return refReembolsos;
    }

    public List<Integer> getRefFretes() {
        return refFretes;
    }

    //---------------------------------- Outros Metodos ---------------------------------------------

    public void listaFretesAdiciona(int i){
        refFretes.add(i);
    }

    public void listaAdiantamentosAdiciona(int i){
        refAdiantamentos.add(i);
    }

    public void listaReembolsosAdiciona(int i){
        refReembolsos.add(i);
    }

}

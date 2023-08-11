package br.com.transporte.AppGhn.model;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.abstracts.Custos;

public class Salario extends Custos {
    private int refMotorista;
    private List<Integer> refAdiantamentos = new ArrayList<>();
    private List<Integer> refReembolsos = new ArrayList<>();
    private List<Integer> refFretes = new ArrayList<>();

    public int getRefMotorista() {
        return refMotorista;
    }

    public void setRefMotorista(int refMotorista) {
        this.refMotorista = refMotorista;
    }

    public List<Integer> getRefAdiantamentos() {
        return refAdiantamentos;
    }

    public void setRefAdiantamentos(List<Integer> refAdiantamentos) {
        this.refAdiantamentos = refAdiantamentos;
    }

    public List<Integer> getRefReembolsos() {
        return refReembolsos;
    }

    public void setRefReembolsos(List<Integer> refReembolsos) {
        this.refReembolsos = refReembolsos;
    }

    public List<Integer> getRefFretes() {
        return refFretes;
    }

    public void setRefFretes(List<Integer> refFretes) {
        this.refFretes = refFretes;
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

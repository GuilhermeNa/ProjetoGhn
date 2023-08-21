package br.com.transporte.AppGhn.model.despesas;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.abstracts.Despesas;

public class DespesasDeImposto extends Despesas {
    private String nome;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean temIdValido() {
        return id > 0;
    }

    public static List<String> listaDeImpostos(){
        List<String> lista = new ArrayList<>();
        lista.add("IRRF");
        lista.add("INSS");
        lista.add("FGTS");
        lista.add("SIMPLES NAC");
        lista.add("IPVA");
        lista.add("ICMS");
        lista.add("ISS");
        lista.add("PIS");
        lista.add("PASEP");
        lista.add("CSLL");
        lista.add("COFINS");
        lista.add("IPI");
        lista.add("IRPJ");

        return lista;
    }

}

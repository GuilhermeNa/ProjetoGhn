package br.com.transporte.AppGhn.model.despesas;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Despesas;

public class DespesaAdm extends Despesas {
    private String descricao;
    private int refPlacaCavalo;
    private int id;

    public DespesaAdm(LocalDate data, BigDecimal valor, String descricao, int refPlacaCavalo) {
       super.setData(data);
        this.setValorDespesa(valor);
        this.descricao = descricao;
        this.refPlacaCavalo = refPlacaCavalo;
    }

    public DespesaAdm() {

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getRefCavalo() {
        return refPlacaCavalo;
    }

    public void setRefPlacaCavalo(int refPlacaCavalo) {
        this.refPlacaCavalo = refPlacaCavalo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean temIdValido(){
        return id > 0;
    }
}

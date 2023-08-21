package br.com.transporte.AppGhn.model.despesas;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Despesas;

public class DespesaAdm extends Despesas {
    private String descricao;
    private int id;

    public DespesaAdm(LocalDate data, BigDecimal valor, String descricao, int refPlacaCavalo) {
        super.setData(data);
        super.setValorDespesa(valor);
        this.descricao = descricao;
        super.setRefCavalo(refPlacaCavalo);
    }

    public DespesaAdm() {

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean temIdValido() {
        return id > 0;
    }
}

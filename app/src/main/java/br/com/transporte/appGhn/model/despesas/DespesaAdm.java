package br.com.transporte.appGhn.model.despesas;

import androidx.room.Entity;

import br.com.transporte.appGhn.model.abstracts.Despesas;

@Entity
public class DespesaAdm extends Despesas {
    private String descricao;

    public DespesaAdm() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}

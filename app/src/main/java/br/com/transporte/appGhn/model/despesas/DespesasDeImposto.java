package br.com.transporte.appGhn.model.despesas;

import androidx.room.Entity;

import br.com.transporte.appGhn.model.abstracts.Despesas;
import br.com.transporte.appGhn.model.enums.TipoDeImposto;

@Entity
public class DespesasDeImposto extends Despesas {
    private String nome;

    private TipoDeImposto tipo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoDeImposto getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeImposto tipo) {
        this.tipo = tipo;
    }
}

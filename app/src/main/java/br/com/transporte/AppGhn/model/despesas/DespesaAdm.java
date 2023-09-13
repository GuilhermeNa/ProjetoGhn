package br.com.transporte.AppGhn.model.despesas;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Despesas;

@Entity
public class DespesaAdm extends Despesas {
    private String descricao;

    @Ignore
    public DespesaAdm(LocalDate data, BigDecimal valor, String descricao, Long refPlacaCavalo) {
        super.setData(data);
        super.setValorDespesa(valor);
        this.descricao = descricao;
        super.setRefCavaloId(refPlacaCavalo);
    }

    public DespesaAdm() {

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}

package br.com.transporte.appGhn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.math.BigDecimal;

import br.com.transporte.appGhn.model.abstracts.Frota;

@Entity(foreignKeys =
@ForeignKey(
        entity = Motorista.class,
        parentColumns = "id",
        childColumns = "refMotoristaId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.SET_NULL
))
public class Cavalo extends Frota {
    private String versao;
    private BigDecimal comissaoBase;
    private boolean valido;
    private Long refMotoristaId;

    public Cavalo() {
    }

    //---------------------------------- Getters and Setters ----------------------------------------------

    public BigDecimal getComissaoBase() {
        return comissaoBase;
    }

    public void setComissaoBase(BigDecimal comissaoBase) {
        this.comissaoBase = comissaoBase;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public Long getRefMotoristaId() {
        return refMotoristaId;
    }

    public void setRefMotoristaId(Long refMotoristaId) {
        this.refMotoristaId = refMotoristaId;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

}

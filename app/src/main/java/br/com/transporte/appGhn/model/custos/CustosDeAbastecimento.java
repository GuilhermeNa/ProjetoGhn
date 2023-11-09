package br.com.transporte.appGhn.model.custos;

import androidx.room.Entity;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.transporte.appGhn.model.abstracts.Custos;
import br.com.transporte.appGhn.model.enums.TipoAbastecimento;

@Entity
public class CustosDeAbastecimento extends Custos implements Serializable {

    private boolean flagAbastecimentoTotal;
    private TipoAbastecimento tipo;
    private BigDecimal quantidadeLitros, marcacaoKm, valorLitro;
    private String posto;

    public CustosDeAbastecimento() {
    }

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public BigDecimal getMarcacaoKm() {
        return marcacaoKm;
    }

    public void setMarcacaoKm(BigDecimal marcacaoKm) {
        this.marcacaoKm = marcacaoKm;
    }

    public BigDecimal getQuantidadeLitros() {
        return quantidadeLitros;
    }

    public void setQuantidadeLitros(BigDecimal quantidadeLitros) {
        this.quantidadeLitros = quantidadeLitros;
    }

    public BigDecimal getValorLitro() {
        return valorLitro;
    }

    public void setValorLitro(BigDecimal valorLitro) {
        this.valorLitro = valorLitro;
    }

    public TipoAbastecimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAbastecimento tipo) {
        this.tipo = tipo;
    }

    public boolean isFlagAbastecimentoTotal() {
        return flagAbastecimentoTotal;
    }

    public void setFlagAbastecimentoTotal(boolean flagAbastecimentoTotal) {
        this.flagAbastecimentoTotal = flagAbastecimentoTotal;
    }
}


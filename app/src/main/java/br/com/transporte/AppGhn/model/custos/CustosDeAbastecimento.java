package br.com.transporte.AppGhn.model.custos;

import androidx.room.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.Custos;
import br.com.transporte.AppGhn.model.enums.TipoAbastecimento;

@Entity
public class CustosDeAbastecimento extends Custos implements Serializable {

    private boolean flagAbastecimentoTotal;
    private TipoAbastecimento tipo;
    private BigDecimal quantidadeLitros, marcacaoKm, valorLitro;
    private String posto;

    public CustosDeAbastecimento(LocalDate data, String posto, BigDecimal marcacaoKm, BigDecimal quantidadeLitros,
                                 BigDecimal valorLitro, BigDecimal totalAbastecimento, TipoAbastecimento tipo, Long refCavalo, boolean flagAbastecimentoTotal) {
        super.setData(data);
        this.posto = posto;
        this.marcacaoKm = marcacaoKm;
        this.quantidadeLitros = quantidadeLitros;
        this.valorLitro = valorLitro;
        super.setValorCusto(totalAbastecimento);
        this.tipo = tipo;
        super.setRefCavaloId(refCavalo);
        this.flagAbastecimentoTotal = flagAbastecimentoTotal;
    }

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


package br.com.transporte.AppGhn.model.abstracts;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public abstract class Parcela {
    private int numeroDaParcela, refSeguro, id, refCavalo;
    private LocalDate data;
    private BigDecimal valor;
    private boolean valido;
    private boolean paga;
    private TipoDespesa tipoDespesa;

    public int getNumeroDaParcela() {
        return numeroDaParcela;
    }

    public void setNumeroDaParcela(int numeroDaParcela) {
        this.numeroDaParcela = numeroDaParcela;
    }

    public int getRefSeguro() {
        return refSeguro;
    }

    public void setRefSeguro(int refChaveIdEstrangeira) {
        this.refSeguro = refChaveIdEstrangeira;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }

    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public int getRefCavalo() {
        return refCavalo;
    }

    public void setRefCavalo(int refCavalo) {
        this.refCavalo = refCavalo;
    }
}

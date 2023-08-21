package br.com.transporte.AppGhn.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.exception.ValorInvalidoException;

public class Adiantamento implements Serializable {
    private LocalDate data;
    private BigDecimal valorTotal, saldoRestituido, ultimoValorAbatido;
    private String descricao;
    private int refCavalo, refMotorista, id;
    private boolean adiantamentoJaFoiPago;

    public Adiantamento(LocalDate data, BigDecimal valorTotal, String descricao, int refCavalo, int refMotorista) {
        this.data = data;
        this.valorTotal = valorTotal;
        this.descricao = descricao;
        this.refCavalo = refCavalo;
        this.refMotorista = refMotorista;
    }

    public Adiantamento() {

    }

    // ------------------------------ Getters e Setters --------------------------------------------

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getRefCavalo() {
        return refCavalo;
    }

    public void setRefCavalo(int refCavalo) {
        this.refCavalo = refCavalo;
    }

    public int getRefMotorista() {
        return refMotorista;
    }

    public void setRefMotorista(int refMotorista) {
        this.refMotorista = refMotorista;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getSaldoRestituido() {
        return saldoRestituido;
    }

    public void setSaldoRestituido(BigDecimal saldoRestituido) {
        this.saldoRestituido = saldoRestituido;
    }

    public void setAdiantamentoJaFoiPago(boolean adiantamentoJaFoiPago) {
        this.adiantamentoJaFoiPago = adiantamentoJaFoiPago;
    }

    public boolean isAdiantamentoJaFoiPago() {
        return adiantamentoJaFoiPago;
    }

    public BigDecimal getUltimoValorAbatido() {
        return ultimoValorAbatido;
    }

    public void setUltimoValorAbatido(BigDecimal ultimoValorAbatido) {
        this.ultimoValorAbatido = ultimoValorAbatido;
    }

    // ------------------------------ Outros Metodos -----------------------------------------------

    public void restituirValorPagoComoAdiantamento(BigDecimal valorARestituir) throws ValorInvalidoException {
        BigDecimal previsaoFuturaDeSaldoRestituidoAposNovaBaixa = this.saldoRestituido.add(valorARestituir);
        int compare = valorTotal.compareTo(previsaoFuturaDeSaldoRestituidoAposNovaBaixa);
        boolean valorARestituirInsuficienteParaFechamento = compare >= 1;
        boolean valoresBatemParaFechamento = compare == 0;

        if (valorARestituirInsuficienteParaFechamento) {
            realizaRestituicao(valorARestituir);

        } else if (valoresBatemParaFechamento) {
            realizaRestituicao(valorARestituir);
            setAdiantamentoJaFoiPago(true);

        } else {
            throw new ValorInvalidoException("Restituição superior ao que foi pago em forma de adiantamento");
        }
    }

    private void realizaRestituicao(BigDecimal valorARestituir) {
        this.saldoRestituido = saldoRestituido.add(valorARestituir);
    }

    public BigDecimal restaReembolsar() {
        return this.valorTotal.subtract(this.saldoRestituido);
    }


}

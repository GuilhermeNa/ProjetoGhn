package br.com.transporte.AppGhn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.exception.ValorInvalidoException;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Cavalo.class,
                parentColumns = "id",
                childColumns = "refCavaloId",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Motorista.class,
                parentColumns = "id",
                childColumns = "refMotoristaId",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        )
})
public class Adiantamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long refCavaloId;
    private Long refMotoristaId;

    private LocalDate data;
    private BigDecimal valorTotal, saldoRestituido, ultimoValorAbatido;
    private String descricao;
    private boolean adiantamentoJaFoiPago;

    @Ignore
    public Adiantamento(LocalDate data, BigDecimal valorTotal, String descricao, Long refCavalo, Long refMotorista) {
        this.data = data;
        this.valorTotal = valorTotal;
        this.descricao = descricao;
        this.refCavaloId = refCavalo;
        this.refMotoristaId = refMotorista;
    }

    public Adiantamento() {

    }

    // ------------------------------ Getters e Setters --------------------------------------------


    public boolean isAdiantamentoJaFoiPago() {
        return adiantamentoJaFoiPago;
    }

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

    public Long getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(Long refCavaloId) {
        this.refCavaloId = refCavaloId;
    }

    public Long getRefMotoristaId() {
        return refMotoristaId;
    }

    public void setRefMotoristaId(Long refMotoristaId) {
        this.refMotoristaId = refMotoristaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isAdiantamentoJaFoiDescontado() {
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

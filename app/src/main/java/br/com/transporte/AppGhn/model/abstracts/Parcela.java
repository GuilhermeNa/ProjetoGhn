package br.com.transporte.AppGhn.model.abstracts;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

@Entity(foreignKeys =
@ForeignKey(
        entity = Cavalo.class,
        parentColumns = "id",
        childColumns = "refCavaloId",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
))
public abstract class Parcela {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private Integer refCavaloId;
    private int numeroDaParcela;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getRefCavaloId() {
        return refCavaloId;
    }

    public void setRefCavaloId(int refCavaloId) {
        this.refCavaloId = refCavaloId;
    }
}

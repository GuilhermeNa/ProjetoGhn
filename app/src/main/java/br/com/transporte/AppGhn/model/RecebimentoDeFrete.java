package br.com.transporte.AppGhn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete;

@Entity(foreignKeys =
@ForeignKey(
        entity = Frete.class,
        parentColumns = "id",
        childColumns = "refFreteId",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
))
public class RecebimentoDeFrete {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long refFreteId;

    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private TipoRecebimentoFrete tipoRecebimentoFrete;

    public RecebimentoDeFrete(LocalDate data, BigDecimal valor, String descricao,
                              TipoRecebimentoFrete tipoRecebimentoFrete) {
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
        this.tipoRecebimentoFrete = tipoRecebimentoFrete;
    }

    public RecebimentoDeFrete() {
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoRecebimentoFrete getTipoRecebimentoFrete() {
        return tipoRecebimentoFrete;
    }

    public void setTipoRecebimentoFrete(TipoRecebimentoFrete tipoRecebimentoFrete) {
        this.tipoRecebimentoFrete = tipoRecebimentoFrete;
    }

    public long getRefFreteId() {
        return refFreteId;
    }

    public void setRefFreteId(long refFreteId) {
        this.refFreteId = refFreteId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

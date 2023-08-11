package br.com.transporte.AppGhn.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete;

public class RecebimentoDeFrete {
    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private TipoRecebimentoFrete tipoRecebimentoFrete;
    private int refFrete;
    private int id;

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

    public int getRefFrete() {
        return refFrete;
    }

    public void setRefFrete(int refFrete) {
        this.refFrete = refFrete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

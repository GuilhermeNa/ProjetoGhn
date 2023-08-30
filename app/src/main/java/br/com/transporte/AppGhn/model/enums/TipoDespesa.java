package br.com.transporte.AppGhn.model.enums;

public enum TipoDespesa {
    DIRETA("DIRETA"),
    INDIRETA("INDIRETA");

    private final String descricao;

    TipoDespesa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao(){return descricao;}

}

package br.com.transporte.AppGhn.model.enums;

public enum TipoAbastecimento {
    PARCIAL("PARCIAL"),
    TOTAL("TOTAL");

    private final String descricao;

    TipoAbastecimento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao(){return descricao;}

}

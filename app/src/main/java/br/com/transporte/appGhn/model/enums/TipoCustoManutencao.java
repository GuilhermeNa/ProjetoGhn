package br.com.transporte.appGhn.model.enums;

public enum TipoCustoManutencao {
    PERIODICA("PERIODICA"),
    EXTRAORDINARIA("EXTRAORDINARIA");

    private final String descricao;


    TipoCustoManutencao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

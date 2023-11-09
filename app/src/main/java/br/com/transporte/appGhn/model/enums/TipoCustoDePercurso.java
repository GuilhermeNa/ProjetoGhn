package br.com.transporte.appGhn.model.enums;

public enum TipoCustoDePercurso {
    NAO_REEMBOLSAVEL("NAO_REEMBOLSAVEL"),
    REEMBOLSAVEL_EM_ABERTO("REEMBOLSAVEL_EM_ABERTO"),
    REEMBOLSAVEL_JA_PAGO("REEMBOLSAVEL_JA_PAGO");

    private final String descricao;


    TipoCustoDePercurso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao(){return descricao;}
}

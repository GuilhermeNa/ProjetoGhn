package br.com.transporte.AppGhn.model.enums;

public enum TipoRecebimentoFrete {
    ADIANTAMENTO("ADIANTAMENTO"),
    SALDO("SALDO");

    private final String descricao;

    TipoRecebimentoFrete(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }


}

package br.com.transporte.appGhn.model.enums;

public enum TipoMeses {
    MES_DEFAULT(0, "Default"),
    JANEIRO(1,"Janeiro"),
    FEVEREIRO(2,"Fevereiro"),
    MARCO(3,"Março"),
    ABRIL(4,"Abril"),
    MAIO(5,"Maio"),
    JUNHO(6,"Junho"),
    JULHO(7,"Julho"),
    AGOSTO(8,"Agosto"),
    SETEMBRO(9,"Setembro"),
    OUTUBRO(10,"Outubro"),
    NOVREMBO(11,"Novembro"),
    DEZEMBRO(12,"Dezembro");

    private final String descricao;
    private final int ref;

    TipoMeses(int ref, String descricao) {
        this.descricao = descricao;
        this.ref = ref;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getRef() {
        return ref;
    }
}

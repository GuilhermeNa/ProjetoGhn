package br.com.transporte.AppGhn.model.enums;

public enum TipoDeRequisicao {
    FRETE_BRUTO("Frete bruto"),
    FRETE_LIQUIDO("Frete a receber"),
    COMISSAO("Comissões pagas"),

    KM_RODADO("Km rodado"),
    LITRAGEM("Litragem abastecida"),


    CUSTOS_PERCURSO("Custos de Percurso"),
    CUSTOS_ABASTECIMENTO("Custos de abastecimento"),
    CUSTOS_MANUTENCAO("Custos de manutenção"),

    DESPESAS_ADM("Despesas administrativas"),
    DESPESAS_IMPOSTOS("Despesas de impostos"),
    DESPESA_CERTIFICADOS("Despesas de certificados"),

    DESPESA_SEGUROS_DIRETOS("Despesas de Seguros Diretos"),
    DESPESA_SEGUROS_INDIRETOS("Despesas de Seguros Indiretos"),

    LUCRO_LIQUIDO("Lucro Líquido"),

    MEDIA("Média"),
    LUCRO_PERCENTUAL( "Lucro Percentual")

    ;




    private final String descricao;

    TipoDeRequisicao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
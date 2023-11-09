package br.com.transporte.appGhn.model.enums;

public enum TipoDeRequisicao {
    FRETE_BRUTO("Frete bruto"),
    FRETE_LIQUIDO("Frete a receber"),
    COMISSAO("Comissões pagas"),
    LUCRO_LIQUIDO("Lucro Líquido"),

    LITRAGEM("Litragem abastecida"),

    CUSTOS_PERCURSO("Custos de Percurso"),
    CUSTOS_ABASTECIMENTO("Custos de abastecimento"),
    CUSTOS_MANUTENCAO("Custos de manutenção"),

    DESPESAS_ADM("Despesas administrativas"),
    DESPESAS_IMPOSTOS("Despesas de impostos"),
    DESPESA_CERTIFICADOS("Despesas de certificados"),
    DESPESA_SEGURO_FROTA("Despesas de Seguros Diretos"),
    DESPESA_SEGURO_VIDA("Despesas de Seguros Indiretos");

    private final String descricao;

    TipoDeRequisicao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

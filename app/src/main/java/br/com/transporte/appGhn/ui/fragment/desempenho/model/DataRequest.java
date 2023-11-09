package br.com.transporte.appGhn.ui.fragment.desempenho.model;

import br.com.transporte.appGhn.model.enums.TipoDeRequisicao;

public class DataRequest {
    private TipoDeRequisicao tipo;
    private boolean exibirRateio;
    private Long cavaloId;
    private int ano;
    private int mes;

    public DataRequest(TipoDeRequisicao tipo, Long cavaloId, int ano, int mes, boolean exibirRateio) {
        this.tipo = tipo;
        this.cavaloId = cavaloId;
        this.ano = ano;
        this.mes = mes;
        this.exibirRateio = exibirRateio;
    }

    public TipoDeRequisicao getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeRequisicao tipo) {
        this.tipo = tipo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public boolean isExibirRateio() {
        return exibirRateio;
    }

    public void setExibirRateio(boolean exibirRateio) {
        this.exibirRateio = exibirRateio;
    }

    public Long getCavaloId() {
        return cavaloId;
    }

    public void setCavaloId(Long cavaloId) {
        this.cavaloId = cavaloId;
    }
}

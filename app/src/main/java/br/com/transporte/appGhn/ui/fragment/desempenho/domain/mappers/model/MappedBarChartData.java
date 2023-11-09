package br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model;

import java.math.BigDecimal;
import java.util.List;

public class MappedBarChartData {
    private List<BigDecimal> listaDeValores;
    private List<String> listaDeMeses;

    public List<BigDecimal> getListaDeValores() {
        return listaDeValores;
    }

    public void setListaDeValores(List<BigDecimal> listaDeValores) {
        this.listaDeValores = listaDeValores;
    }

    public List<String> getListaDeMeses() {
        return listaDeMeses;
    }

    public void setListaDeMeses(List<String> listaDeMeses) {
        this.listaDeMeses = listaDeMeses;
    }
}

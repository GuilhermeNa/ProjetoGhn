package br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesEmAberto.extensions;

import java.math.BigDecimal;

public class Resource {
    private BigDecimal comissaoTotalDevidaAosMotoristas;
    private BigDecimal comissaoTotalQueJaFoiPagaAosMotoristas;
    private BigDecimal comissaoEmAbertoASerPaga;
    private BigDecimal evolucaoProgressBar;

    public BigDecimal getComissaoTotalDevidaAosMotoristas() {
        return comissaoTotalDevidaAosMotoristas;
    }

    public void setComissaoTotalDevidaAosMotoristas(BigDecimal comissaoTotalDevidaAosMotoristas) {
        this.comissaoTotalDevidaAosMotoristas = comissaoTotalDevidaAosMotoristas;
    }

    public BigDecimal getComissaoTotalQueJaFoiPagaAosMotoristas() {
        return comissaoTotalQueJaFoiPagaAosMotoristas;
    }

    public void setComissaoTotalQueJaFoiPagaAosMotoristas(BigDecimal comissaoTotalQueJaFoiPagaAosMotoristas) {
        this.comissaoTotalQueJaFoiPagaAosMotoristas = comissaoTotalQueJaFoiPagaAosMotoristas;
    }

    public BigDecimal getComissaoEmAbertoASerPaga() {
        return comissaoEmAbertoASerPaga;
    }

    public void setComissaoEmAbertoASerPaga(BigDecimal comissaoEmAbertoASerPaga) {
        this.comissaoEmAbertoASerPaga = comissaoEmAbertoASerPaga;
    }

    public BigDecimal getEvolucaoProgressBar() {
        return evolucaoProgressBar;
    }

    public void setEvolucaoProgressBar(BigDecimal evolucaoProgressBar) {
        this.evolucaoProgressBar = evolucaoProgressBar;
    }
}

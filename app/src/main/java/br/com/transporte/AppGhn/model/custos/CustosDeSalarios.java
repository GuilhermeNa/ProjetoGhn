package br.com.transporte.AppGhn.model.custos;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.model.abstracts.Custos;

public class CustosDeSalarios extends Custos {
    private BigDecimal comissao;
    private BigDecimal reembolso;
    private BigDecimal descontos;
    private int refMotorista;

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public BigDecimal getReembolso() {
        return reembolso;
    }

    public void setReembolso(BigDecimal reembolso) {
        this.reembolso = reembolso;
    }

    public BigDecimal getDescontos() {
        return descontos;
    }

    public void setDescontos(BigDecimal descontos) {
        this.descontos = descontos;
    }
}

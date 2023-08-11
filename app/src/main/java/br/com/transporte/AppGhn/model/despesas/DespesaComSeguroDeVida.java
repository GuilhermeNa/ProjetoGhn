package br.com.transporte.AppGhn.model.despesas;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;

public class DespesaComSeguroDeVida extends DespesaComSeguro {
    private BigDecimal coberturaSocios, coberturaMotoristas, coberturaOutrosFuncionarios;

    public DespesaComSeguroDeVida(LocalDate dataInicial, LocalDate dataFinal,LocalDate dataPrimeiraParcela, BigDecimal valorParcela, BigDecimal valorDespesa, String companhia,
                                  boolean valido, int parcelas, int nContrato, int refCavalo, BigDecimal coberturaSocios,
                                  BigDecimal coberturaMotoristas, BigDecimal coberturaOutrosFuncionarios) {
        super();
        super.setDataInicial(dataInicial);
        super.setDataFinal(dataFinal);
        super.setDataPrimeiraParcela(dataPrimeiraParcela);
        super.setValorParcela(valorParcela);
        super.setValorDespesa(valorDespesa);
        super.setCompanhia(companhia);
        super.setValido(valido);
        super.setParcelas(parcelas);
        super.setnContrato(nContrato);
        super.setRefCavalo(refCavalo);
        this.coberturaSocios = coberturaSocios;
        this.coberturaMotoristas = coberturaMotoristas;
        this.coberturaOutrosFuncionarios = coberturaOutrosFuncionarios;

    }

    public DespesaComSeguroDeVida() {

    }


    public BigDecimal getCoberturaSocios() {
        return coberturaSocios;
    }

    public void setCoberturaSocios(BigDecimal coberturaSocios) {
        this.coberturaSocios = coberturaSocios;
    }

    public BigDecimal getCoberturaMotoristas() {
        return coberturaMotoristas;
    }

    public void setCoberturaMotoristas(BigDecimal coberturaMotoristas) {
        this.coberturaMotoristas = coberturaMotoristas;
    }

    public BigDecimal getCoberturaOutrosFuncionarios() {
        return coberturaOutrosFuncionarios;
    }

    public void setCoberturaOutrosFuncionarios(BigDecimal coberturaOutrosFuncionarios) {
        this.coberturaOutrosFuncionarios = coberturaOutrosFuncionarios;
    }

}

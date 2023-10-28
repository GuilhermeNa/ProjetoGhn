package br.com.transporte.AppGhn.model.despesas;

import androidx.room.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.model.parcelas.factory.ParcelamentoInterface;
import br.com.transporte.AppGhn.model.parcelas.factory.ParcelasFactory;

@Entity
public class DespesaComSeguroDeVida extends DespesaComSeguro implements ParcelamentoInterface<Parcela_seguroVida> {
    private BigDecimal coberturaSocios, coberturaMotoristas, coberturaOutrosFuncionarios;

    public DespesaComSeguroDeVida(LocalDate dataInicial, LocalDate dataFinal, LocalDate dataPrimeiraParcela, BigDecimal valorParcela, BigDecimal valorDespesa, String companhia,
                                  boolean valido, int parcelas, int nContrato, Long refCavalo, BigDecimal coberturaSocios,
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
        super.setNContrato(nContrato);
        super.setRefCavaloId(refCavalo);
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

    @Override
    public List<Parcela_seguroVida> criaParcelas(Long chaveEstrangeira) {
        final List<Parcela_seguroVida> listaDeParcelas = new ArrayList<>();
        int quantidadeDeParcelas = ParcelasFactory
                .configQuantidadeDeParcelas(this.getParcelas());

        Parcela_seguroVida parcelaDeSeguro;
        for (int i = 0; i < quantidadeDeParcelas; i++) {
            parcelaDeSeguro = new Parcela_seguroVida();
            parcelaDeSeguro.setData(this.getDataPrimeiraParcela().plusMonths(i));
            parcelaDeSeguro.setTipoDespesa(this.getTipoDespesa());
            parcelaDeSeguro.setRefCavaloId(0L);
            parcelaDeSeguro.setValor(this.getValorParcela());
            parcelaDeSeguro.setRefSeguro(chaveEstrangeira);
            parcelaDeSeguro.setNumeroDaParcela(i + 1);
            parcelaDeSeguro.setValido(true);
            parcelaDeSeguro.setPaga(false);

            listaDeParcelas.add(parcelaDeSeguro);
        }
        return listaDeParcelas;
    }
}


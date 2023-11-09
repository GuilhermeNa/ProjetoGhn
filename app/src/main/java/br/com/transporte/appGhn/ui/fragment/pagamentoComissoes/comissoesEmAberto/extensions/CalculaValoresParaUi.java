package br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesEmAberto.extensions;

import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.ONE_HUNDRED;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.util.CalculoUtil;

public class CalculaValoresParaUi {
    private final List<Frete> dataSet;

    public CalculaValoresParaUi(List<Frete> dataSet) {
        this.dataSet = dataSet;
    }

    public Resource getResource() {
        final Resource resource = new Resource();

        BigDecimal comissaoTotalDevidaAosMotoristas = CalculoUtil.somaComissao(dataSet);
        resource.setComissaoTotalDevidaAosMotoristas(comissaoTotalDevidaAosMotoristas);

        BigDecimal comissaoTotalQueJaFoiPagaAosMotoristas = CalculoUtil.somaComissaoPorStatus(dataSet, true);
        resource.setComissaoTotalQueJaFoiPagaAosMotoristas(comissaoTotalQueJaFoiPagaAosMotoristas);

        BigDecimal comissaoEmAbertoASerPaga = comissaoTotalDevidaAosMotoristas.subtract(comissaoTotalQueJaFoiPagaAosMotoristas);
        resource.setComissaoEmAbertoASerPaga(comissaoEmAbertoASerPaga);

        BigDecimal evolucaoProgressBar;
        try {
            evolucaoProgressBar = comissaoTotalQueJaFoiPagaAosMotoristas.divide(comissaoTotalDevidaAosMotoristas, RoundingMode.HALF_EVEN).multiply(new BigDecimal(ONE_HUNDRED));
        } catch (ArithmeticException e) {
            evolucaoProgressBar = BigDecimal.ZERO;
        }
        resource.setEvolucaoProgressBar(evolucaoProgressBar);

        return resource;
    }


}

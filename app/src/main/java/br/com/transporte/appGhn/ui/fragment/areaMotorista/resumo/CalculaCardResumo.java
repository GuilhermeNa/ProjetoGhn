package br.com.transporte.appGhn.ui.fragment.areaMotorista.resumo;

import static br.com.transporte.appGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.util.CalculoUtil;

public class CalculaCardResumo {

    public interface CalculaResumoCallback {
        void comissaoReceber(BigDecimal valor);

        void reembolso(BigDecimal valor);

        void adiantamento(BigDecimal valor);
    }

    public void run(
            final List<CustosDePercurso> dataSet,
            final List<Adiantamento> dataSetAdiantamento,
            @NonNull final BigDecimal comissaoTotalAoMotorista,
            final BigDecimal comissaoJaPaga,
            @NonNull final CalculaResumoCallback callback
    ) {
        BigDecimal comissaAReceber = comissaoTotalAoMotorista.subtract(comissaoJaPaga);
        BigDecimal reembolsoEmAberto = ui_reembolso(dataSet);
        BigDecimal adiantamentoDescontar = ui_adiantamento(dataSetAdiantamento);
        callback.comissaoReceber(comissaAReceber);
        callback.reembolso(reembolsoEmAberto);
        callback.adiantamento(adiantamentoDescontar);
    }

    private BigDecimal ui_reembolso(List<CustosDePercurso> dataSet) {
        List<CustosDePercurso> listaCustosEmAberto = FiltraCustosPercurso.listaPorTipo(dataSet, REEMBOLSAVEL_EM_ABERTO);
        return CalculoUtil.somaCustosDePercurso(listaCustosEmAberto);
    }

    @Nullable
    private BigDecimal ui_adiantamento(List<Adiantamento> dataSet) {
        return CalculoUtil.somaAdiantamentoPorStatus(dataSet, false);
    }

}

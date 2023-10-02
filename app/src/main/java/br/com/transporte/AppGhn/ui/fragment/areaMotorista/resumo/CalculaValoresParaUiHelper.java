package br.com.transporte.AppGhn.ui.fragment.areaMotorista.resumo;

import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.NAO_REEMBOLSAVEL;
import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class CalculaValoresParaUiHelper {
    private List<CustosDeAbastecimento> dataSetAbastecimentoFiltrado;
    private List<CustosDeAbastecimento> dataSetAbastecimentoTodos;
    private List<Adiantamento> dataSetAdiantamento;
    private List<CustosDePercurso> dataSetCusto;
    private List<Frete> dataSetFrete;

    public interface CalculadoraCallback {
        void calculoFinalizado(ResourceValores resource);
    }

    //----------------------------------------------------------------------------------------------

    public void atualizaDataBase(
            final List<CustosDeAbastecimento> dataSetAbastecimentoTodos,
            final List<CustosDeAbastecimento> dataSetAbastecimentoFiltrado,
            final List<Adiantamento> dataSetAdiantamento,
            final List<CustosDePercurso> dataSetCusto,
            final List<Frete> dataSetFrete
    ) {
        this.dataSetAbastecimentoTodos = dataSetAbastecimentoTodos;
        this.dataSetAbastecimentoFiltrado = dataSetAbastecimentoFiltrado;
        this.dataSetAdiantamento = dataSetAdiantamento;
        this.dataSetCusto = dataSetCusto;
        this.dataSetFrete = dataSetFrete;
    }

    public void geraValores(@NonNull final CalculadoraCallback callback) {
        ResourceValores resource = new ResourceValores();

        BigDecimal comissaoTotalAoMotorista = CalculoUtil.somaComissao(dataSetFrete);
        resource.setComissaoTotalAoMotorista(comissaoTotalAoMotorista);

        BigDecimal comissaoJaPaga = CalculoUtil.somaComissaoPorStatus(dataSetFrete, true);
        resource.setComissaoJaPaga(comissaoJaPaga);

        BigDecimal adiantamentoADescontar = CalculoUtil.somaAdiantamentoPorStatus(dataSetAdiantamento, false);
        resource.setAdiantamentoADescontar(adiantamentoADescontar);

        BigDecimal custoEmAberto = calcCustoEmAberto(dataSetCusto);
        resource.setCustoEmAberto(custoEmAberto);

        BigDecimal comissaoAReceber = comissaoTotalAoMotorista.subtract(comissaoJaPaga);
        resource.setComissaAReceber(comissaoAReceber);

        BigDecimal valorAReceber =
                comissaoAReceber
                        .add(custoEmAberto)
                        .subtract(adiantamentoADescontar);
        resource.setValorAReceber(valorAReceber);

        BigDecimal somaFreteBruto = CalculoUtil.somaFreteBruto(dataSetFrete);
        resource.setSomaFreteBruto(somaFreteBruto);

        BigDecimal somaDescontosNoFrete = CalculoUtil.somaDescontoNoFrete(dataSetFrete);
        resource.setSomaDescontosNoFrete(somaDescontosNoFrete);

        BigDecimal somaFreteLiquidoAReceber = CalculoUtil.somaFreteLiquido(dataSetFrete);
        resource.setSomaFreteLiquidoAReceber(somaFreteLiquidoAReceber);

        BigDecimal abastecimentoTotal = CalculoUtil.somaCustosDeAbastecimento(dataSetAbastecimentoFiltrado);
        resource.setAbastecimentoTotal(abastecimentoTotal);

        BigDecimal litragemTotal = CalculoUtil.somaLitragemTotal(dataSetAbastecimentoFiltrado);
        resource.setLitragemTotal(litragemTotal);

        BigDecimal kmPercorrido = calcKmPercorrido(dataSetAbastecimentoFiltrado, dataSetAbastecimentoTodos);
        resource.setKmPercorrido(kmPercorrido);

        BigDecimal custosPercursoTotal = CalculoUtil.somaCustosDePercurso(dataSetCusto);
        resource.setCustosPercursoTotal(custosPercursoTotal);

        BigDecimal custosDePercursoSemReembolso = calcCustoPercursoSemReembolso(dataSetCusto);
        resource.setCustosDePercursoSemReembolso(custosDePercursoSemReembolso);

        callback.calculoFinalizado(resource);
    }

    private BigDecimal calcCustoPercursoSemReembolso(List<CustosDePercurso> dataSetCusto) {
        List<CustosDePercurso> listaCustosNaoReembolsavel = FiltraCustosPercurso.listaPorTipo(dataSetCusto, NAO_REEMBOLSAVEL);
        return CalculoUtil.somaCustosDePercurso(listaCustosNaoReembolsavel);
    }

    private BigDecimal calcCustoEmAberto(final List<CustosDePercurso> dataSetCusto) {
        List<CustosDePercurso> dataSet = FiltraCustosPercurso.listaPorTipo(dataSetCusto, REEMBOLSAVEL_EM_ABERTO);
        return CalculoUtil.somaCustosDePercurso(dataSet);
    }

    private BigDecimal calcKmPercorrido(
            final List<CustosDeAbastecimento> dataSetAbastecimentoFiltrado,
            final List<CustosDeAbastecimento> dataSetAbastecimentoTodos
    ) {
        BigDecimal kmPercorrido;
        try{
            kmPercorrido = getKmPercorrido(dataSetAbastecimentoFiltrado, dataSetAbastecimentoTodos);
        } catch (IndexOutOfBoundsException e){
            kmPercorrido = BigDecimal.ZERO;
        }
        return kmPercorrido;
    }
    
    private BigDecimal getKmPercorrido(
            final List<CustosDeAbastecimento> dataSetAbastecimentoFiltrado,
            final List<CustosDeAbastecimento> dataSetAbastecimentoTodos
    ) {
        List<CustosDeAbastecimento> listaTodos = dataSetAbastecimentoTodos;
        List<CustosDeAbastecimento> listaFiltrada = dataSetAbastecimentoFiltrado;

        Comparator<CustosDeAbastecimento> comparing = Comparator.comparing(CustosDeAbastecimento::getMarcacaoKm);
        listaTodos.sort(comparing);
        listaFiltrada.sort(comparing);

        CustosDeAbastecimento dMenos1;
        CustosDeAbastecimento d0;
        CustosDeAbastecimento dMais1;

        if (listaFiltrada.size() == 0) {
            throw new IndexOutOfBoundsException("Lista filtrada não encontrada");
        } else if (listaTodos.size() == 0) {
            throw new IndexOutOfBoundsException("Lista total não encontrada");
        }
        d0 = listaFiltrada.get(0);
        dMais1 = listaFiltrada.get(listaFiltrada.size() - 1);
        int d0PosicaoGeral = listaTodos.indexOf(d0);

        try {
            dMenos1 = listaTodos.get(d0PosicaoGeral - 1);
        } catch (IndexOutOfBoundsException e) {
            dMenos1 = listaTodos.get(d0PosicaoGeral);
        }
        return dMais1.getMarcacaoKm().subtract(dMenos1.getMarcacaoKm());
    }




}

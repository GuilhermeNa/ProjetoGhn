package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.NAO_REEMBOLSAVEL;
import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaResumoBinding;
import br.com.transporte.AppGhn.filtros.FiltraAdiantamento;
import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class AreaMotoristaResumoFragment extends Fragment implements DateRangePickerUtil.CallbackDatePicker {
    public static final String ZERO = "0.00";
    public static final String KEY_ACTION_ADAPTER = "key_action_adapter";
    private FragmentAreaMotoristaResumoBinding binding;
    private TextView totalAReceberTxt, acumuladoTxt, jaRecebidoTxt, emAbertoTxt, reembolsoDespesasTxt, adiantamentoTxt,
            dataInicialTxt, dataFinalTxt, freteBrutoTxt, descontosFreteTxt, freteLiquidoTxt, abastecimentoTotalTxt,
            litragemTotalTxt, kmPercorridoTxt, despesaTotalTxt, despesaReembolsavelTxt, despesaNaoReembolsavelTxt;
    private CustosDeAbastecimentoDAO abastecimentoDao;
    private CustosDePercursoDAO custoDao;
    private AdiantamentoDAO adiantamentoDao;
    private FreteDAO freteDao;
    private List<CustosDeAbastecimento> listaDeAbastecimentos;
    private List<Adiantamento> listaAdiantamentos;
    private List<CustosDePercurso> listaDeCustos;
    private List<Frete> listaDeFretes;
    private LocalDate dataInicial, dataFinal;
    private LinearLayout dataLayout;
    private Cavalo cavalo;
    private DateRangePickerUtil dateRange;
    private BigDecimal custoEmAberto;

    private boolean atualizacaoSolicitadaPelaActivity = false;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());

        abastecimentoDao = new CustosDeAbastecimentoDAO();
        adiantamentoDao = new AdiantamentoDAO();
        custoDao = new CustosDePercursoDAO();
        freteDao = new FreteDAO();
        configuracaoInicialDateRange();
        cavalo = recebeCavaloEscolhidoParaVisualizacao(dataBase);
        atualizaListas();
    }

    private Cavalo recebeCavaloEscolhidoParaVisualizacao(@NonNull GhnDataBase dataBase) {
        RoomCavaloDao cavaloDao = dataBase.getRoomCavaloDao();
        int cavaloId = requireArguments().getInt(CHAVE_ID_CAVALO);
        return cavalo = cavaloDao.localizaPeloId(cavaloId);
    }

    private void configuracaoInicialDateRange() {
        dateRange = new DateRangePickerUtil(getParentFragmentManager());
        dataInicial = dateRange.getDataInicialEmLocalDate();
        dataFinal = dateRange.getDataFinalEmLocalDate();
    }

    //----------------------------------
    // -> Configura Ui                ||
    //----------------------------------

    private void atualizaListas() {
        listaDeFretes = getListaDeFretes(cavalo);
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
        listaDeCustos = getListaDeCustos(cavalo);
        listaAdiantamentos = getListaAdiantamentos(cavalo);
    }

    private List<Adiantamento> getListaAdiantamentos(@NonNull Cavalo cavalo) {
        List<Adiantamento> dataSet = FiltraAdiantamento.listaPorCavaloId(adiantamentoDao.listaTodos(), cavalo.getId());
        return FiltraAdiantamento.listaPorStatus(dataSet, false);
    }

    private List<CustosDePercurso> getListaDeCustos(@NonNull Cavalo cavalo) {
        List<CustosDePercurso> dataSet = FiltraCustosPercurso.listaPorCavaloId(custoDao.listaTodos(), cavalo.getId());
        return FiltraCustosPercurso.listaPorData(dataSet, dataInicial, dataFinal);
    }

    private List<CustosDeAbastecimento> getListaDeAbastecimentos(@NonNull Cavalo cavalo) {
        List<CustosDeAbastecimento> dataSet = FiltraCustosAbastecimento.listaPorCavaloId(abastecimentoDao.listaTodos(), cavalo.getId());
        return FiltraCustosAbastecimento.listaPorData(dataSet, dataInicial, dataFinal);
    }

    private List<Frete> getListaDeFretes(@NonNull Cavalo cavalo) {
        List<Frete> dataSet = FiltraFrete.listaPorCavaloId(freteDao.listaTodos(), cavalo.getId());
        return FiltraFrete.listaPorData(dataSet, dataInicial, dataFinal);
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAreaMotoristaResumoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          onViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraDateRangePicker();
        configuraUi();
    }

    private void configuraDateRangePicker() {
        dateRange.build(dataLayout);
        dateRange.setCallbackDatePicker(this);
    }

    private void inicializaCampos() {
        totalAReceberTxt = binding.fragResumoValorTotal;
        acumuladoTxt = binding.fragResumoValorAcumulado;
        jaRecebidoTxt = binding.fragResumoValorJaRecebido;
        emAbertoTxt = binding.fragResumoValorEmAberto;
        reembolsoDespesasTxt = binding.fragResumoValorReembolsoDespesas;
        adiantamentoTxt = binding.fragResumoValorAdiantamento;
        dataLayout = binding.fragResumoMes;
        dataInicialTxt = binding.fragResumoMesDtInicial;
        dataFinalTxt = binding.fragResumoMesDtFinal;
        freteBrutoTxt = binding.fragResumoFreteBrutoVlr;
        descontosFreteTxt = binding.fragResumoFreteDescontosVlr;
        freteLiquidoTxt = binding.fragResumoFreteLiquidoVlr;
        abastecimentoTotalTxt = binding.fragResumoAbastecimentoTotalVlr;
        litragemTotalTxt = binding.fragResumoAbastecimentoLitrosVlr;
        kmPercorridoTxt = binding.fragResumoAbastecimentoKmVlr;
        despesaTotalTxt = binding.fragResumoDespesaTotalVlr;
        despesaReembolsavelTxt = binding.fragResumoDespesasReembolsaveisVlr;
        despesaNaoReembolsavelTxt = binding.fragResumoDespesasNaoReembolsaveisVlr;
    }

    //--------------------------------
    // -> Configura Ui              ||
    //--------------------------------

    private void configuraUi() {
        ui_datas();
        ui_configuraComissionamento();
        ui_configuraCardFrete();
        ui_configuraCardAbastecimento();
        ui_configuraCardReembolso();
    }

    private void ui_datas() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    private void ui_configuraCardReembolso() {
        UiCardReembolso.ui_despesaTotal(listaDeCustos, despesaTotalTxt);
        UiCardReembolso.ui_semReembolso(listaDeCustos, despesaNaoReembolsavelTxt);
        UiCardReembolso.ui_reembolso(custoEmAberto, despesaReembolsavelTxt);
    }

    private void ui_configuraCardAbastecimento() {
        UiCardAbastecimento.ui_abastecimentoTotal(listaDeAbastecimentos, abastecimentoTotalTxt);
        UiCardAbastecimento.ui_litragemTotal(listaDeAbastecimentos, litragemTotalTxt);
        UiCardAbastecimento.ui_kmPercorrido(cavalo.getId(), dataInicial, dataFinal, kmPercorridoTxt);
    }

    private void ui_configuraCardFrete() {
        UiCardFrete.ui_FreteBruto(listaDeFretes, freteBrutoTxt);
        UiCardFrete.ui_descontos(listaDeFretes, descontosFreteTxt);
        UiCardFrete.ui_freteLiquido(listaDeFretes, freteLiquidoTxt);
    }

    //---------------------- Configura Informaçoes de comissao

    private void ui_configuraComissionamento() {
        BigDecimal comissaoTotalAoMotorista = ui_acumulado();
        BigDecimal comissaoJaPaga = ui_jaRecebido();

        BigDecimal adiantamentoADescontar = UiCardResumo.ui_adiantamento(listaAdiantamentos, adiantamentoTxt);
        custoEmAberto = UiCardResumo.ui_reembolso(listaDeCustos, reembolsoDespesasTxt);
        BigDecimal comissaAReceber = UiCardResumo.ui_comissaoAReceber(comissaoTotalAoMotorista, comissaoJaPaga, emAbertoTxt);

        totalAReceber(adiantamentoADescontar, custoEmAberto, comissaAReceber);
    }

    private BigDecimal ui_acumulado() {
    //    BigDecimal comissaoTotalAoMotorista = CalculoUtil.somaComissao(listaDeFretes);
    //    acumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoTotalAoMotorista));
    //    return comissaoTotalAoMotorista;
        return null;
    }

    private void totalAReceber(
            BigDecimal adiantamentoADescontar, BigDecimal custoEmAberto, @NonNull BigDecimal comissaAReceber
    ) {
//        BigDecimal valorAReceber = comissaAReceber.add(custoEmAberto).subtract(adiantamentoADescontar);
  //      totalAReceberTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAReceber));
    }

    private BigDecimal ui_jaRecebido() {
   //     BigDecimal comissaoJaPaga = CalculoUtil.somaComissaoPorStatus(listaDeFretes, true);
    //    jaRecebidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoJaPaga));
    //    return comissaoJaPaga;
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (atualizacaoSolicitadaPelaActivity) {
            atualizaUi();
            resetaSolicitacaoDeAtualizacao();
        }
    }

    private void atualizaUi(){
        listaDeFretes = getListaDeFretes(cavalo);
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
        listaDeCustos = getListaDeCustos(cavalo);
        ui_configuraComissionamento();
        ui_configuraCardFrete();
        ui_configuraCardAbastecimento();
        ui_configuraCardReembolso();
    }

    private void resetaSolicitacaoDeAtualizacao() {
        atualizacaoSolicitadaPelaActivity = false;
    }

    //----------------------------------------------------------------------------------------------
    //                                       Metodos publicos                                     ||
    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(){
        Log.d("teste", "solicitacao");
        this.atualizacaoSolicitadaPelaActivity = true;
    }

    //----------------------------------------------------------------------------------------------
    //                                          CallBack                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void selecionaDataComSucesso(LocalDate dataInicial, LocalDate dataFinal) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        atualizaListas();
        configuraUi();
    }

    //----------------------------------------------------------------------------------------------
    //                                          Ui Card Frete                                     ||
    //----------------------------------------------------------------------------------------------

    private static class UiCardFrete {
        private static void ui_freteLiquido(List<Frete> dataSet, @NonNull TextView view) {
         //   BigDecimal somaFreteLiquidoAReceber = CalculoUtil.somaFreteLiquido(dataSet);
         //   view.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteLiquidoAReceber));
        }

        private static void ui_descontos(List<Frete> dataSet, @NonNull TextView view) {
      //      BigDecimal somaDescontosNoFrete = CalculoUtil.somaDescontoNoFrete(dataSet);
      //      view.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaDescontosNoFrete));
        }

        private static void ui_FreteBruto(List<Frete> dataSet, @NonNull TextView view) {
      //      BigDecimal somaFreteBruto = CalculoUtil.somaFreteBruto(dataSet);
      //      view.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteBruto));
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                    Ui Card Abastecimento                                   ||
    //----------------------------------------------------------------------------------------------

    private static class UiCardAbastecimento {
        private static void ui_litragemTotal(List<CustosDeAbastecimento> dataSet, TextView view) {
            BigDecimal litragemTotal = CalculoUtil.somaLitragemTotal(dataSet);
            if (litragemTotal.compareTo(BigDecimal.ZERO) == 0) view.setText(ZERO);
            else view.setText(FormataNumerosUtil.formataNumero(litragemTotal));
        }

        private static void ui_abastecimentoTotal(List<CustosDeAbastecimento> dataSet, @NonNull TextView view) {
            BigDecimal abastecimentoTotal = CalculoUtil.somaCustosDeAbastecimento(dataSet);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(abastecimentoTotal));
        }

        private static void ui_kmPercorrido(int cavaloId, LocalDate dataInicial, LocalDate dataFinal, TextView view) {
            try {
                BigDecimal kmPercorrido = calculaKmPercorrido(cavaloId, dataInicial, dataFinal);
                if (kmPercorrido.compareTo(BigDecimal.ZERO) == 0) {
                    view.setText(ZERO);
                } else {
                    view.setText(FormataNumerosUtil.formataNumero(kmPercorrido));
                }

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                view.setText(ZERO);
            }
        }

        private static BigDecimal calculaKmPercorrido(int cavaloId, LocalDate dataInicial, LocalDate dataFinal) {
            CustosDeAbastecimentoDAO abastecimentoDao = new CustosDeAbastecimentoDAO();
            List<CustosDeAbastecimento> listaTodos = FiltraCustosAbastecimento.listaPorCavaloId(abastecimentoDao.listaTodos(), cavaloId);
            List<CustosDeAbastecimento> listaFiltrada = FiltraCustosAbastecimento.listaPorData(listaTodos, dataInicial, dataFinal);

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
            } catch (ArrayIndexOutOfBoundsException e) {
                dMenos1 = listaTodos.get(d0PosicaoGeral);
            }

            return dMais1.getMarcacaoKm().subtract(dMenos1.getMarcacaoKm());
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                    Ui Card Reembolso                                       ||
    //----------------------------------------------------------------------------------------------

    private static class UiCardReembolso {
        private static void ui_reembolso(BigDecimal custoEmAberto, @NonNull TextView view) {
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(custoEmAberto));
        }

        private static void ui_semReembolso(List<CustosDePercurso> dataSet, @NonNull TextView view) {
            List<CustosDePercurso> listaCustosNaoReembolsavel = FiltraCustosPercurso.listaPorTipo(dataSet, NAO_REEMBOLSAVEL);
            BigDecimal custosDePercursoSemReembolso = CalculoUtil.somaCustosDePercurso(listaCustosNaoReembolsavel);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosDePercursoSemReembolso));
        }

        private static void ui_despesaTotal(List<CustosDePercurso> dataSet, @NonNull TextView view) {
            BigDecimal custosPercursoTotal = CalculoUtil.somaCustosDePercurso(dataSet);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosPercursoTotal));
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                    Ui Card Comissao                                       ||
    //----------------------------------------------------------------------------------------------

    private static class UiCardResumo {
        private static BigDecimal ui_comissaoAReceber(@NonNull BigDecimal comissaoTotalAoMotorista, BigDecimal comissaoJaPaga, @NonNull TextView view) {
          //  BigDecimal comissaAReceber = comissaoTotalAoMotorista.subtract(comissaoJaPaga);
          //  view.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaAReceber));
         //   return comissaAReceber;
            return null;
        }

        private static BigDecimal ui_reembolso(List<CustosDePercurso> dataSet, @NonNull TextView view) {
            List<CustosDePercurso> listaCustosEmAberto = FiltraCustosPercurso.listaPorTipo(dataSet, REEMBOLSAVEL_EM_ABERTO);
            BigDecimal custoEmAberto = CalculoUtil.somaCustosDePercurso(listaCustosEmAberto);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(custoEmAberto));
            return custoEmAberto;
        }

        @Nullable
        private static BigDecimal ui_adiantamento(List<Adiantamento> dataSet, @NonNull TextView view) {
            BigDecimal adiantamentoADescontar = CalculoUtil.somaAdiantamentoPorStatus(dataSet, false);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamentoADescontar));
            return adiantamentoADescontar;
        }
    }
}

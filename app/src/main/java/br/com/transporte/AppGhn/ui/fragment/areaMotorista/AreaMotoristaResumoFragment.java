package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.NAO_REEMBOLSAVEL;
import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
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
    private RoomCustosAbastecimentoDao abastecimentoDao;
    private RoomCustosPercursoDao custoDao;
    private RoomAdiantamentoDao adiantamentoDao;
    private RoomFreteDao freteDao;
    private List<CustosDeAbastecimento> dataSet_abastecimento;
    private List<Adiantamento> dataSet_adiantamento;
    private List<CustosDePercurso> dataSet_custoPercurso;
    private List<Frete> dataSet_frete;
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
        abastecimentoDao = dataBase.getRoomCustosAbastecimentoDao();
        adiantamentoDao = dataBase.getRoomAdiantamentoDao();
        custoDao = dataBase.getRoomCustosPercursoDao();
        freteDao = dataBase.getRoomFreteDao();

        configuracaoInicialDateRange();
        cavalo = recebeCavaloEscolhidoParaVisualizacao(dataBase);
        atualizaDataSet();
    }

    private Cavalo recebeCavaloEscolhidoParaVisualizacao(@NonNull GhnDataBase dataBase) {
        RoomCavaloDao cavaloDao = dataBase.getRoomCavaloDao();
        Long cavaloId = requireArguments().getLong(CHAVE_ID_CAVALO);
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

    private void atualizaDataSet() {
        if (dataSet_frete == null) dataSet_frete = new ArrayList<>();
        if (dataSet_abastecimento == null) dataSet_abastecimento = new ArrayList<>();
        if (dataSet_custoPercurso == null) dataSet_custoPercurso = new ArrayList<>();
        if (dataSet_adiantamento == null) dataSet_adiantamento = new ArrayList<>();

        dataSet_frete.clear();
        dataSet_frete.addAll(freteDao.listaPorCavaloId(cavalo.getId()));
        dataSet_frete = FiltraFrete.listaPorData(dataSet_frete, dataInicial, dataFinal);

        dataSet_abastecimento.clear();
        dataSet_abastecimento.addAll(abastecimentoDao.listaPorCavaloId(cavalo.getId()));
        dataSet_abastecimento = FiltraCustosAbastecimento.listaPorData(dataSet_abastecimento, dataInicial, dataFinal);

        dataSet_custoPercurso.clear();
        dataSet_custoPercurso.addAll(custoDao.listaPorCavaloId(cavalo.getId()));
        dataSet_custoPercurso = FiltraCustosPercurso.listaPorData(dataSet_custoPercurso, dataInicial, dataFinal);

        dataSet_adiantamento.clear();
        dataSet_adiantamento.addAll(adiantamentoDao.listaPorCavaloId(cavalo.getId()));
        dataSet_adiantamento = FiltraAdiantamento.listaPorStatus(dataSet_adiantamento, false);
    }

    @NonNull
    private List<Adiantamento> getDataSetAdiantamento() {
        return new ArrayList<>(dataSet_adiantamento);
    }

    @NonNull
    private List<CustosDePercurso> getDataSetCustoPercurso() {
        return new ArrayList<>(dataSet_custoPercurso);
    }

    @NonNull
    private List<CustosDeAbastecimento> getDataSetAbastecimento() {
        return new ArrayList<>(dataSet_abastecimento);
    }

    @NonNull
    @Contract(" -> new")
    private List<Frete> getListaDeFretes() {
        return new ArrayList<>(dataSet_frete);
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
        UiCardReembolso.ui_despesaTotal(dataSet_custoPercurso, despesaTotalTxt);
        UiCardReembolso.ui_semReembolso(dataSet_custoPercurso, despesaNaoReembolsavelTxt);
        UiCardReembolso.ui_reembolso(custoEmAberto, despesaReembolsavelTxt);
    }

    private void ui_configuraCardAbastecimento() {
        UiCardAbastecimento.ui_abastecimentoTotal(dataSet_abastecimento, abastecimentoTotalTxt);
        UiCardAbastecimento.ui_litragemTotal(dataSet_abastecimento, litragemTotalTxt);
        UiCardAbastecimento.ui_kmPercorrido(cavalo.getId(), dataInicial, dataFinal, kmPercorridoTxt);
    }

    private void ui_configuraCardFrete() {
        UiCardFrete.ui_FreteBruto(dataSet_frete, freteBrutoTxt);
        UiCardFrete.ui_descontos(dataSet_frete, descontosFreteTxt);
        UiCardFrete.ui_freteLiquido(dataSet_frete, freteLiquidoTxt);
    }

    //---------------------- Configura Informaçoes de comissao

    private void ui_configuraComissionamento() {
        BigDecimal comissaoTotalAoMotorista = ui_acumulado();
        BigDecimal comissaoJaPaga = ui_jaRecebido();

        BigDecimal adiantamentoADescontar = UiCardResumo.ui_adiantamento(dataSet_adiantamento, adiantamentoTxt);
        custoEmAberto = UiCardResumo.ui_reembolso(dataSet_custoPercurso, reembolsoDespesasTxt);
        BigDecimal comissaAReceber = UiCardResumo.ui_comissaoAReceber(comissaoTotalAoMotorista, comissaoJaPaga, emAbertoTxt);

        totalAReceber(adiantamentoADescontar, custoEmAberto, comissaAReceber);
    }

    private BigDecimal ui_acumulado() {
        BigDecimal comissaoTotalAoMotorista = CalculoUtil.somaComissao(dataSet_frete);
        acumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoTotalAoMotorista));
        return comissaoTotalAoMotorista;
    }

    private void totalAReceber(
            BigDecimal adiantamentoADescontar, BigDecimal custoEmAberto, @NonNull BigDecimal comissaAReceber
    ) {
        BigDecimal valorAReceber = comissaAReceber.add(custoEmAberto).subtract(adiantamentoADescontar);
        totalAReceberTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAReceber));
    }

    private BigDecimal ui_jaRecebido() {
        BigDecimal comissaoJaPaga = CalculoUtil.somaComissaoPorStatus(dataSet_frete, true);
        jaRecebidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoJaPaga));
        return comissaoJaPaga;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (atualizacaoSolicitadaPelaActivity) {
            atualizaUi();
            resetaSolicitacaoDeAtualizacao();
        }
    }

    private void atualizaUi() {
        atualizaDataSet();
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

    public void solicitaAtualizacao() {
        this.atualizacaoSolicitadaPelaActivity = true;
    }

    //----------------------------------------------------------------------------------------------
    //                                          CallBack                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void selecionaDataComSucesso(LocalDate dataInicial, LocalDate dataFinal) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        atualizaDataSet();
        configuraUi();
    }

    //----------------------------------------------------------------------------------------------
    //                                          Ui Card Frete                                     ||
    //----------------------------------------------------------------------------------------------

    private static class UiCardFrete {
        private static void ui_freteLiquido(List<Frete> dataSet, @NonNull TextView view) {
            BigDecimal somaFreteLiquidoAReceber = CalculoUtil.somaFreteLiquido(dataSet);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteLiquidoAReceber));
        }

        private static void ui_descontos(List<Frete> dataSet, @NonNull TextView view) {
            BigDecimal somaDescontosNoFrete = CalculoUtil.somaDescontoNoFrete(dataSet);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaDescontosNoFrete));
        }

        private static void ui_FreteBruto(List<Frete> dataSet, @NonNull TextView view) {
            BigDecimal somaFreteBruto = CalculoUtil.somaFreteBruto(dataSet);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteBruto));
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

        private static void ui_kmPercorrido(Long cavaloId, LocalDate dataInicial, LocalDate dataFinal, TextView view) {
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

        private static BigDecimal calculaKmPercorrido(Long cavaloId, LocalDate dataInicial, LocalDate dataFinal) {
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
            BigDecimal comissaAReceber = comissaoTotalAoMotorista.subtract(comissaoJaPaga);
            view.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaAReceber));
            return comissaAReceber;
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

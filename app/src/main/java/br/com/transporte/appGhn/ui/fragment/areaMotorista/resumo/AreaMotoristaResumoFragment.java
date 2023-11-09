package br.com.transporte.appGhn.ui.fragment.areaMotorista.resumo;

import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.REQUEST_ATUALIZACAO_DE_DATA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaResumoBinding;
import br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel.AreaMotoristaActViewModel;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.DateRangePickerUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;

public class AreaMotoristaResumoFragment extends Fragment {
    public static final String ZERO = "0.00";
    private FragmentAreaMotoristaResumoBinding binding;
    private TextView totalAReceberTxt, acumuladoTxt, jaRecebidoTxt, emAbertoTxt, reembolsoDespesasTxt, adiantamentoTxt,
            dataInicialTxt, dataFinalTxt, freteBrutoTxt, descontosFreteTxt, freteLiquidoTxt, abastecimentoTotalTxt,
            litragemTotalTxt, kmPercorridoTxt, despesaTotalTxt, despesaReembolsavelTxt, despesaNaoReembolsavelTxt;
    private LinearLayout dataLayout;
    private AreaMotoristaActViewModel viewModel;
    private boolean atualizacaoSolicitadaPelaAct;
    private DateRangePickerUtil dateRange;

    public boolean isAtualizacaoSolicitadaPelaAct() {
        return atualizacaoSolicitadaPelaAct;
    }

    public void setAtualizacaoSolicitadaPelaAct(boolean atualizacaoSolicitadaPelaAct) {
        this.atualizacaoSolicitadaPelaAct = atualizacaoSolicitadaPelaAct;
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AreaMotoristaActViewModel.class);
        dateRange = new DateRangePickerUtil(getParentFragmentManager());
    }

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
        ui_data();
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

    /** @noinspection DataFlowIssue*/
    private void configuraDateRangePicker() {
        dateRange.build(dataLayout);
        dateRange.setCallbackDatePicker(
                (dataInicial, dataFinal) -> {
                    viewModel.setSharedDataInicial(dataInicial);
                    viewModel.setSharedDataFinal(dataFinal);
                    getParentFragmentManager().setFragmentResult(REQUEST_ATUALIZACAO_DE_DATA, null);
                });
    }

    private void ui_data() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(viewModel.getSharedDataInicial()));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(viewModel.getSharedDataFinal()));
    }

    //----------------------------------------------------------------------------------------------
    //                                    Metodos para Activity                                   ||
    //----------------------------------------------------------------------------------------------
    public void atualizaValoresParaExibirNaUi() {
        ui_data();
        final CalculaValoresParaUiHelper calculadora = new CalculaValoresParaUiHelper();
        calculadora.atualizaDataBase(
                viewModel.getList_abastecimentoTodos(),
                viewModel.getSharedList_abastecimento(),
                viewModel.getSharedList_adiantamento(),
                viewModel.getSharedList_custo(),
                viewModel.getSharedList_frete()
        );
        calculadora.geraValores(
                resource -> {
                    BigDecimal comissaoTotalAoMotorista = resource.getComissaoTotalAoMotorista();
                    if (comissaoTotalAoMotorista != null){
                        acumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoTotalAoMotorista));
                    }

                    BigDecimal comissaoJaPaga = resource.getComissaoJaPaga();
                    if (comissaoJaPaga != null)
                        jaRecebidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoJaPaga));

                    BigDecimal adiantamentoADescontar = resource.getAdiantamentoADescontar();
                    if (adiantamentoADescontar != null)
                        adiantamentoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamentoADescontar));

                    BigDecimal custoEmAberto1 = resource.getCustoEmAberto();
                    if (custoEmAberto1 != null){
                        reembolsoDespesasTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custoEmAberto1));
                        despesaReembolsavelTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custoEmAberto1));
                    }

                    BigDecimal comissaoAReceber = resource.getComissaoAReceber();
                    if (comissaoAReceber != null)
                        emAbertoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoAReceber));

                    BigDecimal valorAReceber = resource.getValorAReceber();
                    if (valorAReceber != null)
                        totalAReceberTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAReceber));

                    BigDecimal somaFreteBruto = resource.getSomaFreteBruto();
                    if (somaFreteBruto != null)
                        freteBrutoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteBruto));

                    BigDecimal somaDescontosNoFrete = resource.getSomaDescontosNoFrete();
                    if (somaDescontosNoFrete != null)
                        descontosFreteTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaDescontosNoFrete));

                    BigDecimal somaFreteLiquidoAReceber = resource.getSomaFreteLiquidoAReceber();
                    if (somaFreteLiquidoAReceber != null)
                        freteLiquidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteLiquidoAReceber));

                    BigDecimal abastecimentoTotal = resource.getAbastecimentoTotal();
                    if (abastecimentoTotal != null)
                        abastecimentoTotalTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(abastecimentoTotal));

                    BigDecimal litragemTotal = resource.getLitragemTotal();
                    if (litragemTotal != null)
                        litragemTotalTxt.setText(FormataNumerosUtil.formataNumero(litragemTotal));

                    BigDecimal kmPercorrido = resource.getKmPercorrido();
                    if (kmPercorrido != null)
                        kmPercorridoTxt.setText(FormataNumerosUtil.formataNumero(kmPercorrido));

                    BigDecimal custosPercursoTotal = resource.getCustosPercursoTotal();
                    if (custosPercursoTotal != null)
                        despesaTotalTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosPercursoTotal));

                    BigDecimal custosDePercursoSemReembolso = resource.getCustosDePercursoSemReembolso();
                    if (custosDePercursoSemReembolso != null)
                        despesaNaoReembolsavelTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosDePercursoSemReembolso));
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAtualizacaoSolicitadaPelaAct()){
            atualizaValoresParaExibirNaUi();
            setAtualizacaoSolicitadaPelaAct(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

package br.com.transporte.appGhn.ui.fragment.pagamentoComissoes;

import static android.app.Activity.RESULT_CANCELED;
import static android.view.View.GONE;
import static br.com.transporte.appGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ADIANTAMENTO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_ADIANTAMENTO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_DEFAUT;
import static br.com.transporte.appGhn.ui.fragment.manutencao.ManutencaoDetalhesFragment.S_M;
import static br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment.TipoDeAdapterPressionado.ADIANTAMENTO;
import static br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment.TipoDeAdapterPressionado.FRETE;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_GONE;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentComissoesDetalhesBinding;
import br.com.transporte.appGhn.exception.ValorInvalidoException;
import br.com.transporte.appGhn.filtros.FiltraAdiantamento;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.filtros.FiltraMotorista;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.custos.CustosDeSalario;
import br.com.transporte.appGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.dialog.AlteraComissao;
import br.com.transporte.appGhn.ui.dialog.DescontaAdiantamento;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesDetalhesHelpers.ComissoesRecyclerAdiantamentoHelper;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesDetalhesHelpers.ComissoesRecyclerCustosHelper;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesDetalhesHelpers.ComissoesRecyclerFreteHelper;
import br.com.transporte.appGhn.ui.viewmodel.ComissaoActViewModel;
import br.com.transporte.appGhn.util.CalculoUtil;
import br.com.transporte.appGhn.util.DataUtil;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class ComissoesDetalhesFragment extends Fragment {
    public static final String DETALHES_PARA_PAGAMENTO = "Detalhes para pagamento";
    public static final String FECHAMENTO = "Fechamento";
    public static final String CONFIRMAR = "Confirmar";
    public static final String CANCELAR = "Cancelar";
    public static final int VALORES_IGUAIS = 0;
    private FragmentComissoesDetalhesBinding binding;
    private TextView comissaoTxt, liquidoTxt, reembolsoTxt, adiantamentoDescontarTxt, placaTxt, nomeTxt;
    private BigDecimal comissaoAcumulada, reembolsoAcumulado, descontoAcumulado, liquidoAFechar;
    private TipoDeAdapterPressionado tipoDeAdapterPressionado;
    private List<Adiantamento> dataSet_adiantamento;
    private List<CustosDePercurso> dataSet_custosPercurso;
    private List<Frete> dataSet_frete;
    private Map<Long, BigDecimal> mapDeAdiantamentos;
    private ComissoesRecyclerAdiantamentoHelper recyclerAdiantamentoHelper;
    private ComissoesRecyclerFreteHelper recyclerFreteHelper;
    private ComissoesRecyclerCustosHelper recyclerCustosHelper;
    private Cavalo cavalo;
    private Button btn;
    private ComissaoActViewModel viewModel;
    private final ActivityResultLauncher<Intent> activityResultLauncherAdiantamento = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        Adiantamento adiantamentoRecebido = (Adiantamento) Objects.requireNonNull(dataResultado).getSerializableExtra(CHAVE_ADIANTAMENTO);
                        substituiValorNoMap(Objects.requireNonNull(adiantamentoRecebido).getId(), adiantamentoRecebido.restaReembolsar());
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;

                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;

                    case RESULT_DELETE:
                        int key = Objects.requireNonNull(dataResultado).getIntExtra(CHAVE_ID, VALOR_DEFAUT);
                        //noinspection SuspiciousMethodCalls
                        mapDeAdiantamentos.remove(key);
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                }
            });

    private final ActivityResultLauncher<Intent> activityResultLauncherCustosReembolsaveis = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                }
            });

    private void atualizaAdiantamentoAposRetornoDeResult(String msg) {
        atualizaDataSet_adiantamento();
        recyclerAdiantamentoHelper.solicitaAtualizacao(getDataSet_adiantamento());
        configuraVisibilidadeDeLayouts();
        ui_atualizaAdiantamento();
        ui_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ComissaoActViewModel.class);
        cavalo = recebeIdArguments();

        atualizaDataSet_frete();
        atualizaDataSet_adiantamento();
        atualizaDataSet_custos();
        configuraMapComValoresDeAdiantamentoADescontar();
    }

    private void configuraMapComValoresDeAdiantamentoADescontar() {
        if (mapDeAdiantamentos == null) mapDeAdiantamentos = new HashMap<>();
        for (Adiantamento a : dataSet_adiantamento) {
            BigDecimal valorRestanteADescontar = a.getValorTotal().subtract(a.getSaldoRestituido());
            mapDeAdiantamentos.put(a.getId(), valorRestanteADescontar);
        }
    }

    private Cavalo recebeIdArguments() {
        Long cavaloId = ComissoesDetalhesFragmentArgs.fromBundle(getArguments()).getCavaloId();
        cavalo = (Cavalo) FiltraCavalo.localizaPeloId(viewModel.getDataSet_cavalo(), cavaloId);
        return cavalo;
    }

    private void atualizaDataSet_frete() {
        if (dataSet_frete == null) dataSet_adiantamento = new ArrayList<>();
        dataSet_frete = FiltraFrete.listaPorCavaloId(viewModel.getListaFreteComFiltro(), cavalo.getId());
        dataSet_frete = FiltraFrete.listaPorStatusDePagamentoDaComissao(dataSet_frete, false);
    }

    private void atualizaDataSet_adiantamento() {
        if (dataSet_adiantamento == null) dataSet_adiantamento = new ArrayList<>();
        dataSet_adiantamento = FiltraAdiantamento.listaPorCavaloId(viewModel.getDataSetAdiantamento(), cavalo.getId());
        dataSet_adiantamento = FiltraAdiantamento.listaPorStatus(dataSet_adiantamento, false);
    }

    private void atualizaDataSet_custos() {
        if (dataSet_custosPercurso == null) dataSet_custosPercurso = new ArrayList<>();
        dataSet_custosPercurso = FiltraCustosPercurso.listaPorCavaloId(viewModel.getDataSetReembolso(), cavalo.getId());
        dataSet_custosPercurso = FiltraCustosPercurso.listaPorTipo(dataSet_custosPercurso, REEMBOLSAVEL_EM_ABERTO);
    }

    @NonNull
    @Contract(" -> new")
    private List<Frete> getDataSet_frete() {
        return new ArrayList<>(dataSet_frete);
    }

    @NonNull
    @Contract(" -> new")
    private List<Adiantamento> getDataSet_adiantamento() {
        return new ArrayList<>(dataSet_adiantamento);
    }

    @NonNull
    @Contract(" -> new")
    private List<CustosDePercurso> getDataSet_custos() {
        return new ArrayList<>(dataSet_custosPercurso);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesDetalhesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraRecyclerAdiantamento();
        configuraRecyclerFrete();
        configuraRecyclerReembolso();
        configuraUi();
        configuraBtn(view);
        configuraVisibilidadeDeLayouts();
    }

    private void inicializaCampos() {
        btn = binding.btn;
        placaTxt = binding.placa;
        nomeTxt = binding.motorista;
        liquidoTxt = binding.liquidoValor;
        adiantamentoDescontarTxt = binding.descontoValor;
        comissaoTxt = binding.comissaoValor;
        reembolsoTxt = binding.reembolsoValor;
    }

    private void configuraVisibilidadeDeLayouts() {
        LinearLayout layoutReembolso = binding.reembolsoLayout;
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet_custos().size(), null, layoutReembolso, VIEW_GONE);

        LinearLayout layoutAdiantamento = binding.adiantamentoLayout;
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet_adiantamento().size(), null, layoutAdiantamento, VIEW_GONE);

        LinearLayout layoutFrete = binding.freteLayout;
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet_frete().size(), null, layoutFrete, VIEW_GONE);
    }

    //--------------------------------

    private void configuraRecyclerAdiantamento() {
        RecyclerView recycler = binding.adiantamentoRecycler;
        recyclerAdiantamentoHelper = new ComissoesRecyclerAdiantamentoHelper(this.requireContext());
        recyclerAdiantamentoHelper.build(recycler, getDataSet_adiantamento());
        recyclerAdiantamentoHelper.setCallbackAdiantamento(new ComissoesRecyclerAdiantamentoHelper.CallbackAdiantamento() {
            @Override
            public void onClickListener(Long adiantamentoId) {
                Intent intent = new Intent(requireContext(), FormulariosActivity.class);
                intent.putExtra(CHAVE_FORMULARIO, VALOR_ADIANTAMENTO);
                intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
                intent.putExtra(CHAVE_ID, adiantamentoId);
                activityResultLauncherAdiantamento.launch(intent);
            }

            @Override
            public void onLongClickListener() {
                tipoDeAdapterPressionado = ADIANTAMENTO;
            }
        });
    }

    private void configuraRecyclerFrete() {
        RecyclerView recycler = binding.comissaoRecycler;
        recyclerFreteHelper = new ComissoesRecyclerFreteHelper(requireContext());
        recyclerFreteHelper.build(recycler, getDataSet_frete());
        recyclerFreteHelper.setCallbackReembolso(() -> tipoDeAdapterPressionado = FRETE);
    }

    private void configuraRecyclerReembolso() {
        RecyclerView recycler = binding.reembolsoRecycler;
        recyclerCustosHelper = new ComissoesRecyclerCustosHelper(requireContext());
        recyclerCustosHelper.build(recycler, getDataSet_custos());
        recyclerCustosHelper.setCallbackReembolso(custoId -> {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            intent.putExtra(CHAVE_ID, custoId);
            activityResultLauncherCustosReembolsaveis.launch(intent);
        });
    }

    //----------------------------------------

    private void configuraBtn(@NonNull View view) {
        int compare = comissaoAcumulada.compareTo(BigDecimal.ZERO);
        if (compare == 0) {
            btn.setVisibility(GONE);
        }

        btn.setOnClickListener(v ->
                new AlertDialog.Builder(getContext())
                        .setTitle(FECHAMENTO)
                        .setMessage(FormataNumerosUtil.formataMoedaPadraoBr(liquidoAFechar))
                        .setPositiveButton(CONFIRMAR, (dialog, which) -> {
                            final CustosDeSalario salario = new CustosDeSalario();

                            for (Frete f : dataSet_frete) {
                                f.setComissaoJaFoiPaga(true);
                                f.setApenasAdmEdita(true);
                                viewModel.salvaFrete(f);
                                salario.listaFretesAdiciona(f.getId());
                            }

                            for (Adiantamento a : dataSet_adiantamento) {
                                if (mapDeAdiantamentos.containsKey(a.getId())) {
                                    BigDecimal bd = mapDeAdiantamentos.get(a.getId());
                                    try {
                                        a.restituirValorPagoComoAdiantamento(bd);
                                        a.setUltimoValorAbatido(bd);
                                        salario.listaAdiantamentosAdiciona(a.getId());
                                    } catch (ValorInvalidoException e) {
                                        e.printStackTrace();
                                    }
                                }
                                viewModel.salvaAdiantamento(a);
                            }

                            for (CustosDePercurso c : dataSet_custosPercurso) {
                                c.setTipo(TipoCustoDePercurso.REEMBOLSAVEL_JA_PAGO);
                                viewModel.editaCusto(c);
                                salario.listaReembolsosAdiciona(c.getId());
                            }

                            final LocalDate dataDoPagamento = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
                            salario.setData(dataDoPagamento);
                            salario.setRefCavaloId(cavalo.getId());
                            salario.setValorCusto(liquidoAFechar);
                            salario.setRefMotoristaId(cavalo.getRefMotoristaId());
                            viewModel.adicionaSalario(salario).observe(getViewLifecycleOwner(),
                                    id_ignored -> {
                                        recyclerFreteHelper.solicitaAtualizacao(getDataSet_frete());
                                        recyclerAdiantamentoHelper.solicitaAtualizacao(getDataSet_adiantamento());
                                        recyclerCustosHelper.solicitaAtualizacao(getDataSet_custos());

                                        NavController controlador = Navigation.findNavController(view);
                                        controlador.popBackStack();
                                    });
                        })
                        .setNegativeButton(CANCELAR, null)
                        .show());
    }

    //------------------------------------

    private void configuraUi() {
        placaTxt.setText(cavalo.getPlaca());

        try {
            String nome = FiltraMotorista.localizaPeloId(viewModel.getDataSet_motorista(), cavalo.getRefMotoristaId()).getNome();
            nomeTxt.setText(nome);
        } catch (NullPointerException e) {
            e.printStackTrace();
            nomeTxt.setText(S_M);
        }

        comissaoAcumulada = ui_comissao();
        reembolsoAcumulado = ui_atualizaReembolso();
        descontoAcumulado = ui_atualizaAdiantamento();
        ui_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
    }

    private void ui_atualizaLiquidoAFechar(@NonNull BigDecimal comissaoAcumulada, BigDecimal reembolsoAcumulado, BigDecimal descontoAcumulado) {
        liquidoAFechar = comissaoAcumulada.add(reembolsoAcumulado).subtract(descontoAcumulado);
        liquidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(liquidoAFechar));
    }

    private BigDecimal ui_comissao() {
        comissaoAcumulada = CalculoUtil.somaComissao(getDataSet_frete());
        comissaoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoAcumulada));
        return comissaoAcumulada;
    }

    private BigDecimal ui_atualizaReembolso() {
        reembolsoAcumulado = CalculoUtil.somaCustosDePercurso(getDataSet_custos());
        reembolsoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(reembolsoAcumulado));
        return reembolsoAcumulado;
    }

    private BigDecimal ui_atualizaAdiantamento() {
        descontoAcumulado = mapDeAdiantamentos.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        adiantamentoDescontarTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(descontoAcumulado));
        return descontoAcumulado;
    }

    /** @noinspection UnusedAssignment*/
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;

        switch (tipoDeAdapterPressionado) {
            case ADIANTAMENTO:
                posicao = recyclerAdiantamentoHelper.solicitaPosicao();
                Adiantamento adiantamento = dataSet_adiantamento.get(posicao);
                if (item.getItemId() == R.id.editarDesconto) {
                    DescontaAdiantamento descontaAdiantamento = new DescontaAdiantamento(this.getContext(), adiantamento);
                    descontaAdiantamento.dialogDescontaAdiantamento();
                    descontaAdiantamento.setCallback(new DescontaAdiantamento.Callback() {
                        @Override
                        public void quandoFunciona(Long id, BigDecimal novoValor, String msg) {
                            substituiValorNoMap(id, novoValor);
                            recyclerAdiantamentoHelper.solicitaAtualizacaoMap(new HashMap<>(mapDeAdiantamentos));
                            configuraVisibilidadeDeLayouts();
                            ui_atualizaAdiantamento();
                            ui_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void quandoFalha(String msg) {
                            MensagemUtil.snackBar(getView(), msg);
                        }

                        @Override
                        public void quandoCancela(String msg) {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

            case FRETE:
                posicao = recyclerFreteHelper.solicitaPosicao();
                Frete frete = dataSet_frete.get(posicao);
                if (item.getItemId() == R.id.editarComissao) {
                    AlteraComissao alteraComissao = new AlteraComissao(this.getContext(), frete);
                    alteraComissao.dialogAlteraComissao();
                    int posicaoTemporaria = posicao;
                    alteraComissao.setCallback(new AlteraComissao.Callback() {
                        @Override
                        public void quandoFunciona(Frete frete, String msg) {
                            viewModel.salvaFrete(frete).observe(getViewLifecycleOwner(),
                                    id -> {
                                        recyclerFreteHelper.solicitaAtualizacaoDoItem(posicaoTemporaria);
                                        atualizaDataSet_frete();
                                        ui_comissao();
                                        ui_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
                                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                                    });
                        }

                        @Override
                        public void quandoFalha(String msg) {
                            MensagemUtil.snackBar(requireView(), msg);
                        }

                        @Override
                        public void quandoCancela(String msg) {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void substituiValorNoMap(Long id, BigDecimal novoValor) {
        if (mapDeAdiantamentos.containsKey(id)) {
            mapDeAdiantamentos.replace(id, novoValor);
        }
    }

    public void actNotificaAtualizacao_frete() {
        //todo
    }

    public void actSolicitaAtt_adiantamento() {
        atualizaDataSet_adiantamento();
        recyclerAdiantamentoHelper.solicitaAtualizacao(getDataSet_adiantamento());
        recyclerAdiantamentoHelper.solicitaAtualizacaoMap(mapDeAdiantamentos);
        configuraVisibilidadeDeLayouts();
        ui_atualizaAdiantamento();
        ui_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
    }

    public void actSolicitaAtt_custoPercurso() {
        atualizaDataSet_custos();
        recyclerCustosHelper.solicitaAtualizacao(getDataSet_custos());
        configuraVisibilidadeDeLayouts();
        ui_atualizaReembolso();
        ui_atualizaLiquidoAFechar(comissaoAcumulada, reembolsoAcumulado, descontoAcumulado);
    }

    public enum TipoDeAdapterPressionado {
        ADIANTAMENTO, FRETE
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
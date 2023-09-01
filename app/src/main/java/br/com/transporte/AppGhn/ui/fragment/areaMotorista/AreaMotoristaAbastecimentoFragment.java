package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ABASTECIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaResumoFragment.KEY_ACTION_ADAPTER;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaAbastecimentoBinding;
import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.AbastecimentoAdapter;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class AreaMotoristaAbastecimentoFragment extends Fragment implements DateRangePickerUtil.CallbackDatePicker {
    private FragmentAreaMotoristaAbastecimentoBinding binding;
    private AbastecimentoAdapter adapter;
    private CustosDeAbastecimentoDAO abastecimentoDao;
    private TextView dataInicialTxt, dataFinalTxt, abastecimentoAcumuladoTxt;
    private LocalDate dataInicial, dataFinal;
    private LinearLayout dataLayout, buscaVazia;
    private List<CustosDeAbastecimento> listaDeAbastecimentos;
    private Cavalo cavalo;
    private DateRangePickerUtil dateRange;
    private RecyclerView recycler;
    private boolean atualizacaoSolicitadaPelaActivity = false;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_EDIT:
                            atualizaAposResultado(REGISTRO_EDITADO);
                            ui_totalAbastecimento();
                            getParentFragmentManager().setFragmentResult(KEY_ACTION_ADAPTER, null);
                            break;

                        case RESULT_CANCELED:
                            atualizaAposResultado(NENHUMA_ALTERACAO_REALIZADA);
                            break;

                        case RESULT_DELETE:
                            atualizaAposResultado(REGISTRO_APAGADO);
                            ui_totalAbastecimento();
                            getParentFragmentManager().setFragmentResult(KEY_ACTION_ADAPTER, null);
                            break;
                    }
                }
        );
    }

    private void atualizaAposResultado(String msg) {
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
        adapter.atualiza(listaDeAbastecimentos);
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abastecimentoDao = new CustosDeAbastecimentoDAO();
        configuracaoInicialDateRange();
        configuracaoInicialListaDeAbastecimentos();
    }

    private void configuracaoInicialListaDeAbastecimentos() {
        CavaloDAO cavaloDao = new CavaloDAO();
        int cavaloId = requireArguments().getInt(CHAVE_ID_CAVALO);
        cavalo = cavaloDao.localizaPeloId(cavaloId);
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
    }

    private void configuracaoInicialDateRange() {
        dateRange = new DateRangePickerUtil(getParentFragmentManager());
        dataInicial = dateRange.getDataInicialEmLocalDate();
        dataFinal = dateRange.getDataFinalEmLocalDate();
    }

    @NonNull
    private List<CustosDeAbastecimento> getListaDeAbastecimentos(@NonNull Cavalo cavalo) {
        List<CustosDeAbastecimento> dataSet = FiltraCustosAbastecimento.listaPorCavaloId(abastecimentoDao.listaTodos(), cavalo.getId());
        dataSet = FiltraCustosAbastecimento.listaPorData(dataSet, dataInicial, dataFinal);
        dataSet.sort(Comparator.comparing(CustosDeAbastecimento::getMarcacaoKm));
        Collections.reverse(dataSet);
        return dataSet;
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAreaMotoristaAbastecimentoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();
    }

    private void inicializaCampos() {
        recycler = binding.fragAreaMotoristaAbastecimentoRecycler;
        abastecimentoAcumuladoTxt = binding.fragAbastecimentoValorTotal;
        dataLayout = binding.fragAbastecimentoMes;
        dataInicialTxt = binding.fragAbastecimentoMesDtInicial;
        dataFinalTxt = binding.fragAbastecimentoMesDtFinal;
        buscaVazia = binding.fragAbastecimentoVazio;
    }

    private void configuraDateRangePicker() {
        dateRange.build(dataLayout);
        dateRange.setCallbackDatePicker(this);
    }

    //-------------------------------------
    // -> Configura Recycler             ||
    //-------------------------------------

    private void configuraRecycler() {
        adapter = new AbastecimentoAdapter(this, listaDeAbastecimentos);
        recycler.setAdapter(adapter);
        configuraRecyclerListener();
    }

    private void configuraRecyclerListener() {
        adapter.setOnItemClickListener((idAbastecimento) -> {
            Intent intent = new Intent(getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_ID, (Integer) idAbastecimento);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_ABASTECIMENTO);
            activityResultLauncher.launch(intent);
        });
    }

    //--------------------------------
    // -> Configura Ui              ||
    //--------------------------------

    private void configuraUi() {
        ui_data();
        ui_totalAbastecimento();
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaDeAbastecimentos.size(), buscaVazia, recycler, "INVISIBLE");
    }

    private void ui_totalAbastecimento() {
        BigDecimal somaAbastecimento = CalculoUtil.somaCustosDeAbastecimento(listaDeAbastecimentos);
        abastecimentoAcumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaAbastecimento));
    }

    private void ui_data() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (atualizacaoSolicitadaPelaActivity) {
            atualizaUi();
            resetaSolicitacaoDeAtualizacao();
            ui_totalAbastecimento();
            ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaDeAbastecimentos.size(), buscaVazia, recycler, "INVISIBLE");
        }
    }

    private void resetaSolicitacaoDeAtualizacao() {
        atualizacaoSolicitadaPelaActivity = false;
    }

    private void atualizaUi() {
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
        adapter.atualiza(listaDeAbastecimentos);
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
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
        adapter.atualiza(listaDeAbastecimentos);
        configuraUi();
    }


}


package br.com.transporte.appGhn.ui.fragment.areaMotorista;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.REQUEST_ATUALIZACAO_DE_DATA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;

import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaCustosDePercursoBinding;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.CustosDePercursoAdapter;
import br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel.AreaMotoristaActViewModel;
import br.com.transporte.appGhn.util.CalculoUtil;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.DateRangePickerUtil;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class AreaMotoristaCustosDePercursoFragment extends Fragment {
    private FragmentAreaMotoristaCustosDePercursoBinding binding;
    private LinearLayout dataLayout, alertaView;
    private TextView dataInicialTxt, dataFinalTxt, despesaAcumuladaTxt;
    private RecyclerView recycler;
    private AreaMotoristaActViewModel viewModel;
    private boolean atualizacaoSolicitadaPelaAct;
    private CustosDePercursoAdapter adapter;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();
    private DateRangePickerUtil dateRange;

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    switch (resultCode) {
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
    }

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
        binding = FragmentAreaMotoristaCustosDePercursoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          onViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraRecycler();
        ui_data();
        ui_valorTotal();
        configuraDateRangePicker();
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getSharedList_custo().size(), alertaView, recycler, VIEW_INVISIBLE);
    }

    private void inicializaCampos() {
        recycler = binding.fragAreaMotoristaDespesaRecycler;
        despesaAcumuladaTxt = binding.fragDespesaValorTotal;
        dataLayout = binding.fragDespesaMes;
        dataInicialTxt = binding.fragDespesaMesDtInicial;
        dataFinalTxt = binding.fragDespesaMesDtFinal;
        alertaView = binding.fragDespesaVazio;
    }

    private void ui_data() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(viewModel.getSharedDataInicial()));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(viewModel.getSharedDataFinal()));
    }

    private void ui_valorTotal() {
        final BigDecimal somaCustos = CalculoUtil.somaCustosDePercurso(viewModel.getSharedList_custo());
        despesaAcumuladaTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaCustos));
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

    private void configuraRecycler() {
        adapter = new CustosDePercursoAdapter(this, viewModel.getSharedList_custo());
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this::configuraRecyclerListener);
    }

    private void configuraRecyclerListener(long despesaId) {
        final Intent intent = new Intent(getActivity(), FormulariosActivity.class);
        intent.putExtra(CHAVE_ID, despesaId);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
        activityResultLauncher.launch(intent);
    }

    public void atualizaValoresParaExibirNaUi() {
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getSharedList_custo().size(), alertaView, recycler, VIEW_INVISIBLE);
        adapter.atualiza(viewModel.getSharedList_custo());
        ui_data();
        ui_valorTotal();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAtualizacaoSolicitadaPelaAct()) {
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

package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static android.app.Activity.RESULT_CANCELED;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.REQUEST_ATUALIZACAO_DE_DATA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

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

import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaFreteBinding;
import br.com.transporte.AppGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.FreteAdapter;
import br.com.transporte.AppGhn.ui.viewmodel.areaMotoristaViewModel.AreaMotoristaActViewModel;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.DateRangePickerUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class AreaMotoristaFreteFragment extends Fragment {
    private FragmentAreaMotoristaFreteBinding binding;
    private TextView dataInicialTxt, dataFinalTxt, freteAcumuladoTxt;
    private LinearLayout dataLayout, alertaView;
    private RecyclerView recycler;
    private AreaMotoristaActViewModel viewModel;
    private FreteAdapter adapter;
    private boolean atualizacaoSolicitadaPelaAct;
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
                            atualizaAposResultado(REGISTRO_EDITADO);
                            break;
                        case RESULT_CANCELED:
                            atualizaAposResultado(NENHUMA_ALTERACAO_REALIZADA);
                            break;
                        case RESULT_DELETE:
                            atualizaAposResultado(REGISTRO_APAGADO);
                            break;
                    }
                }
        );
    }

    private void atualizaAposResultado(String msg) {
        MensagemUtil.toast(requireContext(), msg);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getSharedList_frete().size(), alertaView, recycler, VIEW_INVISIBLE);
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
        binding = FragmentAreaMotoristaFreteBinding.inflate(inflater, container, false);
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
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getSharedList_frete().size(), alertaView, recycler, VIEW_INVISIBLE);
    }

    private void inicializaCampos() {
        recycler = binding.fragAreaMotoristaFreteRecycler;
        freteAcumuladoTxt = binding.fragFreteValorTotal;
        dataLayout = binding.fragFreteMes;
        dataInicialTxt = binding.fragFreteMesDtInicial;
        dataFinalTxt = binding.fragFreteMesDtFinal;
        alertaView = binding.fragFreteVazio;
    }

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
        adapter = new FreteAdapter(this, viewModel.getSharedList_frete());
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this::configuraRecyclerListener);
    }

    private void configuraRecyclerListener(Long freteId) {
        final Intent intent = new Intent(getActivity(), FormulariosActivity.class);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
        intent.putExtra(CHAVE_ID, freteId);
        activityResultLauncher.launch(intent);
    }

    private void ui_data() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(viewModel.getSharedDataInicial()));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(viewModel.getSharedDataFinal()));
    }

    private void ui_valorTotal() {
        final BigDecimal freteLiquido = CalculoUtil.somaFreteLiquido(viewModel.getSharedList_frete());
        freteAcumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(freteLiquido));
    }

    //----------------------------------------------------------------------------------------------
    //                                    Metodos para Activity                                   ||
    //----------------------------------------------------------------------------------------------

    public void atualizaValoresParaExibirNaUi() {
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(viewModel.getSharedList_frete().size(), alertaView, recycler, VIEW_INVISIBLE);
        adapter.atualiza(viewModel.getSharedList_frete());
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


package br.com.transporte.appGhn.ui.fragment.despesasAdm.direta;

import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.databinding.FragmentDespesasAdmBinding;
import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.appGhn.ui.activity.despesaAdm.viewmodel.DespesaAdmActViewModel;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.DespesasAdmAdapter;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.domain.BuscaDespesasAdmDiretasUseCase;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.domain.model.DespesaAdmDiretaObject;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.extensions.CalculaDespesaAdmDiretaTotalExt;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.extensions.DespesaAdmDiretaMenuProviderExt;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.viewmodel.DespesaAdmDiretaViewModel;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.MensagemUtil;

public class DespesasAdmDiretasFragment extends Fragment {
    private FragmentDespesasAdmBinding binding;
    private DespesasAdmAdapter adapter;
    private RecyclerView recyclerView;
    private DespesaAdmActViewModel sharedActivityViewModel;
    private DespesaAdmDiretaViewModel viewModel;
    private DespesaAdmDiretaMenuProviderExt menuProviderExt;
    private BuscaDespesasAdmDiretasUseCase dataUseCase;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharedViewModel();
        initViewModelDoFragment();
        getDataUseCase();
    }

    private void initSharedViewModel() {
        sharedActivityViewModel = new ViewModelProvider(requireActivity()).get(DespesaAdmActViewModel.class);
    }

    private void initViewModelDoFragment() {
        viewModel = new DespesaAdmDiretaViewModel();
    }

    private void getDataUseCase() {
        dataUseCase = new BuscaDespesasAdmDiretasUseCase(requireContext(), this);
        dataUseCase.buscaDataRelacionadaAsDespesasAdmDiretas(
                sharedActivityViewModel.getDataInicial(),
                sharedActivityViewModel.getDataFinal(),
                dataSet -> {
                    if (dataSet != null) {
                        viewModel.setDataSet(dataSet);
                        menuProviderExt.atualizaDataSet(viewModel.getDataSet());
                        atualizaUi(dataSet);
                    }
                });
    }

    private void atualizaUi(final List<DespesaAdmDiretaObject> dataSet) {
        adapter.atualiza(dataSet);
        configuraUi(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDespesasAdmBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraMenuProvider();
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.fragItemDespesasFinanceirasRecycler;
    }

    private void configuraRecycler() {
        adapter = new DespesasAdmAdapter(this, viewModel.getDataSet());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(despesaId -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_DESPESA_ADM);
            intent.putExtra(CHAVE_TIPO_DESPESA, TipoDespesa.DIRETA);
            intent.putExtra(CHAVE_ID, (despesaId));
            startActivity(intent);
        });
    }

    private void configuraUi(final List<DespesaAdmDiretaObject> dataSet) {
        final LinearLayout alertaLayout = binding.alertaLayout.alerta;
        final TextView campoValor = binding.totalPago;
        final BigDecimal valorPago =
                CalculaDespesaAdmDiretaTotalExt
                        .getValor(dataSet);

        try {
            BindData.R$fromBigDecimal(campoValor, valorPago);
        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }

        ExibirResultadoDaBusca_sucessoOuAlerta
                .configura(
                        dataSet.size(), alertaLayout,
                        recyclerView, VIEW_INVISIBLE
                );

    }

    private void configuraMenuProvider() {
        menuProviderExt = new DespesaAdmDiretaMenuProviderExt(viewModel.getDataSet());
        requireActivity().addMenuProvider(menuProviderExt, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderExt.setCallback(new DespesaAdmDiretaMenuProviderExt.DespesaAdmDiretaMenuCallback() {
            @Override
            public void realizaBusca(List<DespesaAdmDiretaObject> dataSearch) {
                atualizaUi(dataSearch);
            }

            @Override
            public void onLogoutClick() {
                MensagemUtil.toast(requireContext(), LOGOUT);
                Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchClick() {
                Objects.requireNonNull(((AppCompatActivity) requireActivity())
                                .getSupportActionBar())
                        .setDisplayShowTitleEnabled(false);
            }

            @Override
            public void onSearchClear() {
                Objects.requireNonNull(((AppCompatActivity) requireActivity())
                                .getSupportActionBar())
                        .setDisplayShowTitleEnabled(true);
            }

            @Override
            public void onHomeClick() {
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        configuraUi(viewModel.getDataSet());
        if(viewModel.isAguardandoAtualizacao()){
            atualizaPorBuscaNaData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void atualizaPorBuscaNaData() {
        dataUseCase.solicitaAtualizacaoDeDados(
                sharedActivityViewModel.getDataInicial(),
                sharedActivityViewModel.getDataFinal()
        );
    }

    public void notificaQueHouveAlteracaoNaData() {
        viewModel.setAguardandoAtualizacao(true);
    }
}
package br.com.transporte.AppGhn.ui.activity.freteAReceberActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityFreteAreceberBinding;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.RecebimentoDeFreteRepository;
import br.com.transporte.AppGhn.ui.activity.freteAReceberActivity.helpers.FreteAReceberActMenuProviderHelper;
import br.com.transporte.AppGhn.ui.activity.freteAReceberActivity.helpers.FreteAReceberGetFragmentsHelper;
import br.com.transporte.AppGhn.ui.activity.freteAReceberActivity.helpers.FreteAReceberViewModelObserverHelper;
import br.com.transporte.AppGhn.ui.activity.util.StatusBarUtil;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.CarregandoDadosFreteReceberFragment;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberPagosFragment;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberResumoFragment;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.freteEmAberto.FreteAReceberEmAbertoFragment;
import br.com.transporte.AppGhn.ui.viewmodel.FreteAReceberActViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.FreteAReceberActViewModelFactory;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DateRangePickerUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class FreteAReceberActivity extends AppCompatActivity {
    public static final String FRETE_JA_RECEBIDO = "Frete já recebido";
    public static final String FRETE_EM_ABERTO = "Frete em aberto";
    public static final String DETALHES_DO_FRETE = "Detalhes do Frete";
    private ActivityFreteAreceberBinding binding;
    private FreteAReceberActViewModel viewModel;
    private FreteAReceberEmAbertoFragment fragFreteEmAberto;
    private FreteAReceberPagosFragment fragFreteJaPago;
    private FreteAReceberResumoFragment fragResumo;
    private FreteAReceberActMenuProviderHelper menuProviderHelper;
    private ToolbarUtil toolbarUtil;
    private BottomNavigationView bottomNavigation;
    private CarregandoDadosFreteReceberFragment fragCarregandoDados;
    private LinearLayout dataLayout;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFreteAreceberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configuraViewModel();
        pegaReferenciaDeFragmentsAoAtachar();
        configuraMenuProviderHelper();
        configuraDateRanger();
        viewModelObserverHelper();
        configuraToolbar();
        configuraBottomNavigation();
    }

    private void configuraViewModel() {
        final FreteRepository freteRepository = new FreteRepository(this);
        final CavaloRepository cavaloRepository = new CavaloRepository(this);
        final RecebimentoDeFreteRepository recebimentoRepository = new RecebimentoDeFreteRepository(this);
        final FreteAReceberActViewModelFactory factory = new FreteAReceberActViewModelFactory(freteRepository, cavaloRepository, recebimentoRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(FreteAReceberActViewModel.class);
    }

    private void pegaReferenciaDeFragmentsAoAtachar() {
        final FreteAReceberGetFragmentsHelper getFragments = new FreteAReceberGetFragmentsHelper(getSupportFragmentManager());
        getFragments.run(new FreteAReceberGetFragmentsHelper.GetFragmentsCallback() {
            @Override
            public void fragment_carregaDados(CarregandoDadosFreteReceberFragment fragment) {
                fragCarregandoDados = fragment;
            }

            @Override
            public void fragment_FreteEmAberto(FreteAReceberEmAbertoFragment fragment) {
                fragFreteEmAberto = fragment;
            }

            @Override
            public void fragment_FretePago(FreteAReceberPagosFragment fragment) {
                fragFreteJaPago = fragment;
            }

            @Override
            public void fragment_resumo(FreteAReceberResumoFragment fragment) {
                fragResumo = fragment;
            }
        });
    }

    private void viewModelObserverHelper() {
        final FreteAReceberViewModelObserverHelper viewModelObserverHelper =
                new FreteAReceberViewModelObserverHelper(this, viewModel);
        viewModelObserverHelper.configuraObservers();
        viewModelObserverHelper.setCallback(new FreteAReceberViewModelObserverHelper.ObserverHelperCallback() {
            @Override
            public void dadosPreparadosParaInicializacao() {
                fragCarregandoDados.finaliza();
                menuProviderHelper.defineDataSetCavalo(viewModel.getDataSet_cavalo());
            }

            @Override
            public void notificaAtualizacao() {
                atualizaFragments();
            }
        });
    }

    private void configuraToolbar() {
        final Toolbar toolbar = binding.toolbarLayout.toolbar;
        toolbarUtil = new ToolbarUtil("");
        toolbarUtil.configuraToolbar(this, toolbar);
        StatusBarUtil.setStatusBarColor(this, getWindow());
    }

    private void configuraMenuProviderHelper() {
        final Context context = this;
        menuProviderHelper = new FreteAReceberActMenuProviderHelper();
        addMenuProvider(menuProviderHelper, this, Lifecycle.State.RESUMED);
        menuProviderHelper.setCallback(new FreteAReceberActMenuProviderHelper.MenuProviderCallback() {
            @Override
            public void onSearchClick() {
                toolbarUtil.setTitleAtivo(false);
                bottomNavigation.setVisibility(GONE);
                AnimationUtil.defineAnimacao(context, R.anim.slide_out_bottom, bottomNavigation);
            }

            @Override
            public void onSearchRemoved() {
                toolbarUtil.setTitleAtivo(true);
                bottomNavigation.setVisibility(VISIBLE);
                AnimationUtil.defineAnimacao(context, R.anim.navigation_bar_slide_in_bottom, bottomNavigation);
            }

            @Override
            public void realizaBusca(List<Frete> dataSet_search) {
                exibeResultadoDaBuscaNosFragments(dataSet_search);
            }

            private void exibeResultadoDaBuscaNosFragments(List<Frete> dataSet_search) {
                if (fragFreteEmAberto.isVisible()) {
                    fragFreteEmAberto.exibeResultadoDeBusca(dataSet_search);
                } else if (fragFreteJaPago.isVisible()) {
                    fragFreteJaPago.exibeResultadoDeBusca(dataSet_search);
                }
            }

            @Override
            public void onLogoutCLick() {
                MensagemUtil.toast(context, LOGOUT);
            }

            @Override
            public void onHomeClick() {
                if (fragResumo != null && fragResumo.isVisible()) {
                    fragResumo.popBackStack();
                } else {
                    finish();
                }
            }

            @Override
            public void onEditClick() {
                fragResumo.navegaParaFormulario();
            }
        });
    }

    private void configuraDateRanger() {
        dataLayout = binding.datePickerLayout.layoutData;
        TextView campoDataInicial = binding.datePickerLayout.dataInicial;
        TextView campoDataFinal = binding.datePickerLayout.dataFinal;

        final DateRangePickerUtil datePickerUtil = new DateRangePickerUtil(getSupportFragmentManager());
        datePickerUtil.configuraCampos(campoDataInicial, campoDataFinal);
        datePickerUtil.build(dataLayout);
        datePickerUtil.setCallbackDatePicker(
                (dataInicial, dataFinal) -> {
                    campoDataInicial.setText(ConverteDataUtil.dataParaString(dataInicial));
                    campoDataFinal.setText(ConverteDataUtil.dataParaString(dataFinal));
                    viewModel.setDataInicial(dataInicial);
                    viewModel.setDataFinal(dataFinal);
                    viewModel.separaDataSet_pagos_E_emAberto();
                    atualizaFragments();
                });
    }

    private void atualizaFragments() {
        if (fragFreteEmAberto != null) {
            if (fragFreteEmAberto.isVisible()) {
                fragFreteEmAberto.actSolicitaAtualizacao();
            }
        }
        if (fragFreteJaPago != null) {
            if (fragFreteJaPago.isVisible()) {
                fragFreteJaPago.actSolicitaAtualizacao();
            }
        }
        if (fragResumo != null) {
            if (fragResumo.isVisible()) {
                fragResumo.actSolicitaAtualizacao();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraBottomNavigation() {
        bottomNavigation = binding.bottomNavigation;
        NavController controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_frete_receber);
        NavigationUI.setupWithNavController(bottomNavigation, controlador);
        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.carregandoDadosFreteReceberFragment:
                    Objects.requireNonNull(getSupportActionBar()).hide();
                    break;

                case R.id.navFreteReceber:
                    if (!Objects.requireNonNull(getSupportActionBar()).isShowing()) {
                        Objects.requireNonNull(getSupportActionBar()).show();
                    }
                    if (menuProviderHelper.edita.isVisible()) {
                        menuProviderHelper.busca.setVisible(true);
                        menuProviderHelper.busca.setEnabled(true);
                        menuProviderHelper.edita.setVisible(false);
                        menuProviderHelper.edita.setEnabled(false);
                    }
                    exibeBottomNavigation();
                    exibeDataLayout();
                    toolbarUtil.setTitle(FRETE_EM_ABERTO);
                    menuProviderHelper.defineDataSetFrete(viewModel.getDataSet_freteEmAberto());
                    break;

                case R.id.navFreteReceberPago:
                    if (menuProviderHelper.edita.isVisible()) {
                        menuProviderHelper.busca.setVisible(true);
                        menuProviderHelper.busca.setEnabled(true);
                        menuProviderHelper.edita.setVisible(false);
                        menuProviderHelper.edita.setEnabled(false);
                    }
                    exibeBottomNavigation();
                    exibeDataLayout();
                    toolbarUtil.setTitle(FRETE_JA_RECEBIDO);
                    menuProviderHelper.defineDataSetFrete(viewModel.getDataSet_freteRecebido());
                    break;

                case R.id.navFreteAReceberResumo:
                    toolbarUtil.setTitle(DETALHES_DO_FRETE);
                    bottomNavigation.setVisibility(GONE);
                    dataLayout.setVisibility(GONE);
                    preparaToolbarParaFragmentResumo();
                    break;
            }
        });
    }

    private void preparaToolbarParaFragmentResumo() {
        menuProviderHelper.busca.setVisible(false);
        menuProviderHelper.busca.setEnabled(false);
        menuProviderHelper.edita.setVisible(true);
        menuProviderHelper.edita.setEnabled(true);
    }

    private void exibeDataLayout() {
        if (dataLayout.getVisibility() == GONE) {
            dataLayout.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(this, R.anim.data_layout_slide_in_top, dataLayout);
        }
    }

    private void exibeBottomNavigation() {
        if (bottomNavigation.getVisibility() == GONE) {
            bottomNavigation.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(this, R.anim.navigation_bar_slide_in_bottom, bottomNavigation);
        }
    }

}

package br.com.transporte.appGhn.ui.activity.freteAReceberActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.RESULT_LOGOUT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityFreteAreceberBinding;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.FreteRepository;
import br.com.transporte.appGhn.repository.RecebimentoDeFreteRepository;
import br.com.transporte.appGhn.ui.activity.freteAReceberActivity.helpers.FreteAReceberActMenuProviderHelper;
import br.com.transporte.appGhn.ui.activity.freteAReceberActivity.helpers.FreteAReceberGetFragmentsHelper;
import br.com.transporte.appGhn.ui.activity.freteAReceberActivity.helpers.FreteAReceberViewModelObserverHelper;
import br.com.transporte.appGhn.ui.fragment.freteReceber.CarregandoDadosFreteReceberFragment;
import br.com.transporte.appGhn.ui.fragment.freteReceber.FreteAReceberPagosFragment;
import br.com.transporte.appGhn.ui.fragment.freteReceber.FreteAReceberResumoFragment;
import br.com.transporte.appGhn.ui.fragment.freteReceber.freteEmAberto.FreteAReceberEmAbertoFragment;
import br.com.transporte.appGhn.ui.viewmodel.FreteAReceberActViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.FreteAReceberActViewModelFactory;
import br.com.transporte.appGhn.util.AnimationUtil;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.DateRangePickerUtil;

public class FreteAReceberActivity extends AppCompatActivity {
    public static final String FRETE_RECEBIDO = "Frete recebido";
    public static final String FRETE_EM_ABERTO = "Frete em aberto";
    public static final String DETALHES = "Detalhes";
    private ActivityFreteAreceberBinding binding;
    private FreteAReceberActViewModel viewModel;
    private FreteAReceberEmAbertoFragment fragFreteEmAberto;
    private FreteAReceberPagosFragment fragFreteJaPago;
    private FreteAReceberResumoFragment fragResumo;
    private FreteAReceberActMenuProviderHelper menuProviderHelper;
    private BottomNavigationView bottomNavigation;
    private CarregandoDadosFreteReceberFragment fragCarregandoDados;
    private LinearLayout dataLayout;
    private ActionBar supportActionBar;

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

    private void configuraMenuProviderHelper() {
        final Context context = this;
        menuProviderHelper = new FreteAReceberActMenuProviderHelper();
        addMenuProvider(menuProviderHelper, this, Lifecycle.State.RESUMED);
        menuProviderHelper.setCallback(new FreteAReceberActMenuProviderHelper.MenuProviderCallback() {
            @Override
            public void onSearchClick() {
               if(supportActionBar != null){
                   supportActionBar.setDisplayShowTitleEnabled(false);
               }

                bottomNavigation.setVisibility(GONE);
                AnimationUtil.defineAnimacao(context, R.anim.slide_out_bottom, bottomNavigation);
            }

            @Override
            public void onSearchRemoved() {
                if(supportActionBar != null){
                    supportActionBar.setDisplayShowTitleEnabled(false);
                }

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
                setResult(RESULT_LOGOUT);
                finish();
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
        supportActionBar = getSupportActionBar();

        bottomNavigation = binding.bottomNavigation;
        NavController controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_frete_receber);
        NavigationUI.setupWithNavController(bottomNavigation, controlador);
        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.carregandoDadosFreteReceberFragment:
                    if(supportActionBar != null) {
                        supportActionBar.hide();

                    }
                    break;

                case R.id.navFreteReceber:
                    if(supportActionBar != null) {
                        supportActionBar.show();
                        supportActionBar.setTitle(FRETE_EM_ABERTO);
                        supportActionBar.setDisplayHomeAsUpEnabled(true);
                        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                    }

                    if (menuProviderHelper.edita.isVisible()) {
                        menuProviderHelper.busca.setVisible(true);
                        menuProviderHelper.busca.setEnabled(true);
                        menuProviderHelper.edita.setVisible(false);
                        menuProviderHelper.edita.setEnabled(false);
                    }

                    exibeBottomNavigation();
                    exibeDataLayout();
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
                    if (supportActionBar != null) {
                        supportActionBar.setTitle(FRETE_RECEBIDO);
                    }

                    menuProviderHelper.defineDataSetFrete(viewModel.getDataSet_freteRecebido());
                    break;

                case R.id.navFreteAReceberResumo:
                    if (supportActionBar != null) {
                        supportActionBar.setTitle(DETALHES);
                    }
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


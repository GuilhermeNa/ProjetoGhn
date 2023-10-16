package br.com.transporte.AppGhn.ui.activity.despesaAdm;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityDespesasAdmBinding;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.DespesasActOnFragmentsAttachExt;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.viewmodel.DespesaAdmActViewModel;
import br.com.transporte.AppGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.AppGhn.ui.activity.utilActivity.FabAnimatorUtil;
import br.com.transporte.AppGhn.ui.activity.utilActivity.StatusBarUtil;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.DespesasAdmDiretasFragment;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.indireta.DespesasAdmIndiretasFragment;
import br.com.transporte.AppGhn.util.DateRangePickerUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class DespesasAdmActivity extends AppCompatActivity {
    public static final String DESPESAS_INDIRETAS = "Despesas indiretas";
    public static final String DESPESAS_DIRETAS = "Despesas diretas";
    private ActivityDespesasAdmBinding binding;
    private DespesaAdmActViewModel viewModel;
    private ToolbarUtil toolbarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDespesasAdmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaViewModel();
        getFragmentsNoOnAttachListener();
        configuraToolbar();
        configuraNavControler();
        configuraDatePicker();
        configuraFabs();
        configuraUiDoCampoData();
    }

    private void inicializaViewModel() {
        final ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(DespesaAdmActViewModel.class);
    }

    private void getFragmentsNoOnAttachListener() {
        final DespesasActOnFragmentsAttachExt fragmentsHelper = new DespesasActOnFragmentsAttachExt(getSupportFragmentManager());
        fragmentsHelper.getFragmentsWhenAttach(new DespesasActOnFragmentsAttachExt.DespesasFragmentsCallback() {
            @Override
            public void despesaDiretaFragmentAttached(DespesasAdmDiretasFragment fragment) {
                viewModel.setFragmentDespesaDireta(fragment);
            }

            @Override
            public void despesaIndiretaFragmentAttached(DespesasAdmIndiretasFragment fragment) {
                viewModel.setFragmentDespesaIndireta(fragment);
            }
        });
    }

    private void configuraToolbar() {
        final Toolbar toolbar = binding.toolbarLayout.toolbar;
        toolbarUtil = new ToolbarUtil(DESPESAS_INDIRETAS);
        toolbarUtil.configuraToolbar(this, toolbar);
        StatusBarUtil.setStatusBarColor(this, getWindow());
    }

    private void configuraDatePicker() {
        final DateRangePickerUtil datePicker = new DateRangePickerUtil(getSupportFragmentManager());
        datePicker.build(binding.dataLayout.layoutData);
        datePicker.setCallbackDatePicker((dataInicial, dataFinal) -> {
            viewModel.setDataInicial(dataInicial);
            viewModel.setDataFinal(dataFinal);
            configuraUiDoCampoData();
            notificaFragmentQueHouveAlteracaoNaData();
        });
    }

    private void notificaFragmentQueHouveAlteracaoNaData() {
        if (viewModel.getFragmentDespesaDireta() != null) {
            if (viewModel.getFragmentDespesaDireta().isVisible()) {
                viewModel.getFragmentDespesaDireta()
                        .atualizaPorBuscaNaData();
            } else {
                viewModel.getFragmentDespesaDireta()
                        .notificaQueHouveAlteracaoNaData();
            }
        }
        if (viewModel.getFragmentDespesaIndireta() != null) {
            if (viewModel.getFragmentDespesaIndireta().isVisible()) {
                viewModel.getFragmentDespesaIndireta()
                        .atualizaPorBuscaNaData();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraNavControler() {
        final NavController controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_despesa);
        final BottomNavigationView bottomNavigation = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomNavigation, controlador);
        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.navDespesasDiretas:
                    toolbarUtil.setTitle(DESPESAS_INDIRETAS);
                    break;

                case R.id.navDespesasIndiretas:
                    toolbarUtil.setTitle(DESPESAS_DIRETAS);
                    break;
            }
        });
    }

    private void configuraFabs() {
        final FloatingActionButton fabPrincipal = binding.fab;
        final FloatingActionButton fabDireto = binding.fabDireta;
        final FloatingActionButton fabIndireto = binding.fabIndireta;
        final FabAnimatorUtil fabAnimatorExt = new FabAnimatorUtil(this, fabDireto, fabIndireto);

        fabPrincipal.setOnClickListener(v ->
                fabAnimatorExt.animaFabs(viewModel.statusDeVisibilidadeDoFab(),
                        () -> viewModel.alteraStatusDeVisibilidadeDoFab()
                ));

        fabDireto.setOnClickListener(v -> {
            fabAnimatorExt.animaFabs(viewModel.statusDeVisibilidadeDoFab(),
                    () -> viewModel.alteraStatusDeVisibilidadeDoFab());
            navegaParaFormulario(DIRETA);
        });

        fabIndireto.setOnClickListener(v -> {
            fabAnimatorExt.animaFabs(viewModel.statusDeVisibilidadeDoFab(),
                    () -> viewModel.alteraStatusDeVisibilidadeDoFab());
            navegaParaFormulario(INDIRETA);
        });

    }

    private void navegaParaFormulario(final TipoDespesa tipoDeDespesa) {
        Intent intent = new Intent(this, FormulariosActivity.class);
        intent.putExtra(CHAVE_FORMULARIO, VALOR_DESPESA_ADM);
        intent.putExtra(CHAVE_TIPO_DESPESA, tipoDeDespesa);
        startActivity(intent);
    }

    private void configuraUiDoCampoData() {
        try {
            BindData.fromLocalDate(
                    binding.dataLayout.dataInicial,
                    viewModel.getDataInicial());

            BindData.fromLocalDate(
                    binding.dataLayout.dataFinal,
                    viewModel.getDataFinal());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }


    }

}
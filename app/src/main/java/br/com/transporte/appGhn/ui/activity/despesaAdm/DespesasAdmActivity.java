package br.com.transporte.appGhn.ui.activity.despesaAdm;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityDespesasAdmBinding;
import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.appGhn.ui.activity.despesaAdm.extensions.DespesasActOnFragmentsAttachExt;
import br.com.transporte.appGhn.ui.activity.despesaAdm.viewmodel.DespesaAdmActViewModel;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.activity.utilActivity.FabAnimatorUtil;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.DespesasAdmDiretasFragment;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.indireta.DespesasAdmIndiretasFragment;
import br.com.transporte.appGhn.util.DateRangePickerUtil;

public class DespesasAdmActivity extends AppCompatActivity {
    public static final String DESPESAS_INDIRETAS = "Despesas indiretas";
    public static final String DESPESAS_DIRETAS = "Despesas diretas";
    private ActivityDespesasAdmBinding binding;
    private DespesaAdmActViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDespesasAdmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaViewModel();
        getFragmentsNoOnAttachListener();
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
        final ActionBar supportActionBar = getSupportActionBar();

        final NavController controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_despesa);
        final BottomNavigationView bottomNavigation = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomNavigation, controlador);
        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.navDespesasDiretas:
                    if (supportActionBar != null) {
                        supportActionBar.setTitle(DESPESAS_INDIRETAS);
                        supportActionBar.setDisplayHomeAsUpEnabled(true);
                    }
                    break;

                case R.id.navDespesasIndiretas:
                    if (supportActionBar != null) {
                        supportActionBar.setTitle(DESPESAS_DIRETAS);
                    }
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
        final Intent intent = new Intent(this, FormulariosActivity.class);
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
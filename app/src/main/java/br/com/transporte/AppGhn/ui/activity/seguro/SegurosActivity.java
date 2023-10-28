package br.com.transporte.AppGhn.ui.activity.seguro;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivitySegurosBinding;
import br.com.transporte.AppGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.AppGhn.ui.activity.seguro.extensions.SegurosActFragmentsResultListenerExt;
import br.com.transporte.AppGhn.ui.activity.seguro.viewmodel.SeguroActViewModel;
import br.com.transporte.AppGhn.ui.activity.utilActivity.FabAnimatorUtil;
import br.com.transporte.AppGhn.ui.activity.utilActivity.StatusBarUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class SegurosActivity extends AppCompatActivity {

    private ActivitySegurosBinding binding;
    private FloatingActionButton fabPrincipal;
    private SeguroActViewModel viewModel;
    private ToolbarUtil toolbarUtil;
    private BottomNavigationView bottomAppBar;
    private Menu menu;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();

    @NonNull
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    switch (resultCode) {
                        case RESULT_OK:
                            MensagemUtil.toast(this, REGISTRO_CRIADO);
                            break;
                        case RESULT_CANCELED:
                            MensagemUtil.toast(this, NENHUMA_ALTERACAO_REALIZADA);
                            break;
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySegurosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaViewModel();
        configuraEventosDeListenersParaFragments();
        configuraToolbar();
        configuraFabs();
        configuraBottomNavigation();
    }

    private void inicializaViewModel() {
        final ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(SeguroActViewModel.class);
    }

    private void configuraEventosDeListenersParaFragments() {
        final NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container_seguros);
        final SegurosActFragmentsResultListenerExt fragmentsListenerExt =
                new SegurosActFragmentsResultListenerExt(navHost, this);

        fragmentsListenerExt.getResult(
                new SegurosActFragmentsResultListenerExt.SegurosActFragmentsResultListener() {
                    @Override
                    public void onMenuProviderSearchViewClick() {
                        toolbarUtil.setTitleAtivo(false);
                        bottomAppBar.setVisibility(GONE);
                        fabPrincipal.setVisibility(GONE);
                        menu.findItem(R.id.menu_padrao_logout)
                                .setVisible(false);
                    }

                    @Override
                    public void onMenuProviderSearchViewClear() {
                        toolbarUtil.setTitleAtivo(true);
                        bottomAppBar.setVisibility(VISIBLE);
                        fabPrincipal.setVisibility(VISIBLE);
                        menu.findItem(R.id.menu_padrao_logout)
                                .setVisible(true);
                    }
                });
    }

    private void configuraToolbar() {
        final Toolbar toolbar = binding.toolbarLayout.toolbar;
        toolbarUtil = new ToolbarUtil("");
        toolbarUtil.configuraToolbar(this, toolbar);
        StatusBarUtil.setStatusBarColor(this, getWindow());
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraBottomNavigation() {
        final NavController controlador =
                Navigation.findNavController(this, R.id.nav_host_fragment_container_seguros);
        bottomAppBar = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomAppBar, controlador);
        controlador.addOnDestinationChangedListener(
                (navController, navDestination, bundle) -> {
                    switch (navDestination.getId()) {
                        case R.id.navSeguroHome:
                            Objects.requireNonNull(getSupportActionBar()).hide();
                            break;
                        case R.id.navSeguroFrota:
                            Objects.requireNonNull(getSupportActionBar()).show();
                            toolbarUtil.setTitle("Seguro Frota");
                            bottomAppBar.setVisibility(VISIBLE);
                            fabPrincipal.setVisibility(VISIBLE);
                            break;
                        case R.id.navSeguroVida:
                            toolbarUtil.setTitle("Seguro Vida");
                            bottomAppBar.setVisibility(VISIBLE);
                            fabPrincipal.setVisibility(VISIBLE);
                            break;
                        case R.id.navSeguroInformacoes:
                            toolbarUtil.setTitle("Resumo");
                            bottomAppBar.setVisibility(GONE);
                            fabPrincipal.setVisibility(GONE);
                            break;
                    }
                });
    }

    private void configuraFabs() {
        fabPrincipal = binding.fab;
        final FloatingActionButton fabDemaisRamos = binding.fabDemaisRamos;
        final FloatingActionButton fabFrota = binding.fabFrota;
        final FabAnimatorUtil fabAnimatorExt =
                new FabAnimatorUtil(this, fabFrota, fabDemaisRamos);

        fabPrincipal.setOnClickListener(v ->
                fabAnimatorExt.animaFabs(viewModel.statusDeVisibilidadeDoFab(),
                        () -> viewModel.alteraStatusDeVisibilidadeDoFab()
                ));

        fabDemaisRamos.setOnClickListener(v -> {
            fabAnimatorExt.animaFabs(viewModel.statusDeVisibilidadeDoFab(),
                    () -> viewModel.alteraStatusDeVisibilidadeDoFab());
            navegaParaFormulario(VALOR_SEGURO_VIDA);
        });

        fabFrota.setOnClickListener(v -> {
            fabAnimatorExt.animaFabs(viewModel.statusDeVisibilidadeDoFab(),
                    () -> viewModel.alteraStatusDeVisibilidadeDoFab());
            navegaParaFormulario(VALOR_SEGURO_FROTA);
        });
    }

    private void navegaParaFormulario(final int valorSeguro) {
        final Intent intent = new Intent(this, FormulariosActivity.class);
        intent.putExtra(CHAVE_FORMULARIO, valorSeguro);
        intent.putExtra(CHAVE_REQUISICAO, ADICIONANDO);
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        this.menu = menu;
        menu.removeItem(R.id.menu_padrao_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
package br.com.transporte.appGhn.ui.activity.seguro;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_FROTA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_SEGURO_VIDA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivitySegurosBinding;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.activity.seguro.extensions.SegurosActFragmentsResultListenerExt;
import br.com.transporte.appGhn.ui.activity.seguro.viewmodel.SeguroActViewModel;
import br.com.transporte.appGhn.ui.activity.utilActivity.FabAnimatorUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class SegurosActivity extends AppCompatActivity {
    private ActivitySegurosBinding binding;
    private FloatingActionButton fabPrincipal;
    private SeguroActViewModel viewModel;
    private BottomNavigationView bottomAppBar;
    private Menu menu;
    private final ActivityResultLauncher<Intent> activityResultLauncher = getActivityResultLauncher();
    private ActionBar supportActionBar;

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
                        supportActionBar.setDisplayShowTitleEnabled(false);
                        bottomAppBar.setVisibility(GONE);
                        fabPrincipal.setVisibility(GONE);
                        menu.findItem(R.id.menu_padrao_logout)
                                .setVisible(false);
                    }

                    @Override
                    public void onMenuProviderSearchViewClear() {
                        supportActionBar.setDisplayShowTitleEnabled(true);
                        bottomAppBar.setVisibility(VISIBLE);
                        fabPrincipal.setVisibility(VISIBLE);
                        menu.findItem(R.id.menu_padrao_logout)
                                .setVisible(true);
                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraBottomNavigation() {
        supportActionBar = getSupportActionBar();

        final NavController controlador =
                Navigation.findNavController(this, R.id.nav_host_fragment_container_seguros);
        bottomAppBar = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomAppBar, controlador);
        controlador.addOnDestinationChangedListener(
                (navController, navDestination, bundle) -> {
                    switch (navDestination.getId()) {
                        case R.id.navSeguroHome:
                            if (supportActionBar != null) {
                                supportActionBar.hide();
                            }
                            break;

                        case R.id.navSeguroFrota:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.show();
                                supportActionBar.setTitle("Seguro Frota");
                            }

                            bottomAppBar.setVisibility(VISIBLE);
                            fabPrincipal.setVisibility(VISIBLE);
                            break;

                        case R.id.navSeguroVida:
                            if (supportActionBar != null) {
                                supportActionBar.setTitle("Seguro Vida");
                            }

                            bottomAppBar.setVisibility(VISIBLE);
                            fabPrincipal.setVisibility(VISIBLE);
                            break;

                        case R.id.navSeguroInformacoes:
                            if (supportActionBar != null) {
                                supportActionBar.setTitle("Resumo");
                            }

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
package br.com.transporte.appGhn.ui.activity.certificado;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.appGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CERTIFICADOS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityCertificadosBinding;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CertificadoRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.activity.certificado.helpers.CertificadoViewModelObserverHelper;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.fragment.certificados.CarregandoDadosCertificadoFragment;
import br.com.transporte.appGhn.ui.fragment.certificados.CertificadoDiretosDetalhesFragment;
import br.com.transporte.appGhn.ui.fragment.certificados.CertificadosDiretosFragment;
import br.com.transporte.appGhn.ui.fragment.certificados.CertificadosIndiretosFragment;
import br.com.transporte.appGhn.ui.viewmodel.CertificadoActViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.CertificadosActViewModelFactory;
import br.com.transporte.appGhn.util.AnimationUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class CertificadosActivity extends AppCompatActivity {
    public static final String SEARCH_CLICK = "search_click";
    public static final String ACT_LISTENER = "act_listener";
    public static final String CERTIFICADOS = "Certificados";
    private FloatingActionButton fabIndireta, fabDireta;
    private ActivityCertificadosBinding binding;
    private FloatingActionButton fabPrincipal;
    private boolean fabEscondido = true;
    private CertificadoActViewModel viewModel;
    private CarregandoDadosCertificadoFragment fragCarregaDados;
    private Fragment fragmentVisivel;
    private BottomNavigationView navigationView;
    private final ActivityResultLauncher<Intent> activityResultLauncherFormularioCertificadoIndireto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_OK:
                        MensagemUtil.toast(this, REGISTRO_CRIADO);
                        break;

                    case RESULT_CANCELED:
                        MensagemUtil.toast(this, NENHUMA_ALTERACAO_REALIZADA);
                        break;
                }
            });
    private final ActivityResultLauncher<Intent> activityResultLauncherFormularioCertificadoDireto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_OK:
                        MensagemUtil.toast(this, REGISTRO_CRIADO);
                        break;

                    case RESULT_CANCELED:
                        MensagemUtil.toast(this, NENHUMA_ALTERACAO_REALIZADA);
                        break;
                }
            });

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCertificadosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaCamposDaViewCompartilhadosEntreMetodos();
        pegaReferenciaDeFragmentAoAtachar();
        configuraListenersParaFragments();
        inicializaViewModel();
        configuraViewModelObserverHelper();
        configuraFabs();
        configuraBottomNavigation();
    }

    private void configuraListenersParaFragments() {
        final NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_container_certificados);
        if (navHost != null) {
            navHost.getChildFragmentManager().setFragmentResultListener(ACT_LISTENER, this,
                    (requestKey, result) -> {
                        if (requestKey.equals(ACT_LISTENER)) {
                            if (result.containsKey(SEARCH_CLICK)) {
                                boolean onSearchCLicked = result.getBoolean(SEARCH_CLICK);
                                if (onSearchCLicked) {
                                    navigationView.setVisibility(GONE);
                                    fabPrincipal.setVisibility(GONE);
                                } else {
                                    fabPrincipal.setVisibility(VISIBLE);
                                    AnimationUtil.defineAnimacao(this, R.anim.navigation_bar_slide_in_bottom, navigationView);
                                    navigationView.setVisibility(VISIBLE);
                                    AnimationUtil.defineAnimacao(this, R.anim.navigation_bar_slide_in_bottom, fabPrincipal);
                                }
                            }
                        }
                    });
        }
    }

    private void pegaReferenciaDeFragmentAoAtachar() {
        final CertificadosGetFragmentsOnAtach getFragments = new CertificadosGetFragmentsOnAtach(getSupportFragmentManager());
        getFragments.run(new CertificadosGetFragmentsOnAtach.CertificadoGetFragmentsCallback() {
            @Override
            public void fragment_carregaDados(CarregandoDadosCertificadoFragment fragment) {
                fragCarregaDados = fragment;
            }

            @Override
            public void fragment_certificadoDireto(CertificadosDiretosFragment fragment) {
                fragmentVisivel = fragment;
            }

            @Override
            public void fragment_certificadoDiretoDetalhe(CertificadoDiretosDetalhesFragment fragment) {
                fragmentVisivel = fragment;
            }

            @Override
            public void fragment_certificadoIndireto(CertificadosIndiretosFragment fragment) {
                fragmentVisivel = fragment;
            }
        });
    }

    private void inicializaViewModel() {
        final CertificadoRepository certificadoRepository = new CertificadoRepository(this);
        final CavaloRepository cavaloRepository = new CavaloRepository(this);
        final MotoristaRepository motoristaRepository = new MotoristaRepository(this);
        final CertificadosActViewModelFactory factory = new CertificadosActViewModelFactory(certificadoRepository, cavaloRepository, motoristaRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(CertificadoActViewModel.class);
    }

    private void configuraViewModelObserverHelper() {
        final ActCertificadoFragmentsUpdateManager fragmentsUpdateManager = new ActCertificadoFragmentsUpdateManager();
        final CertificadoViewModelObserverHelper viewModelObserver = new CertificadoViewModelObserverHelper(viewModel, this);
        viewModelObserver.configuraObservers();
        viewModelObserver.setCallback(new CertificadoViewModelObserverHelper.ObserverHelperCallback() {
            @Override
            public void dadosPreparadosParaInicializacao() {
                fragCarregaDados.finaliza();
            }

            @Override
            public void certificadosCarregados() {
                fragmentsUpdateManager.enviaData(fragmentVisivel);
            }
        });
    }

    private void inicializaCamposDaViewCompartilhadosEntreMetodos() {
        fabDireta = binding.fabDireta;
        fabIndireta = binding.fabIndireta;
        fabPrincipal = binding.fab;
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraBottomNavigation() {
        final ActionBar supportActionBar = getSupportActionBar();

        final Animation animacima =
                AnimationUtils.loadAnimation(this, R.anim.slide_in);

        final Animation animabaixo =
                AnimationUtils.loadAnimation(this, R.anim.slide_out);

        navigationView = binding.bottomNavigation;
        final NavController controlador =
                Navigation.findNavController(this, R.id.nav_host_fragment_container_certificados);
        NavigationUI.setupWithNavController(navigationView, controlador);

        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.navCarregaDadosCertificado:
                    if (supportActionBar != null) {
                        supportActionBar.hide();
                    }

                    navigationView.setVisibility(GONE);
                    fabPrincipal.setVisibility(GONE);
                    break;

                case R.id.navCertificadosDiretos:
                    if (supportActionBar != null) {
                        supportActionBar.show();
                        supportActionBar.setDisplayHomeAsUpEnabled(true);
                        supportActionBar.setTitle(CERTIFICADOS);
                    }

                    navigationView.setVisibility(VISIBLE);
                    fabPrincipal.setVisibility(VISIBLE);
                    break;

                case R.id.navCertificadosIndiretos:
                    if (fabDireta.getVisibility() == VISIBLE) {
                        animacaoFabs();
                    }

                    if (navigationView.getVisibility() == GONE) {
                        navigationView.setVisibility(VISIBLE);
                        fabPrincipal.setVisibility(VISIBLE);
                        navigationView.startAnimation(animacima);
                        fabPrincipal.startAnimation(animacima);
                    }
                    break;

                case R.id.navCertificadosDiretosDetalhes:

                    if (fabDireta.getVisibility() == VISIBLE) {
                        animacaoFabs();
                    }

                    navigationView.setVisibility(GONE);
                    fabPrincipal.setVisibility(GONE);
                    navigationView.startAnimation(animabaixo);
                    fabPrincipal.startAnimation(animabaixo);
                    break;
            }
        });
    }

    private void configuraFabs() {
        fabPrincipal.setOnClickListener(v -> animacaoFabs());

        fabDireta.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_REQUISICAO, ADICIONANDO);
            intent.putExtra(CHAVE_DESPESA, DIRETA);
            activityResultLauncherFormularioCertificadoDireto.launch(intent);
        });

        fabIndireta.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CERTIFICADOS);
            intent.putExtra(CHAVE_REQUISICAO, ADICIONANDO);
            intent.putExtra(CHAVE_DESPESA, INDIRETA);
            activityResultLauncherFormularioCertificadoIndireto.launch(intent);
        });
    }

    private void animacaoFabs() {
        Animation animFabNoroesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_abre);
        Animation animFabNoroesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_fecha);
        Animation animFabNordesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_abre);
        Animation animFabNordesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_fecha);

        if (fabEscondido) {
            fabDireta.setVisibility(VISIBLE);
            fabIndireta.setVisibility(VISIBLE);
            fabDireta.startAnimation(animFabNoroesteAbre);
            fabIndireta.startAnimation(animFabNordesteAbre);

        } else {
            fabDireta.setVisibility(View.INVISIBLE);
            fabIndireta.setVisibility(View.INVISIBLE);
            fabDireta.startAnimation(animFabNoroesteFecha);
            fabIndireta.startAnimation(animFabNordesteFecha);
        }
        fabEscondido = !fabEscondido;
    }

}

class CertificadosGetFragmentsOnAtach {
    private final FragmentManager fragmentManager;

    public CertificadosGetFragmentsOnAtach(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public interface CertificadoGetFragmentsCallback {
        void fragment_carregaDados(CarregandoDadosCertificadoFragment fragment);

        void fragment_certificadoDireto(CertificadosDiretosFragment fragment);

        void fragment_certificadoDiretoDetalhe(CertificadoDiretosDetalhesFragment fragment);

        void fragment_certificadoIndireto(CertificadosIndiretosFragment fragment);

    }

    //----------------------------------------------------------------------------------------------

    public void run(final CertificadoGetFragmentsCallback callback) {
        final NavHostFragment navHost = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_container_certificados);
        if (navHost != null) {
            getFragmentDeAbertura(navHost, callback);
            getDemaisFragments(navHost, callback);
        }
    }

    private void getFragmentDeAbertura(
            @NonNull final NavHostFragment navHost,
            @NonNull final CertificadoGetFragmentsCallback callback
    ) {
        final CarregandoDadosCertificadoFragment fragment =
                (CarregandoDadosCertificadoFragment) navHost.getChildFragmentManager().getFragments().get(0);
        callback.fragment_carregaDados(fragment);
    }

    private void getDemaisFragments(
            @NonNull final NavHostFragment navHost,
            final CertificadoGetFragmentsCallback callback
    ) {
        navHost.getChildFragmentManager().addFragmentOnAttachListener(
                (fragmentManager, fragment) -> {
                    getFragment_certificadoDireto(callback, fragment);
                    getFragment_certificadoDiretoDetalhe(callback, fragment);
                    getFragment_certificadoIndireto(callback, fragment);
                });
    }

    private void getFragment_certificadoIndireto(
            final CertificadoGetFragmentsCallback callback,
            final Fragment fragment
    ) {
        if (fragment instanceof CertificadosIndiretosFragment) {
            final CertificadosIndiretosFragment fragCertificadoIndireto = (CertificadosIndiretosFragment) fragment;
            callback.fragment_certificadoIndireto(fragCertificadoIndireto);
        }
    }

    private void getFragment_certificadoDireto(
            final CertificadoGetFragmentsCallback callback,
            final Fragment fragment
    ) {
        if (fragment instanceof CertificadosDiretosFragment) {
            final CertificadosDiretosFragment fragCertificadoDireto = (CertificadosDiretosFragment) fragment;
            callback.fragment_certificadoDireto(fragCertificadoDireto);
        }
    }

    private static void getFragment_certificadoDiretoDetalhe(
            final CertificadoGetFragmentsCallback callback,
            final Fragment fragment
    ) {
        if (fragment instanceof CertificadoDiretosDetalhesFragment) {
            final CertificadoDiretosDetalhesFragment fragCertificadoDiretoDetalhe = (CertificadoDiretosDetalhesFragment) fragment;
            callback.fragment_certificadoDiretoDetalhe(fragCertificadoDiretoDetalhe);
        }
    }

}

class ActCertificadoFragmentsUpdateManager {
    public void enviaData(
            final Fragment fragmentVisivel
    ) {
        if (fragmentVisivel instanceof CertificadoDiretosDetalhesFragment)
            ((CertificadoDiretosDetalhesFragment) fragmentVisivel).solicitaAtualizacaoAdapter();

        else if (fragmentVisivel instanceof CertificadosIndiretosFragment)
            ((CertificadosIndiretosFragment) fragmentVisivel).solicitaAtualizacaoAdapter();

        else if (fragmentVisivel instanceof CertificadosDiretosFragment)
            ((CertificadosDiretosFragment) fragmentVisivel).solicitaAtualizacaoAdapter();
    }
}



package br.com.transporte.appGhn.ui.activity.comissao;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.RESULT_LOGOUT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityComissoesBinding;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.abstracts.Frota;
import br.com.transporte.appGhn.model.custos.CustosDeSalario;
import br.com.transporte.appGhn.repository.AdiantamentoRepository;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CustoDePercursoRepository;
import br.com.transporte.appGhn.repository.CustoDeSalarioRepository;
import br.com.transporte.appGhn.repository.FreteRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.activity.comissao.helpers.ComissaoViewModelObserverHelper;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.CarregandoDadosComissaoFragment;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.ComissoesPagasDetalhesFragment;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesEmAberto.ComissoesEmAbertoFragment;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesPagas.ComissoesPagasFragment;
import br.com.transporte.appGhn.ui.viewmodel.ComissaoActViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.ComissaoActViewModelFactory;
import br.com.transporte.appGhn.util.AnimationUtil;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.DateRangePickerUtil;

public class ComissoesActivity extends AppCompatActivity {
    private ActivityComissoesBinding binding;
    private ComissaoActViewModel viewModel;
    private LinearLayout dataLayout;
    private CarregandoDadosComissaoFragment fragCarregando;
    private ComissoesEmAbertoFragment fragComissaoEmAberto;
    private ComissoesPagasFragment fragComissaoPaga;
    private ComissoesDetalhesFragment fragDetalhesParaPagamento;
    private ComissoesPagasDetalhesFragment fragResumoDosPagamentos;
    private Fragment fragmentVisivel;
    private BottomNavigationView navigationView;
    private ComissaoMenuProvider menuProvider;
    private ActComissao_fragmentsUpdateManager fragmentsUpdateManager;
    private NavController controlador;
    private ActionBar supportActionBar;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComissoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaCamposDaViewCompartilhadosPorMetodos();
        inicializaViewModel();
        pegaReferenciaDeFragmentAoAtachar();
        configuraDatePicker();
        configuraViewModelObserverHelper();
        configuraNavigationBottomApp();
        configuraMenuProvider();
    }

    private void inicializaCamposDaViewCompartilhadosPorMetodos() {
        navigationView = binding.bottomNavigation;
    }

    private void inicializaViewModel() {
        final CavaloRepository cavaloRepository = new CavaloRepository(this);
        final MotoristaRepository motoristaRepository = new MotoristaRepository(this);
        final FreteRepository freteRepository = new FreteRepository(this);
        final AdiantamentoRepository adiantamentoRepository = new AdiantamentoRepository(this);
        final CustoDePercursoRepository percursoRepository = new CustoDePercursoRepository(this);
        final CustoDeSalarioRepository salarioRepository = new CustoDeSalarioRepository(this);
        final ComissaoActViewModelFactory factory = new ComissaoActViewModelFactory(
                cavaloRepository,
                motoristaRepository,
                freteRepository,
                adiantamentoRepository,
                percursoRepository,
                salarioRepository
        );
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(ComissaoActViewModel.class);
    }

    private void pegaReferenciaDeFragmentAoAtachar() {
        final ComissoesGetFragmentsOnAtach getFragments =
                new ComissoesGetFragmentsOnAtach(getSupportFragmentManager());

        getFragments.run(new ComissoesGetFragmentsOnAtach.ComissoesGetFragmentsCallback() {
            @Override
            public void fragment_carregaDados(CarregandoDadosComissaoFragment fragment) {
                fragCarregando = fragment;
                fragmentVisivel = fragment;
            }

            @Override
            public void fragment_comissaoEmAberto(ComissoesEmAbertoFragment fragment) {
                fragComissaoEmAberto = fragment;
                fragmentVisivel = fragment;
            }

            @Override
            public void fragment_comissaoPaga(ComissoesPagasFragment fragment) {
                fragComissaoPaga = fragment;
                fragmentVisivel = fragment;
            }

            @Override
            public void fragment_detalhesParaPagamento(ComissoesDetalhesFragment fragment) {
                fragDetalhesParaPagamento = fragment;
                fragmentVisivel = fragment;
            }

            @Override
            public void fragment_resumoDePagamentos(ComissoesPagasDetalhesFragment fragment) {
                fragResumoDosPagamentos = fragment;
                fragmentVisivel = fragment;
            }
        });
    }

    private void configuraViewModelObserverHelper() {
        fragmentsUpdateManager = new ActComissao_fragmentsUpdateManager();
        final ComissaoViewModelObserverHelper viewModelHelper = new ComissaoViewModelObserverHelper(viewModel, this);
        viewModelHelper.configuraObservers();
        viewModelHelper.setCallback(new ComissaoViewModelObserverHelper.ObserverHelperCallback() {
            @Override
            public void dadosPreparadosParaInicializacao() {
                fragCarregando.finaliza();
                menuProvider.setDataSetCavalo(viewModel.getDataSet_cavalo());
                viewModel.solicitaAlteracaoDeDataSetPorNovoFiltroDeData();
                menuProvider.setDataSetPagamentos(viewModel.getListaSalarioComFiltro());
            }

            @Override
            public void notificaAttEmFrete() {
                fragmentsUpdateManager.notificaAlteracaoDeFrete(fragmentVisivel);
            }

            @Override
            public void notificaAttEmAdiantamento() {
                fragmentsUpdateManager.notificaAlteracaoDeAdiantamento(fragmentVisivel);
            }

            @Override
            public void notificaAttEmCustoDePercurso() {
                fragmentsUpdateManager.notificaAlteracaoDeCustoDePercurso(fragmentVisivel);
            }
        });
    }

    private void configuraDatePicker() {
        dataLayout = binding.dataLayout.layoutData;
        final TextView campoDataInicial = binding.dataLayout.dataInicial;
        final TextView campoDataFinal = binding.dataLayout.dataFinal;
        final DateRangePickerUtil datePicker = new DateRangePickerUtil(getSupportFragmentManager());
        datePicker.configuraCampos(campoDataInicial, campoDataFinal);
        datePicker.build(dataLayout);
        datePicker.setCallbackDatePicker(
                (dataInicial, dataFinal) -> {
                    viewModel.setDataInicial(dataInicial);
                    viewModel.setDataFinal(dataFinal);
                    campoDataInicial.setText(ConverteDataUtil.dataParaString(dataInicial));
                    campoDataFinal.setText(ConverteDataUtil.dataParaString(dataFinal));
                    viewModel.solicitaAlteracaoDeDataSetPorNovoFiltroDeData();
                    fragmentsUpdateManager.notificaNovoFiltroPorData(fragmentVisivel);
                    menuProvider.setDataSetPagamentos(viewModel.getListaSalarioComFiltro());
                });
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraNavigationBottomApp() {
        supportActionBar = getSupportActionBar();

        controlador =
                Navigation.findNavController(this, R.id.nav_host_fragment_container_comissao);
        NavigationUI.setupWithNavController(navigationView, controlador);

        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.navComissaoCarregaDados:
                    if (supportActionBar != null) {
                        supportActionBar.hide();
                    }

                    navigationView.setVisibility(GONE);
                    dataLayout.setVisibility(GONE);
                    break;

                case R.id.navComissoes:
                    if (supportActionBar != null) {
                        supportActionBar.setTitle("Em Aberto");
                        supportActionBar.show();
                        supportActionBar.setDisplayHomeAsUpEnabled(true);
                        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                    }

                    exibeBottomNavigation(true);
                    exibeDataLayout(true);
                    menuProvider.busca.setVisible(true);
                    break;

                case R.id.navComissoesPagas:
                    if (supportActionBar != null) {
                        supportActionBar.setTitle("Pagamentos");
                    }

                    exibeBottomNavigation(true);
                    exibeDataLayout(true);
                    menuProvider.busca.setVisible(true);
                    break;

                case R.id.navComissoesDetalhes:
                    if (supportActionBar != null) {
                        supportActionBar.setTitle("Detalhes");
                    }

                    exibeBottomNavigation(false);
                    exibeDataLayout(false);
                    menuProvider.busca.setVisible(false);
                    break;

                case R.id.navComissoesPagasDetalhes:
                    if (supportActionBar != null) {
                        supportActionBar.setTitle("Detalhes");
                    }

                    navigationView.setVisibility(GONE);
                    exibeBottomNavigation(false);
                    exibeDataLayout(false);
                    menuProvider.busca.setVisible(false);
                    break;
            }
        });
    }

    private void exibeDataLayout(final boolean isVisible) {
        if (dataLayout.getVisibility() == GONE && isVisible) {
            dataLayout.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(this, R.anim.data_layout_slide_in_top, dataLayout);
        } else if (dataLayout.getVisibility() == VISIBLE && !isVisible) {
            dataLayout.setVisibility(GONE);
        }
    }

    private void exibeBottomNavigation(final boolean isVisible) {
        if (navigationView.getVisibility() == GONE && isVisible) {
            navigationView.setVisibility(VISIBLE);
            AnimationUtil.defineAnimacao(this, R.anim.navigation_bar_slide_in_bottom, navigationView);
        } else if (navigationView.getVisibility() == VISIBLE && !isVisible) {
            navigationView.setVisibility(GONE);
        }
    }

    private void configuraMenuProvider() {
        final Context context = this;
        menuProvider = new ComissaoMenuProvider(new ArrayList<>(), new ArrayList<>());
        addMenuProvider(menuProvider, this, Lifecycle.State.RESUMED);
        menuProvider.setCallback(new ComissaoMenuProvider.ActComissaoMenuProviderCallback() {
            @Override
            public void onSearchIsSelected() {
                if (supportActionBar != null) {
                    supportActionBar.setDisplayShowTitleEnabled(false);
                }

                navigationView.setVisibility(GONE);
                menuProvider.setFragmentVisivel(fragmentVisivel);
            }

            @Override
            public void onSearchIsClear() {
                if (supportActionBar != null) {
                    supportActionBar.setDisplayShowTitleEnabled(true);
                }

                navigationView.setVisibility(VISIBLE);
                AnimationUtil.defineAnimacao(context, R.anim.navigation_bar_slide_in_bottom, navigationView);
            }

            @Override
            public void realizaBuscaCavalo(List<Cavalo> listaSearch) {
                fragmentsUpdateManager.notificaBuscaFragEmAberto(listaSearch, fragmentVisivel);
            }

            @Override
            public void realizaBuscaPagamento(List<CustosDeSalario> listaSearch) {
                fragmentsUpdateManager.notificaBuscaFragPagos(listaSearch, fragmentVisivel);
            }

            @Override
            public void onHomeClick() {
                if (fragDetalhesParaPagamento != null && fragDetalhesParaPagamento.isVisible()) {
                    controlador.popBackStack();
                } else if (fragResumoDosPagamentos != null && fragResumoDosPagamentos.isVisible()) {
                    controlador.popBackStack();
                } else {
                    finish();
                }
            }

            @Override
            public void onLogoutClick() {
                setResult(RESULT_LOGOUT);
                finish();
            }
        });
    }
}

class ActComissao_fragmentsUpdateManager {

    public void notificaAlteracaoDeFrete(final Fragment fragmentVisivel) {
        if (fragmentVisivel instanceof ComissoesEmAbertoFragment)
            ((ComissoesEmAbertoFragment) fragmentVisivel).actNotificaAtualizacao_frete();

        else if (fragmentVisivel instanceof ComissoesPagasFragment)
            ((ComissoesPagasFragment) fragmentVisivel).actNotificaAtualizacao_frete();

        else if (fragmentVisivel instanceof ComissoesDetalhesFragment)
            ((ComissoesDetalhesFragment) fragmentVisivel).actNotificaAtualizacao_frete();

        else if (fragmentVisivel instanceof ComissoesPagasDetalhesFragment)
            ((ComissoesPagasDetalhesFragment) fragmentVisivel).actNotificaAtualizacao_frete();
    }

    public void notificaAlteracaoDeAdiantamento(final Fragment fragmentVisivel) {
        if (fragmentVisivel instanceof ComissoesEmAbertoFragment)
            ((ComissoesEmAbertoFragment) fragmentVisivel).actNotificaAtualizacao_adiantamento();

        else if (fragmentVisivel instanceof ComissoesPagasFragment)
            ((ComissoesPagasFragment) fragmentVisivel).actSolicitaAtt_adiantamento();

        else if (fragmentVisivel instanceof ComissoesDetalhesFragment)
            ((ComissoesDetalhesFragment) fragmentVisivel).actSolicitaAtt_adiantamento();

        else if (fragmentVisivel instanceof ComissoesPagasDetalhesFragment)
            ((ComissoesPagasDetalhesFragment) fragmentVisivel).actSolicitaAtt_adiantamento();
    }

    public void notificaAlteracaoDeCustoDePercurso(final Fragment fragmentVisivel) {
        if (fragmentVisivel instanceof ComissoesEmAbertoFragment)
            ((ComissoesEmAbertoFragment) fragmentVisivel).actNotificaAtualizacao_salario();

        else if (fragmentVisivel instanceof ComissoesPagasFragment)
            ((ComissoesPagasFragment) fragmentVisivel).actSolicitaAtt_custoPercurso();

        else if (fragmentVisivel instanceof ComissoesDetalhesFragment)
            ((ComissoesDetalhesFragment) fragmentVisivel).actSolicitaAtt_custoPercurso();

        else if (fragmentVisivel instanceof ComissoesPagasDetalhesFragment)
            ((ComissoesPagasDetalhesFragment) fragmentVisivel).actSolicitaAtt_custoPercurso();
    }

    public void notificaBuscaFragEmAberto(List<Cavalo> listaSearch, Fragment fragmentVisivel) {
        if (fragmentVisivel instanceof ComissoesEmAbertoFragment)
            ((ComissoesEmAbertoFragment) fragmentVisivel).actNotificaBuscaNaSearch(listaSearch);
    }

    public void notificaNovoFiltroPorData(final Fragment fragmentVisivel) {
        if (fragmentVisivel instanceof ComissoesEmAbertoFragment)
            ((ComissoesEmAbertoFragment) fragmentVisivel).actNotificaNovaBuscaPorData();

        else if (fragmentVisivel instanceof ComissoesPagasFragment)
            ((ComissoesPagasFragment) fragmentVisivel).actNotificaNovaBuscaPorData();
    }

    public void notificaBuscaFragPagos(final List<CustosDeSalario> listaSearch, Fragment fragmentVisivel) {
        if (fragmentVisivel instanceof ComissoesPagasFragment)
            ((ComissoesPagasFragment) fragmentVisivel).actNotificaBuscaNaSearch(listaSearch);
    }
}

class ComissoesGetFragmentsOnAtach {
    private final FragmentManager fragmentManager;

    public ComissoesGetFragmentsOnAtach(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public interface ComissoesGetFragmentsCallback {
        void fragment_carregaDados(CarregandoDadosComissaoFragment fragment);

        void fragment_comissaoEmAberto(ComissoesEmAbertoFragment fragment);

        void fragment_comissaoPaga(ComissoesPagasFragment fragment);

        void fragment_detalhesParaPagamento(ComissoesDetalhesFragment fragment);

        void fragment_resumoDePagamentos(ComissoesPagasDetalhesFragment fragment);
    }

    //----------------------------------------------------------------------------------------------

    public void run(final ComissoesGetFragmentsCallback callback) {
        NavHostFragment navHost = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_container_comissao);
        if (navHost != null) {
            getFragmentDeAbertura(navHost, callback);
            getDemaisFragments(navHost, callback);
        }
    }

    private void getFragmentDeAbertura(
            @NonNull final NavHostFragment navHost,
            @NonNull final ComissoesGetFragmentsCallback callback
    ) {
        final CarregandoDadosComissaoFragment fragment =
                (CarregandoDadosComissaoFragment) navHost.getChildFragmentManager().getFragments().get(0);
        callback.fragment_carregaDados(fragment);
    }

    private void getDemaisFragments(
            @NonNull final NavHostFragment navHost,
            final ComissoesGetFragmentsCallback callback
    ) {
        navHost.getChildFragmentManager().addFragmentOnAttachListener(
                (fragmentManager, fragment) -> {
                    getFragment_comissaoEmAberto(callback, fragment);
                    getFragment_comissaoPaga(callback, fragment);
                    getFragment_detalheParaPagamento(callback, fragment);
                    getFragment_resumoDePagamentos(callback, fragment);
                });
    }

    private static void getFragment_resumoDePagamentos(ComissoesGetFragmentsCallback callback, Fragment fragment) {
        if (fragment instanceof ComissoesPagasDetalhesFragment) {
            final ComissoesPagasDetalhesFragment fragResumoDePagamentos = (ComissoesPagasDetalhesFragment) fragment;
            callback.fragment_resumoDePagamentos(fragResumoDePagamentos);
        }
    }

    private static void getFragment_detalheParaPagamento(ComissoesGetFragmentsCallback callback, Fragment fragment) {
        if (fragment instanceof ComissoesDetalhesFragment) {
            final ComissoesDetalhesFragment fragDetalhesParaPagamento = (ComissoesDetalhesFragment) fragment;
            callback.fragment_detalhesParaPagamento(fragDetalhesParaPagamento);
        }
    }

    private static void getFragment_comissaoPaga(ComissoesGetFragmentsCallback callback, Fragment fragment) {
        if (fragment instanceof ComissoesPagasFragment) {
            final ComissoesPagasFragment fragComissaoPaga = (ComissoesPagasFragment) fragment;
            callback.fragment_comissaoPaga(fragComissaoPaga);
        }
    }

    private static void getFragment_comissaoEmAberto(ComissoesGetFragmentsCallback callback, Fragment fragment) {
        if (fragment instanceof ComissoesEmAbertoFragment) {
            final ComissoesEmAbertoFragment fragComissaoEmAberto = (ComissoesEmAbertoFragment) fragment;
            callback.fragment_comissaoEmAberto(fragComissaoEmAberto);
        }
    }

}

class ComissaoMenuProvider implements MenuProvider {
    private final List<Cavalo> dataSetCavalo;
    private final List<CustosDeSalario> dataSetSalario;
    private ActComissaoMenuProviderCallback callback;
    private Fragment fragmentVisivel;
    public MenuItem busca;

    public void setFragmentVisivel(Fragment fragmentVisivel) {
        this.fragmentVisivel = fragmentVisivel;
    }

    ComissaoMenuProvider(List<Cavalo> dataSet, List<CustosDeSalario> dataSetSalario) {
        this.dataSetCavalo = dataSet;
        this.dataSetSalario = dataSetSalario;
    }

    public void setDataSetCavalo(final List<Cavalo> dataSetCavalo) {
        this.dataSetCavalo.clear();
        this.dataSetCavalo.addAll(dataSetCavalo);
    }

    public void setCallback(ActComissaoMenuProviderCallback callback) {
        this.callback = callback;
    }

    public void setDataSetPagamentos(final List<CustosDeSalario> listaSalarioComFiltro) {
        this.dataSetSalario.clear();
        this.dataSetSalario.addAll(listaSalarioComFiltro);
    }

    public interface ActComissaoMenuProviderCallback {
        void onSearchIsSelected();

        void onSearchIsClear();

        void realizaBuscaCavalo(List<Cavalo> listaSearch);

        void realizaBuscaPagamento(List<CustosDeSalario> listaSearch);

        void onHomeClick();

        void onLogoutClick();
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.removeItem(R.id.menu_padrao_editar);
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        busca = menu.findItem(R.id.menu_padrao_search);
        SearchView searchView = (SearchView) busca.getActionView();

        Objects.requireNonNull(searchView).setOnSearchClickListener(
                v -> {
                    logout.setVisible(false);
                    callback.onSearchIsSelected();
                });

        searchView.setOnCloseListener(
                () -> {
                    logout.setVisible(true);
                    callback.onSearchIsClear();
                    return false;
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (fragmentVisivel instanceof ComissoesEmAbertoFragment) {
                    List<Cavalo> listaSearch = new ArrayList<>();
                    for (Cavalo c : dataSetCavalo) {
                        if (c.getPlaca().toLowerCase().contains(newText.toLowerCase())) {
                            listaSearch.add(c);
                        }
                    }
                    callback.realizaBuscaCavalo(listaSearch);
                } else if (fragmentVisivel instanceof ComissoesPagasFragment) {
                    List<CustosDeSalario> dataSet_search = new ArrayList<>();
                    for (CustosDeSalario s : dataSetSalario) {
                        final Frota cavalo =
                                FiltraCavalo.localizaPeloId(dataSetCavalo, s.getRefCavaloId());
                        String placa = "";
                        if (cavalo != null) {
                            placa = cavalo.getPlaca();
                        }

                        if (placa.toLowerCase().contains(newText.toLowerCase())) {
                            dataSet_search.add(s);
                        }
                    }
                    callback.realizaBuscaPagamento(dataSet_search);
                }

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                callback.onLogoutClick();
                break;

            case android.R.id.home:
                callback.onHomeClick();
                break;
        }
        return false;
    }
}


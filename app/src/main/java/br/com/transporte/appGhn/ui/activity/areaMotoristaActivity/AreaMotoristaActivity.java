package br.com.transporte.appGhn.ui.activity.areaMotoristaActivity;

import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.INDICE_EXIBIDO_INICIALMENTE;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.INDICE_FRAG_ABASTECIMENTO;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.INDICE_FRAG_DESPESA;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.INDICE_FRAG_FRETE;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.INDICE_FRAG_RESUMO;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.REQUEST_ATUALIZACAO_DE_DATA;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.RESULT_LOGOUT;
import static br.com.transporte.appGhn.ui.activity.areaMotoristaActivity.ConstantesAreaMotorista.SEM_MOTORISTA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_ABASTECIMENTO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_DEFAUT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityAreaMotoristaBinding;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.repository.AdiantamentoRepository;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.appGhn.repository.CustoDePercursoRepository;
import br.com.transporte.appGhn.repository.FreteRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.viewpager.ViewPagerAdapter;
import br.com.transporte.appGhn.ui.fragment.areaMotorista.AreaMotoristaAbastecimentoFragment;
import br.com.transporte.appGhn.ui.fragment.areaMotorista.AreaMotoristaCustosDePercursoFragment;
import br.com.transporte.appGhn.ui.fragment.areaMotorista.AreaMotoristaFreteFragment;
import br.com.transporte.appGhn.ui.fragment.areaMotorista.resumo.AreaMotoristaResumoFragment;
import br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel.AreaMotoristaActViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.AreaMotoristaActViewModelFactory;
import br.com.transporte.appGhn.util.AnimationUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class AreaMotoristaActivity extends AppCompatActivity {
    private ActivityAreaMotoristaBinding binding;
    private FloatingActionButton fabPrincipal, fab1, fab2, fab3;
    private Animation animFabAbertura, animFabFechamento, animFabCima, animFabBaixo, animFabNoroesteAbre,
            animFabNoroesteFecha, animFabNordesteAbre, animFabNordesteFecha;
    private boolean fabVisivel = false;
    private ViewPager2 viewPager2;
    private ImageView btnResumo, btnFrete, btnAbastecimento, btnDespesa;
    private TextView txtResumo, txtFrete, txtAbastecimento, txtDespesa, campoNome;
    private BottomAppBarGerenciadorDeBtns bottomAppBtnsManager;
    private AreaMotoristaResumoFragment resumoFragment;
    private AreaMotoristaFreteFragment freteFragment;
    private AreaMotoristaAbastecimentoFragment abastecimentoFragment;
    private AreaMotoristaCustosDePercursoFragment custoFragment;
    private AreaMotoristaActViewModel viewModel;
    private final ActivityResultLauncher<Intent> activityResultLauncherFrete = getActivityResultLauncherFrete();
    private final ActivityResultLauncher<Intent> activityResultLauncherAbastecimento = getActivityResultLauncherAbastecimento();
    private final ActivityResultLauncher<Intent> activityResultLauncherCusto = getActivityResultLauncherCusto();
    private ActionBar supportActionBar;

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncherCusto() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_CANCELED) {

                        MensagemUtil
                                .toast(this, NENHUMA_ALTERACAO_REALIZADA);
                    }
                });
    }

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncherAbastecimento() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_CANCELED) {

                        MensagemUtil
                                .toast(this, NENHUMA_ALTERACAO_REALIZADA);

                    }
                });
    }

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncherFrete() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_CANCELED) {
                        MensagemUtil.toast(this, NENHUMA_ALTERACAO_REALIZADA);
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAreaMotoristaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configuraActionBar();

        final long cavaloId = recebeIdCavaloSelecionado();

        inicializaViewModel();
        buscaDadosNoViewModel(cavaloId);
        listenersParaCapturarFragmentsAoAtachar();

        inicializaCamposDaView();
        inicializaAnimacoes();

        configuraMenuProviderHelper();
        configuraBottomAppBar();

        configuraViewPager(cavaloId);
        configuraNavegacao(cavaloId);

        listenerParaAlteracaoDeDataNosFragments();

    }

    private void configuraActionBar() {
        supportActionBar = getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
    }

    private long recebeIdCavaloSelecionado() {
        Intent dados = getIntent();
        long cavaloId = 0;
        if (getIntent().hasExtra(CHAVE_ID_CAVALO))
            cavaloId = dados.getLongExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);

        return cavaloId;
    }

    private void inicializaViewModel() {
        final CustoDeAbastecimentoRepository abastecimentoRepository = new CustoDeAbastecimentoRepository(this);
        final AdiantamentoRepository adiantamentoRepository = new AdiantamentoRepository(this);
        final CustoDePercursoRepository custoRepository = new CustoDePercursoRepository(this);
        final MotoristaRepository motoristaRepository = new MotoristaRepository(this);
        final CavaloRepository cavaloRepository = new CavaloRepository(this);
        final FreteRepository freteRepository = new FreteRepository(this);

        final AreaMotoristaActViewModelFactory factory =
                new AreaMotoristaActViewModelFactory(
                        freteRepository,
                        abastecimentoRepository,
                        custoRepository,
                        cavaloRepository,
                        motoristaRepository,
                        adiantamentoRepository
                );
        final ViewModelProvider provedor = new ViewModelProvider(this, factory);
        viewModel = provedor.get(AreaMotoristaActViewModel.class);
    }

    private void listenersParaCapturarFragmentsAoAtachar() {
        getSupportFragmentManager().addFragmentOnAttachListener(
                (fragmentManager, fragment) -> {
                    if (fragment instanceof AreaMotoristaResumoFragment)
                        resumoFragment = (AreaMotoristaResumoFragment) fragment;
                    else if (fragment instanceof AreaMotoristaFreteFragment)
                        freteFragment = (AreaMotoristaFreteFragment) fragment;
                    else if (fragment instanceof AreaMotoristaAbastecimentoFragment)
                        abastecimentoFragment = (AreaMotoristaAbastecimentoFragment) fragment;
                    else if (fragment instanceof AreaMotoristaCustosDePercursoFragment)
                        custoFragment = (AreaMotoristaCustosDePercursoFragment) fragment;
                });
    }

    private void buscaDadosNoViewModel(final long cavaloId) {
        final RequestParaPrimeiraDataAtt request = new RequestParaPrimeiraDataAtt();
        viewModel.localizaCavalo(cavaloId).observe(this,
                cavaloRecebido -> {
                    if (cavaloRecebido != null) {
                        viewModel.setCavaloAcessado(cavaloRecebido);
                        supportActionBar.setTitle(cavaloRecebido.getPlaca());
                        buscaMotorista(cavaloRecebido);
                        observerListaFretes(request, cavaloId);
                        observerListaAbastecimentos(request, cavaloId);
                        observerListaCustosPercurso(request, cavaloId);
                        observerListaAdiantamentos(request, cavaloId);
                    }
                }
        );
    }

    private void buscaMotorista(@NonNull Cavalo cavaloRecebido) {
        viewModel.localizaMotorista(cavaloRecebido.getRefMotoristaId()).observe(this,
                motoristaRecebido -> {
                    String nomeMotorista;
                    if (motoristaRecebido != null) {
                        nomeMotorista = motoristaRecebido.toString();
                    } else {
                        nomeMotorista = SEM_MOTORISTA;
                    }
                    viewModel.setNomeMotoristaAcessado(nomeMotorista);
                    campoNome.setText(nomeMotorista);
                });
    }

    private void observerListaFretes(RequestParaPrimeiraDataAtt request, long cavaloId) {
        viewModel.buscaFretesPorCavaloId(cavaloId).observe(this,
                resource -> {
                    final List<Frete> listaFretes = resource.getDado();
                    if (listaFretes != null) {
                        viewModel.configuraFreteHelper(listaFretes);
                        if (request.isAguardandoPrimeiraAtt()) {
                            request.setFreteDataInicialCarregada(true);
                            tentaFazerPrimeiraAtualizacaoSeTodosDadosEstiveremCarregados(request);
                        } else {
                            atualizaFragmentVisivel();
                        }
                    }
                });
    }

    private void observerListaAbastecimentos(RequestParaPrimeiraDataAtt request, long cavaloId) {
        viewModel.buscaAbastecimentosPorCavaloId(cavaloId).observe(this,
                resource -> {
                    final List<CustosDeAbastecimento> listaAbastecimentos = resource.getDado();
                    if (listaAbastecimentos != null) {
                        viewModel.configuraAbastecimentoHelper(listaAbastecimentos);
                        if (request.isAguardandoPrimeiraAtt()) {
                            request.setAbastecimentoDataInicialCarregada(true);
                            tentaFazerPrimeiraAtualizacaoSeTodosDadosEstiveremCarregados(request);
                        } else {
                            atualizaFragmentVisivel();
                        }
                    }
                });
    }

    private void observerListaCustosPercurso(RequestParaPrimeiraDataAtt request, long cavaloId) {
        viewModel.buscaCustoPercursoPorCavaloId(cavaloId).observe(this,
                resource -> {
                    final List<CustosDePercurso> listaCustoPercurso = resource.getDado();
                    if (listaCustoPercurso != null) {
                        viewModel.configuraCustoHelper(listaCustoPercurso);
                        if (request.isAguardandoPrimeiraAtt()) {
                            request.setCustoDataInicialCarregada(true);
                            tentaFazerPrimeiraAtualizacaoSeTodosDadosEstiveremCarregados(request);
                        } else {
                            atualizaFragmentVisivel();
                        }
                    }
                });
    }

    private void observerListaAdiantamentos(RequestParaPrimeiraDataAtt request, long cavaloId) {
        viewModel.buscaAdiantamentosPorCavaloId(cavaloId).observe(this,
                resource -> {
                    final List<Adiantamento> listaAdiantamentos = resource.getDado();
                    if (listaAdiantamentos != null) {
                        viewModel.configuraAdiantamentoHelper(listaAdiantamentos);
                        request.setAdiantamentoDataInicialCarregada(true);
                        tentaFazerPrimeiraAtualizacaoSeTodosDadosEstiveremCarregados(request);
                    }
                });
    }

    private void tentaFazerPrimeiraAtualizacaoSeTodosDadosEstiveremCarregados(@NonNull RequestParaPrimeiraDataAtt request) {
        request.requestParaPrimeiraAttDeFragment(
                this::atualizaFragmentVisivel
        );
    }

    private void atualizaFragmentVisivel() {
        viewModel.solicitaAlteracaoDeSharedData();
        if (resumoFragment != null) {
            if (resumoFragment.isVisible()) resumoFragment.atualizaValoresParaExibirNaUi();
            else resumoFragment.setAtualizacaoSolicitadaPelaAct(true);
        }

        if (freteFragment != null) {
            if (freteFragment.isVisible()) freteFragment.atualizaValoresParaExibirNaUi();
            else freteFragment.setAtualizacaoSolicitadaPelaAct(true);
        }

        if (abastecimentoFragment != null) {
            if (abastecimentoFragment.isVisible())
                abastecimentoFragment.atualizaValoresParaExibirNaUi();
            else abastecimentoFragment.setAtualizacaoSolicitadaPelaAct(true);
        }

        if (custoFragment != null) {
            if (custoFragment.isVisible()) custoFragment.atualizaValoresParaExibirNaUi();
            else custoFragment.setAtualizacaoSolicitadaPelaAct(true);
        }
    }

    private void inicializaCamposDaView() {
        btnResumo = binding.actAreaMotoristaImgResumo;
        txtResumo = binding.actAreaMotoristaTxtResumo;
        btnFrete = binding.actAreaMotoristaImgFrete;
        txtFrete = binding.actAreaMotoristaTxtFrete;
        btnAbastecimento = binding.actAreaMotoristaImgAbastecimento;
        txtAbastecimento = binding.actAreaMotoristaTxtAbastecimento;
        btnDespesa = binding.actAreaMotoristaImgDespesa;
        txtDespesa = binding.actAreaMotoristaTxtDespesa;
        campoNome = binding.motorista;
        fabPrincipal = binding.areaDoMotoristaFaBPrincipal;
        fab1 = binding.areaDoMotoristaFaB1;
        fab2 = binding.areaDoMotoristaFaB2;
        fab3 = binding.areaDoMotoristaFaB3;
    }

    private void inicializaAnimacoes() {
        animFabAbertura = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_rotacao_abertura);
        animFabFechamento = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_rotacao_fechamento);
        animFabCima = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_cima);
        animFabBaixo = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_baixo);
        animFabNoroesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_abre);
        animFabNoroesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_fecha);
        animFabNordesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_abre);
        animFabNordesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_fecha);
    }

    private void configuraMenuProviderHelper() {
        final AreaMotoristaMenuProviderHelper menuProviderHelper = new AreaMotoristaMenuProviderHelper();
        addMenuProvider(menuProviderHelper, this, Lifecycle.State.RESUMED);
        menuProviderHelper.setMenuProviderCallback(new AreaMotoristaMenuProviderHelper.MenuProviderCallback() {
            @Override
            public void onLogoutCLick() {
                setResult(RESULT_LOGOUT);
                finish();
            }

            @Override
            public void onHomeClick() {
                finish();
            }
        });
    }

    private void configuraBottomAppBar() {
        final BottomAppBarBtn BTN_RESUMO = new BottomAppBarBtn(btnResumo, txtResumo, INDICE_FRAG_RESUMO);
        final BottomAppBarBtn BTN_FRETE = new BottomAppBarBtn(btnFrete, txtFrete, INDICE_FRAG_FRETE);
        final BottomAppBarBtn BTN_ABASTECIMENTO = new BottomAppBarBtn(btnAbastecimento, txtAbastecimento, INDICE_FRAG_ABASTECIMENTO);
        final BottomAppBarBtn BTN_DESPESA = new BottomAppBarBtn(btnDespesa, txtDespesa, INDICE_FRAG_DESPESA);
        bottomAppBtnsManager = new BottomAppBarGerenciadorDeBtns(INDICE_EXIBIDO_INICIALMENTE);
        bottomAppBtnsManager.setListaDeBtns(BTN_RESUMO, BTN_FRETE, BTN_DESPESA, BTN_ABASTECIMENTO);
    }

    private void configuraViewPager(long cavaloId) {
        final ViewPagerAdapter viewPagerAdapter =
                new ViewPagerAdapter(this, cavaloId);

        viewPager2 = binding.viewPager;
        viewPager2.setAdapter(viewPagerAdapter);
    }

    private void configuraNavegacao(long cavaloId) {
        navegaComFabs(cavaloId);
        configuraNavegacaoComTouchNoViewPager();
        configuraNavegacaoComClickNaBottomAppBar();
    }

    private void configuraNavegacaoComTouchNoViewPager() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomAppBtnsManager.mudaCorDaBottomAppBarEmTransicoes(position);
            }
        });
    }

    private void configuraNavegacaoComClickNaBottomAppBar() {
        binding.actAreaMotoristaIcResumo.setOnClickListener(v -> navegaComClick(INDICE_FRAG_RESUMO));
        binding.actAreaMotoristaIcFrete.setOnClickListener(v -> navegaComClick(INDICE_FRAG_FRETE));
        binding.actAreaMotoristaIcAbastecimento.setOnClickListener(v -> navegaComClick(INDICE_FRAG_ABASTECIMENTO));
        binding.actAreaMotoristaIcDespesa.setOnClickListener(v -> navegaComClick(INDICE_FRAG_DESPESA));
    }

    private void navegaComClick(int indiceNovo) {
        int indiceAnterior = viewPager2.getCurrentItem();
        if (indiceAnterior != indiceNovo) {
            viewPager2.setCurrentItem(indiceNovo);
            animaNomeEmTransicoes();
            bottomAppBtnsManager.mudaCorDaBottomAppBarEmTransicoes(indiceNovo);
        } else {
            Toast.makeText(this, R.string.voce_ja_esta_aqui, Toast.LENGTH_SHORT).show();
        }
    }

    private void animaNomeEmTransicoes() {
        AnimationUtil.defineAnimacao(this, R.anim.anim_piscar, campoNome);
    }

    private void navegaComFabs(long cavaloId) {
        fabPrincipal.setOnClickListener(v -> animacaoFabs());
        fab1.setOnClickListener(v -> {
            animacaoFabs();
            final Intent intent =
                    new Intent(this, FormulariosActivity.class);

            intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
            intent.putExtra(CHAVE_ID_CAVALO, cavaloId);
            activityResultLauncherFrete.launch(intent);
        });
        fab2.setOnClickListener(v -> {
            animacaoFabs();
            final Intent intent =
                    new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_ABASTECIMENTO);
            intent.putExtra(CHAVE_ID_CAVALO, cavaloId);
            activityResultLauncherAbastecimento.launch(intent);
        });
        fab3.setOnClickListener(v -> {
            animacaoFabs();
            final Intent intent =
                    new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            intent.putExtra(CHAVE_ID_CAVALO, cavaloId);
            activityResultLauncherCusto.launch(intent);
        });
    }

    private void animacaoFabs() {
        if (!fabVisivel) {
            fab1.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.VISIBLE);
            fab3.setVisibility(View.VISIBLE);
        } else {
            fab1.setVisibility(View.INVISIBLE);
            fab2.setVisibility(View.INVISIBLE);
            fab3.setVisibility(View.INVISIBLE);
        }

        if (!fabVisivel) {
            fabPrincipal.startAnimation(animFabAbertura);
            fab1.startAnimation(animFabNoroesteAbre);
            fab2.startAnimation(animFabCima);
            fab3.startAnimation(animFabNordesteAbre);
        } else {
            fabPrincipal.startAnimation(animFabFechamento);
            fab1.startAnimation(animFabNoroesteFecha);
            fab2.startAnimation(animFabBaixo);
            fab3.startAnimation(animFabNordesteFecha);
        }

        fabVisivel = !fabVisivel;
    }

    private void listenerParaAlteracaoDeDataNosFragments() {
        getSupportFragmentManager().setFragmentResultListener(REQUEST_ATUALIZACAO_DE_DATA, this,
                (requestKey, result) -> atualizaFragmentVisivel()
        );
    }

}


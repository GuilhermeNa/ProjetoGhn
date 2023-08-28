package br.com.transporte.AppGhn.ui.activity;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ABASTECIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DEFAUT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;
import static br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaResumoFragment.KEY_ACTION_ADAPTER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.databinding.ActivityAreaMotoristaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.activity.extensions.StatusBarUtil;
import br.com.transporte.AppGhn.ui.adapter.viewpager.ViewPagerAdapter;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaAbastecimentoFragment;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaCustosDePercursoFragment;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaFreteFragment;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaResumoFragment;
import br.com.transporte.AppGhn.util.AnimationUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class AreaMotoristaActivity extends AppCompatActivity implements MenuProvider {
    public static final String SEM_MOTORISTA = "Sem motorista";
    public static final int VIEW_PAGER_POS_RESUMO_FRAGMENT = 0;
    public static final int VIEW_PAGER_POS_FRETE_FRAGMENT = 1;
    public static final int VIEW_PAGER_POS_ABASTECIMENTO_FRAGMENT = 2;
    public static final int VIEW_PAGER_POS_CUSTO_FRAGMENT = 3;
    private ActivityAreaMotoristaBinding binding;
    private FloatingActionButton fabPrincipal, fab1, fab2, fab3;
    private Animation animFabAbertura, animFabFechamento, animFabCima, animFabBaixo, animFabNoroesteAbre,
            animFabNoroesteFecha, animFabNordesteAbre, animFabNordesteFecha, logout;
    private boolean fabVisivel = false;
    private ViewPager2 viewPager2;
    private TextView nome;
    private AreaMotoristaResumoFragment resumoFragment;
    private AreaMotoristaFreteFragment freteFragment;
    private AreaMotoristaAbastecimentoFragment abastecimentoFragment;
    private AreaMotoristaCustosDePercursoFragment custoFragment;
    private final ActivityResultLauncher<Intent> activityResultLauncherFrete = getActivityResultLauncherFrete();
    private final ActivityResultLauncher<Intent> activityResultLauncherAbastecimento = getActivityResultLauncherAbastecimento();
    private final ActivityResultLauncher<Intent> activityResultLauncherCusto = getActivityResultLauncherCusto();

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncherCusto() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_OK:
                            if (resumoFragment == null) {
                                resumoFragment = (AreaMotoristaResumoFragment) getSupportFragmentManager().findFragmentByTag("f" + VIEW_PAGER_POS_RESUMO_FRAGMENT);
                            }
                            if (custoFragment == null) {
                                Fragment verificaSeFragmentFoiInicializado =  getSupportFragmentManager().findFragmentByTag("f" + VIEW_PAGER_POS_CUSTO_FRAGMENT);
                                if(verificaSeFragmentFoiInicializado != null){
                                    custoFragment = (AreaMotoristaCustosDePercursoFragment) verificaSeFragmentFoiInicializado;
                                }
                            }
                            if(resumoFragment != null) resumoFragment.solicitaAtualizacao();
                            if(custoFragment != null) custoFragment.solicitaAtualizacao();
                            break;

                        case RESULT_CANCELED:
                            MensagemUtil.toast(this, NENHUMA_ALTERACAO_REALIZADA);
                            break;
                    }
                });
    }

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncherAbastecimento() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_OK:
                            if (resumoFragment == null) {
                                resumoFragment = (AreaMotoristaResumoFragment) getSupportFragmentManager().findFragmentByTag("f" + VIEW_PAGER_POS_RESUMO_FRAGMENT);
                            }
                            if (abastecimentoFragment == null) {
                                Fragment verificaSeFragmentFoiInicializado = getSupportFragmentManager().findFragmentByTag("f" + VIEW_PAGER_POS_ABASTECIMENTO_FRAGMENT);
                                if (verificaSeFragmentFoiInicializado != null) {
                                    abastecimentoFragment = (AreaMotoristaAbastecimentoFragment) verificaSeFragmentFoiInicializado;
                                }
                            }
                            if (resumoFragment != null) resumoFragment.solicitaAtualizacao();
                            if (abastecimentoFragment != null) abastecimentoFragment.solicitaAtualizacao();
                            break;

                        case RESULT_CANCELED:
                            MensagemUtil.toast(this, NENHUMA_ALTERACAO_REALIZADA);
                            break;
                    }
                });
    }

    @NonNull
    @Contract(pure = true)
    private ActivityResultLauncher<Intent> getActivityResultLauncherFrete() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_OK:
                            if (resumoFragment == null) {
                                resumoFragment = (AreaMotoristaResumoFragment) getSupportFragmentManager().findFragmentByTag("f" + VIEW_PAGER_POS_RESUMO_FRAGMENT);
                            }
                            if (freteFragment == null) {
                                Fragment verificaSeFragmentFoiInicializado = getSupportFragmentManager().findFragmentByTag("f" + VIEW_PAGER_POS_FRETE_FRAGMENT);
                                if (verificaSeFragmentFoiInicializado != null) {
                                    freteFragment = (AreaMotoristaFreteFragment) verificaSeFragmentFoiInicializado;
                                }
                            }
                            if (resumoFragment != null) resumoFragment.solicitaAtualizacao();
                            if (freteFragment != null) freteFragment.solicitaAtualizacao();
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
        binding = ActivityAreaMotoristaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaCamposDaView();
        Cavalo cavalo = recebeReferenciaDeCavalo();
        configuraUi(cavalo);
        configuraViewPager(cavalo);
        configuraNavegacao(cavalo);
        StatusBarUtil.setStatusBarColor(this, getWindow());
        configuraToolbar(cavalo);
        configuraObserverDosAdaptersDosDemaisFragments();
    }

    private void configuraObserverDosAdaptersDosDemaisFragments() {
        getSupportFragmentManager().setFragmentResultListener(KEY_ACTION_ADAPTER, this, (requestKey, result) -> {
            if(resumoFragment == null){
                resumoFragment = (AreaMotoristaResumoFragment) getSupportFragmentManager().findFragmentByTag("f" + VIEW_PAGER_POS_RESUMO_FRAGMENT);
            }
            if(resumoFragment != null) resumoFragment.solicitaAtualizacao();
        });

    }

    private void configuraToolbar(@NonNull Cavalo cavalo) {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(cavalo.getPlaca());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        addMenuProvider(this, this, Lifecycle.State.RESUMED);
    }

    private void configuraUi(Cavalo cavalo) {
        try {
            nome.setText(cavalo.getMotorista().getNome());
        } catch (NullPointerException e) {
            e.printStackTrace();
            nome.setText(SEM_MOTORISTA);
        }
    }

    private Cavalo recebeReferenciaDeCavalo() {
        Cavalo cavalo;
        Intent dados = getIntent();
        CavaloDAO cavaloDao = new CavaloDAO();
        int cavaloId = 0;

        if (dados.hasExtra(CHAVE_ID_CAVALO)) {
            cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
        }

        cavalo = cavaloDao.localizaPeloId(cavaloId);
        return cavalo;
    }

    private void configuraNavegacao(Cavalo cavalo) {
        configuraFabs(cavalo);
        configuraNavegacaoAppBar();
    }

    private void configuraNavegacaoAppBar() {
        binding.actAreaMotoristaImgResumo.setColorFilter(Color.parseColor("#E74C3C"));
        binding.actAreaMotoristaTxtResumo.setTextColor(Color.parseColor("#E74C3C"));

        binding.actAreaMotoristaIcResumo.setOnClickListener(v -> navegaComClickViewPager(0, binding.actAreaMotoristaImgResumo, binding.actAreaMotoristaTxtResumo));
        binding.actAreaMotoristaIcFrete.setOnClickListener(v -> navegaComClickViewPager(1, binding.actAreaMotoristaImgFrete, binding.actAreaMotoristaTxtFrete));
        binding.actAreaMotoristaIcAbastecimento.setOnClickListener(v -> navegaComClickViewPager(2, binding.actAreaMotoristaImgAbastecimento, binding.actAreaMotoristaTxtAbastecimento));
        binding.actAreaMotoristaIcDespesa.setOnClickListener(v -> navegaComClickViewPager(3, binding.actAreaMotoristaImgDespesa, binding.actAreaMotoristaTxtDespesa));
    }

    private void configuraViewPager(@NonNull Cavalo cavalo) {
        viewPager2 = binding.viewPager;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, cavalo.getId());
        viewPager2.setAdapter(viewPagerAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.actAreaMotoristaImgResumo.setColorFilter(Color.parseColor("#E74C3C"));
                        binding.actAreaMotoristaTxtResumo.setTextColor(Color.parseColor("#E74C3C"));

                        binding.actAreaMotoristaImgFrete.clearColorFilter();
                        binding.actAreaMotoristaTxtFrete.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgAbastecimento.clearColorFilter();
                        binding.actAreaMotoristaTxtAbastecimento.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgDespesa.clearColorFilter();
                        binding.actAreaMotoristaTxtDespesa.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 1:
                        binding.actAreaMotoristaImgFrete.setColorFilter(Color.parseColor("#E74C3C"));
                        binding.actAreaMotoristaTxtFrete.setTextColor(Color.parseColor("#E74C3C"));

                        binding.actAreaMotoristaImgResumo.clearColorFilter();
                        binding.actAreaMotoristaTxtResumo.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgAbastecimento.clearColorFilter();
                        binding.actAreaMotoristaTxtAbastecimento.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgDespesa.clearColorFilter();
                        binding.actAreaMotoristaTxtDespesa.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 2:
                        binding.actAreaMotoristaImgAbastecimento.setColorFilter(Color.parseColor("#E74C3C"));
                        binding.actAreaMotoristaTxtAbastecimento.setTextColor(Color.parseColor("#E74C3C"));

                        binding.actAreaMotoristaImgResumo.clearColorFilter();
                        binding.actAreaMotoristaTxtResumo.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgFrete.clearColorFilter();
                        binding.actAreaMotoristaTxtFrete.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgDespesa.clearColorFilter();
                        binding.actAreaMotoristaTxtDespesa.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 3:
                        binding.actAreaMotoristaImgDespesa.setColorFilter(Color.parseColor("#E74C3C"));
                        binding.actAreaMotoristaTxtDespesa.setTextColor(Color.parseColor("#E74C3C"));

                        binding.actAreaMotoristaImgResumo.clearColorFilter();
                        binding.actAreaMotoristaTxtResumo.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgFrete.clearColorFilter();
                        binding.actAreaMotoristaTxtFrete.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.actAreaMotoristaImgAbastecimento.clearColorFilter();
                        binding.actAreaMotoristaTxtAbastecimento.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                }
                animaNomeEmTransicoes();
            }
        });
    }

    private void navegaComClickViewPager(int idItem, ImageView icon, TextView txt) {
        int indiceFragments = viewPager2.getCurrentItem();
        if (indiceFragments != idItem) {
            viewPager2.setCurrentItem(idItem);
            animaNomeEmTransicoes();
            configuraCorDosItensNaBottomAppBar(icon, txt, indiceFragments);
        } else {
            Toast.makeText(this, R.string.voce_ja_esta_aqui, Toast.LENGTH_SHORT).show();
        }
    }

    private void animaNomeEmTransicoes() {
        AnimationUtil.defineAnimacao(this, R.anim.anim_logout, nome);
    }

    private void configuraCorDosItensNaBottomAppBar(ImageView icon, TextView txt, int indiceFragments) {
        switch (indiceFragments) {
            case 0:
                binding.actAreaMotoristaImgResumo.clearColorFilter();
                binding.actAreaMotoristaTxtResumo.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case 1:
                binding.actAreaMotoristaImgFrete.clearColorFilter();
                binding.actAreaMotoristaTxtFrete.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case 2:
                binding.actAreaMotoristaImgAbastecimento.clearColorFilter();
                binding.actAreaMotoristaTxtAbastecimento.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case 3:
                binding.actAreaMotoristaImgDespesa.clearColorFilter();
                binding.actAreaMotoristaTxtDespesa.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
        icon.setColorFilter(Color.parseColor("#E74C3C"));
        txt.setTextColor(Color.parseColor("#E74C3C"));
    }

    private void inicializaCamposDaView() {
        nome = binding.motorista;
        fabPrincipal = binding.areaDoMotoristaFaBPrincipal;
        fab1 = binding.areaDoMotoristaFaB1;
        fab2 = binding.areaDoMotoristaFaB2;
        fab3 = binding.areaDoMotoristaFaB3;
        animFabAbertura = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_rotacao_abertura);
        animFabFechamento = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_rotacao_fechamento);
        animFabCima = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_cima);
        animFabBaixo = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_baixo);
        animFabNoroesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_abre);
        animFabNoroesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_fecha);
        animFabNordesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_abre);
        animFabNordesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_fecha);
    }

    private void configuraFabs(Cavalo cavalo) {
        fabPrincipal.setOnClickListener(v -> animacaoFabs());
        fab1.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            activityResultLauncherFrete.launch(intent);
        });
        fab2.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_ABASTECIMENTO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            activityResultLauncherAbastecimento.launch(intent);
        });
        fab3.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
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

    //----------------------------------------------------------------------------------------------
    //                                      On Create Menu                                        ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
        menu.removeItem(R.id.menu_padrao_search);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                Toast.makeText(this, LOGOUT, Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }
}

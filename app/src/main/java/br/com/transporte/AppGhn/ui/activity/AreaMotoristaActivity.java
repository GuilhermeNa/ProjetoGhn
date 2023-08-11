package br.com.transporte.AppGhn.ui.activity;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ABASTECIMENTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DEFAUT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityAreaMotoristaBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.adapter.viewpager.ViewPagerAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;

public class AreaMotoristaActivity extends AppCompatActivity implements MenuProvider {
    private ActivityAreaMotoristaBinding binding;
    private FloatingActionButton fabPrincipal, fab1, fab2, fab3;
    private Animation animFabAbertura, animFabFechamento, animFabCima, animFabBaixo, animFabNoroesteAbre, animFabNoroesteFecha, animFabNordesteAbre, animFabNordesteFecha, logout;
    private boolean fabVisivel = false;
    private ViewPager2 viewPager2;

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
        setStatusBarColor();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(cavalo.getPlaca());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        addMenuProvider(this, this, Lifecycle.State.RESUMED);
    }

    private void configuraUi(Cavalo cavalo) {
        TextView nome = binding.motorista;

        try {
            nome.setText(cavalo.getMotorista().getNome());
        } catch (NullPointerException e) {
            e.printStackTrace();
            e.getMessage();
            nome.setText("Sem motorista");
        }
    }

    private Cavalo recebeReferenciaDeCavalo() {
        Cavalo cavalo;
        Intent dados = getIntent();
        CavaloDAO cavaloDao = new CavaloDAO();
        int cavaloId = 0;

        if (dados.hasExtra(CHAVE_ID_CAVALO)){
            cavaloId = dados.getIntExtra(CHAVE_ID_CAVALO, VALOR_DEFAUT);
        }

        cavalo = cavaloDao.localizaPeloId(cavaloId);
        return cavalo;
    }

    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);
    }

    private void configuraNavegacao(Cavalo cavalo) {
        configuraFabs(cavalo);
        configuraNavegacaoAppBar();
    }

    private void configuraNavegacaoAppBar() {
        binding.actAreaMotoristaImgResumo.setColorFilter(Color.parseColor("#E74C3C"));
        binding.actAreaMotoristaTxtResumo.setTextColor(Color.parseColor("#E74C3C"));

        binding.actAreaMotoristaIcResumo.setOnClickListener(v -> {
            navegaComClickViewPager(0, binding.actAreaMotoristaImgResumo, binding.actAreaMotoristaTxtResumo);
        });
        binding.actAreaMotoristaIcFrete.setOnClickListener(v -> {
            navegaComClickViewPager(1, binding.actAreaMotoristaImgFrete, binding.actAreaMotoristaTxtFrete);
        });
        binding.actAreaMotoristaIcAbastecimento.setOnClickListener(v -> {
            navegaComClickViewPager(2, binding.actAreaMotoristaImgAbastecimento, binding.actAreaMotoristaTxtAbastecimento);
        });
        binding.actAreaMotoristaIcDespesa.setOnClickListener(v -> {
            navegaComClickViewPager(3, binding.actAreaMotoristaImgDespesa, binding.actAreaMotoristaTxtDespesa);
        });
    }

    private void configuraViewPager(Cavalo cavalo) {
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

                        logout = AnimationUtils.loadAnimation(AreaMotoristaActivity.this, R.anim.anim_logout);
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

                        logout = AnimationUtils.loadAnimation(AreaMotoristaActivity.this, R.anim.anim_logout);
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

                        logout = AnimationUtils.loadAnimation(AreaMotoristaActivity.this, R.anim.anim_logout);
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

                        logout = AnimationUtils.loadAnimation(AreaMotoristaActivity.this, R.anim.anim_logout);
                        break;
                }
            }
        });
    }

    private void navegaComClickViewPager(int idItem, ImageView icon, TextView txt) {
        int indiceFragments = viewPager2.getCurrentItem();
        if (indiceFragments != idItem) {
            viewPager2.setCurrentItem(idItem);
            logout = AnimationUtils.loadAnimation(this, R.anim.anim_logout);

            alteraCor(icon, txt, indiceFragments);

        } else {
            Toast.makeText(this, "Você está aqui!", Toast.LENGTH_SHORT).show();
        }
    }

    private void alteraCor(ImageView icon, TextView txt, int indiceFragments) {
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
            startActivity(intent);
        });
        fab2.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_ABASTECIMENTO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            startActivity(intent);
        });
        fab3.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            startActivity(intent);
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

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
        menu.removeItem(R.id.menu_padrao_search);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.menu_padrao_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }
}

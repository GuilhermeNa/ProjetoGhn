package br.com.transporte.AppGhn.ui.activity;

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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivitySegurosBinding;
import br.com.transporte.AppGhn.ui.fragment.seguros.SegurosInformacoesGeraisFragment;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.SeguroFrotaFragment;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.SeguroVidaFragment;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class SegurosActivity extends AppCompatActivity implements SegurosInformacoesGeraisFragment.Callback {

    private ActivitySegurosBinding binding;
    private FloatingActionButton fabPrincipal, fabSeguroFrota, fabSeguroVida;
    private boolean fabEscondido = true;
    private NavController controlador;
    private final ActivityResultLauncher<Intent> activityResultLauncherFrota = getActivityResultLauncherFrota();
    private final ActivityResultLauncher<Intent> activityResultLauncherVida = getActivityResultLauncherVida();
    private SeguroFrotaFragment fragmentFrota;

    @NonNull
    private ActivityResultLauncher<Intent> getActivityResultLauncherFrota() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_OK:

                            if (Objects.requireNonNull(controlador.getCurrentDestination()).getId() == R.id.navSeguroFrota) {
                                fragmentFrota.solicitaAtualizacao(REGISTRO_CRIADO);
                            } else {
                                MensagemUtil.toast(this, REGISTRO_CRIADO);
                            }
                            break;

                        case RESULT_CANCELED:
                            MensagemUtil.toast(this, NENHUMA_ALTERACAO_REALIZADA);
                            break;
                    }
                });
    }

    @NonNull
    private ActivityResultLauncher<Intent> getActivityResultLauncherVida() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();

                    switch (resultCode) {
                        case RESULT_OK:
                            if (Objects.requireNonNull(controlador.getCurrentDestination()).getId() == R.id.navSeguroVida) {
                                NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container_seguros);
                                SeguroVidaFragment fragment = (SeguroVidaFragment) Objects.requireNonNull(navHost).getChildFragmentManager().getFragments().get(0);
                                fragment.atualizaAdapter(REGISTRO_CRIADO);
                            } else {
                                MensagemUtil.toast(this, REGISTRO_CRIADO);
                            }
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
        criaReferenciaDoFragmentFrota();
        inicializaCamposDaView();
        setStatusBarColor();
        configuraFabs();
        configuraBottomNavigation();
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraBottomNavigation() {
        BottomNavigationView bottomAppBar = binding.bottomNavigation;
        controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_seguros);
        NavigationUI.setupWithNavController(bottomAppBar, controlador);

        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.navSeguroFrota:
                case R.id.navSeguroVida:
                    bottomAppBar.setVisibility(View.VISIBLE);
                    fabPrincipal.setVisibility(View.VISIBLE);
                    break;

                case R.id.navSeguroInformacoes:
                    bottomAppBar.setVisibility(View.GONE);
                    fabPrincipal.setVisibility(View.GONE);
                    break;
            }
        });
    }

    private void criaReferenciaDoFragmentFrota() {
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container_seguros);
        fragmentFrota = (SeguroFrotaFragment) Objects.requireNonNull(navHost).getChildFragmentManager().getFragments().get(0);
    }

    private void configuraFabs() {
        fabPrincipal.setOnClickListener(v -> animacaoFabs());

        fabSeguroFrota.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_FROTA);
            intent.putExtra(CHAVE_REQUISICAO, ADICIONANDO);
            activityResultLauncherFrota.launch(intent);
        });

        fabSeguroVida.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_SEGURO_VIDA);
            intent.putExtra(CHAVE_REQUISICAO, ADICIONANDO);
            activityResultLauncherVida.launch(intent);
        });
    }

    private void animacaoFabs() {
        Animation animFabNoroesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_abre);
        Animation animFabNoroesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_fecha);
        Animation animFabNordesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_abre);
        Animation animFabNordesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_fecha);

        if (fabEscondido) {
            fabSeguroFrota.setVisibility(View.VISIBLE);
            fabSeguroVida.setVisibility(View.VISIBLE);
            fabSeguroFrota.startAnimation(animFabNoroesteAbre);
            fabSeguroVida.startAnimation(animFabNordesteAbre);

        } else {
            fabSeguroFrota.setVisibility(View.INVISIBLE);
            fabSeguroVida.setVisibility(View.INVISIBLE);
            fabSeguroFrota.startAnimation(animFabNoroesteFecha);
            fabSeguroVida.startAnimation(animFabNordesteFecha);
        }
        fabEscondido = !fabEscondido;
    }

    private void inicializaCamposDaView() {
        fabPrincipal = binding.fab;
        fabSeguroFrota = binding.fabFrota;
        fabSeguroVida = binding.fabDemaisRamos;
    }

    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);
    }

    @Override
    public void atualizaFrotaAdapter(String msg) {
        fragmentFrota.solicitaAtualizacao(msg);
    }

}
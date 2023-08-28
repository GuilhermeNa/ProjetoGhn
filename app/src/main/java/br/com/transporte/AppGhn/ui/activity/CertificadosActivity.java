package br.com.transporte.AppGhn.ui.activity;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.model.enums.TipoFormulario.ADICIONANDO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CERTIFICADOS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityCertificadosBinding;
import br.com.transporte.AppGhn.ui.activity.extensions.StatusBarUtil;
import br.com.transporte.AppGhn.ui.fragment.certificados.CertificadosDiretosFragment;
import br.com.transporte.AppGhn.ui.fragment.certificados.CertificadosIndiretosFragment;

public class CertificadosActivity extends AppCompatActivity {
    private FloatingActionButton fabIndireta, fabDireta;
    private ActivityCertificadosBinding binding;
    private FloatingActionButton fabPrincipal;
    private boolean fabEscondido = true;
    private NavController controlador;

    private final ActivityResultLauncher<Intent> activityResultLauncherFormularioCertificadoIndireto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        if (Objects.requireNonNull(controlador.getCurrentDestination()).getId() ==
                                R.id.navCertificadosIndiretos) {
                            NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container_certificados);
                            CertificadosIndiretosFragment fragmentIndiretos = (CertificadosIndiretosFragment) navHost.getChildFragmentManager().getFragments().get(0);
                            fragmentIndiretos.atualizaAdapter("Registro criado");

                        } else if (Objects.requireNonNull(controlador.getCurrentDestination()).getId() == R.id.navCertificadosDiretos) {
                            Toast.makeText(this, "Registro criado", Toast.LENGTH_SHORT).show();

                        }
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this, "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                        break;
                }
            });

    private final ActivityResultLauncher<Intent> activityResultLauncherFormularioCertificadoDireto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        if (Objects.requireNonNull(controlador.getCurrentDestination()).getId() == R.id.navCertificadosDiretos){
                            NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container_certificados);
                            CertificadosDiretosFragment fragmentDiretos = (CertificadosDiretosFragment) navHost.getChildFragmentManager().getFragments().get(0);
                            fragmentDiretos.atualizaAdapter("Registro criado");
                        }
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this, "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
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
        StatusBarUtil.setStatusBarColor(this, getWindow());

        inicializaCamposDaView();
        configuraFabs();
        configuraBottomNavigation();

    }

    @SuppressLint("NonConstantResourceId")
    private void configuraBottomNavigation() {
        Animation animacima = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation animabaixo = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        BottomNavigationView navigationView = binding.bottomNavigation;
        controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_certificados);
        NavigationUI.setupWithNavController(navigationView, controlador);

        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.navCertificadosDiretos:
                case R.id.navCertificadosIndiretos:

                    if (fabDireta.getVisibility() == View.VISIBLE) {
                        animacaoFabs();
                    }

                    if (navigationView.getVisibility() == View.GONE) {

                        navigationView.setVisibility(View.VISIBLE);
                        fabPrincipal.setVisibility(View.VISIBLE);
                        navigationView.startAnimation(animacima);
                        fabPrincipal.startAnimation(animacima);
                    }
                    break;

                case R.id.navCertificadosDiretosDetalhes:

                    if (fabDireta.getVisibility() == View.VISIBLE) {
                        animacaoFabs();
                    }

                    navigationView.setVisibility(View.GONE);
                    fabPrincipal.setVisibility(View.GONE);
                    navigationView.startAnimation(animabaixo);
                    fabPrincipal.startAnimation(animabaixo);

                    break;

            }
        });
    }

    private void inicializaCamposDaView() {
        fabDireta = binding.fabDireta;
        fabIndireta = binding.fabIndireta;
        fabPrincipal = binding.fab;

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
            fabDireta.setVisibility(View.VISIBLE);
            fabIndireta.setVisibility(View.VISIBLE);
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






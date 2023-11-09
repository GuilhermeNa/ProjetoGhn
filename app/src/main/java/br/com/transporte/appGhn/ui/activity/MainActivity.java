package br.com.transporte.appGhn.ui.activity;

import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.CAVALOS;
import static br.com.transporte.appGhn.ui.fragment.ImpostosFragment.IMPOSTOS;
import static br.com.transporte.appGhn.ui.fragment.desempenho.DesempenhoFragment.DESEMPENHO;
import static br.com.transporte.appGhn.ui.fragment.home.GerenciamentoFragment.GERENCIAMENTO;
import static br.com.transporte.appGhn.ui.fragment.home.frota.FrotaFragment.FROTA;
import static br.com.transporte.appGhn.ui.fragment.home.funcionarios.FuncionariosFragment.FUNCIONARIOS;
import static br.com.transporte.appGhn.ui.fragment.media.MediaFragment.MEDIA;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityMainBinding;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.viewmodel.MainActViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.MainActViewModelFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        br.com.transporte.AppGhn.databinding.ActivityMainBinding binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaViewModel();
        configuraNavigation();
    }

    @SuppressLint("NonConstantResourceId")
    private void configuraNavigation() {
        final NavController controller =
                Navigation.findNavController(this, R.id.nav_host_fragment_container);

        final ActionBar supportActionBar =
                getSupportActionBar();

        controller.addOnDestinationChangedListener(
                (navController, navDestination, bundle) -> {
                    switch (navDestination.getId()) {
                        case R.id.navLogin:
                            if (supportActionBar != null) {
                                supportActionBar.hide();
                            }
                            break;

                        case R.id.navHome:
                            if (supportActionBar != null) {
                                supportActionBar.show();
                                supportActionBar.setDisplayShowHomeEnabled(false);
                                supportActionBar.setDisplayHomeAsUpEnabled(false);
                                supportActionBar.setDisplayShowTitleEnabled(false);
                            }
                            break;

                        case R.id.navMotoristas:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                                supportActionBar.setTitle(FUNCIONARIOS);
                            }
                            break;

                        case R.id.navFrota:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                                supportActionBar.setTitle(FROTA);
                            }
                            break;

                        case R.id.navGerenciamento:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                                supportActionBar.setTitle(GERENCIAMENTO);
                                supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                            }
                            break;

                        case R.id.navSelecionaCavalo:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                                supportActionBar.setTitle(CAVALOS);
                            }
                            break;

                        case R.id.navManutencaoDetalhes:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                            }
                            break;

                        case R.id.navDesempenho:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                                supportActionBar.setTitle(DESEMPENHO);
                            }
                            break;

                        case R.id.navMedia:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                                supportActionBar.setTitle(MEDIA);
                            }
                            break;

                        case R.id.navImpostosFragment:
                            if (supportActionBar != null) {
                                supportActionBar.setDisplayHomeAsUpEnabled(true);
                                supportActionBar.setDisplayShowTitleEnabled(true);
                                supportActionBar.setTitle(IMPOSTOS);
                            }
                            break;
                    }
                });
    }

    private void inicializaViewModel() {
        CavaloRepository cavaloRepository = new CavaloRepository(this);
        MotoristaRepository motoristaRepository = new MotoristaRepository(this);
        MainActViewModelFactory factory = new MainActViewModelFactory(cavaloRepository, motoristaRepository);
        ViewModelProvider provedor = new ViewModelProvider(this, factory);
        MainActViewModel viewModel = provedor.get(MainActViewModel.class);
    }

}
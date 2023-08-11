package br.com.transporte.AppGhn.ui.activity;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityDespesasAdmBinding;
import br.com.transporte.AppGhn.ui.activity.extensions.StatusBarUtil;

public class DespesasAdmActivity extends AppCompatActivity {
    private ActivityDespesasAdmBinding binding;
    private FloatingActionButton fabPrincipal, fabDireto, fabIndireto;
    private boolean fabEscondido = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDespesasAdmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inicializaCamposDaView();
        StatusBarUtil.setStatusBarColor(this, getWindow());
        configuraFabs();


        NavController controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_despesa);
        BottomNavigationView bottomNavigation = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomNavigation, controlador);

    }

    private void configuraFabs() {
        fabPrincipal.setOnClickListener(v -> animacaoFabs());

        fabDireto.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_DESPESA_ADM);
            startActivity(intent);
        });

        fabIndireto.setOnClickListener(v -> {
            animacaoFabs();
            Intent intent = new Intent(this, FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_DESPESA_ADM);
            startActivity(intent);

        });


    }

    private void animacaoFabs() {
        Animation animFabNoroesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_abre);
        Animation animFabNoroesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_noroeste_fecha);
        Animation animFabNordesteAbre = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_abre);
        Animation animFabNordesteFecha = AnimationUtils.loadAnimation(this, R.anim.fab_animacao_nordeste_fecha);

        if (fabEscondido) {
            fabDireto.setVisibility(View.VISIBLE);
            fabIndireto.setVisibility(View.VISIBLE);
            fabDireto.startAnimation(animFabNoroesteAbre);
            fabIndireto.startAnimation(animFabNordesteAbre);

        } else {
            fabDireto.setVisibility(View.INVISIBLE);
            fabIndireto.setVisibility(View.INVISIBLE);
            fabDireto.startAnimation(animFabNoroesteFecha);
            fabIndireto.startAnimation(animFabNordesteFecha);
        }
        fabEscondido = !fabEscondido;
    }

    private void inicializaCamposDaView() {
        fabPrincipal = binding.fab;
        fabDireto = binding.fabDireta;
        fabIndireto = binding.fabIndireta;
    }





}
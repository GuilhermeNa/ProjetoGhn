package br.com.transporte.AppGhn.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityComissoesBinding;

public class ComissoesActivity extends AppCompatActivity {
    private ActivityComissoesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComissoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setStatusBarColor();

        BottomNavigationView navigationView = binding.bottomNavigation;
        NavController controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_comissao);
        NavigationUI.setupWithNavController(navigationView, controlador);

        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()){
                case R.id.navComissoes:
                case R.id.navComissoesPagas:
                    navigationView.setVisibility(View.VISIBLE);
                    break;

                case R.id.navComissoesDetalhes:
                case R.id.navComissoesPagasDetalhes:
                    navigationView.setVisibility(View.GONE);
                    break;

            }
        });
    }

    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);

    }
}
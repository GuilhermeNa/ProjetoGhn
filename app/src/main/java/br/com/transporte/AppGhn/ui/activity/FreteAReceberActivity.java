package br.com.transporte.AppGhn.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityFreteAreceberBinding;

public class FreteAReceberActivity extends AppCompatActivity {
    private ActivityFreteAreceberBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFreteAreceberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBarColor();

        BottomNavigationView bottomNavigation = binding.bottomNavigation;
        NavController controlador = Navigation.findNavController(this, R.id.nav_host_fragment_container_frete_receber);
        NavigationUI.setupWithNavController(bottomNavigation, controlador);

        controlador.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()){
                case R.id.navFreteReceber:
                case R.id.navFreteReceberPago:
                    bottomNavigation.setVisibility(View.VISIBLE);
                    break;
                case R.id.navFreteAReceberResumo:
                    bottomNavigation.setVisibility(View.GONE);
                    break;
            }
        });

    }

    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);
    }
}
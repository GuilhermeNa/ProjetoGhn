package br.com.transporte.AppGhn.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityMainBinding;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.viewmodel.MainActViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.MainActViewModelFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        br.com.transporte.AppGhn.databinding.ActivityMainBinding binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBarColor(this, getWindow());
        inicializaViewModel();
    }

    private void inicializaViewModel() {
        CavaloRepository cavaloRepository = new CavaloRepository(this);
        MotoristaRepository motoristaRepository = new MotoristaRepository(this);
        MainActViewModelFactory factory = new MainActViewModelFactory(cavaloRepository, motoristaRepository);
        ViewModelProvider provedor = new ViewModelProvider(this, factory);
        MainActViewModel viewModel =  provedor.get(MainActViewModel.class);
    }

    public static void setStatusBarColor(final Context context, final Window window) {
        int color = ContextCompat.getColor(context, R.color.midnightblue);
        window.setStatusBarColor(color);
    }
}
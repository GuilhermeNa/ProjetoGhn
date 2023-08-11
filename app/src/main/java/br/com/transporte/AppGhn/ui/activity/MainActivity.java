package br.com.transporte.AppGhn.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getSupportActionBar().hide();
        setStatusBarColor();

    }

    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);
    }
}
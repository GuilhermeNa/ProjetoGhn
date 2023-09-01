package br.com.transporte.AppGhn.util;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.com.transporte.AppGhn.R;

public class ToolbarUtil {
    private final String titulo;
    private ActionBar supportActionBar;

    public ToolbarUtil(String titulo) {
        this.titulo = titulo;
    }

    public void configuraToolbar(Activity activity, Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        appCompatActivity.setSupportActionBar(toolbar);
        supportActionBar = appCompatActivity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
            supportActionBar.setTitle(titulo);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
    }

    public void setTitleAtivo(boolean isAtivo) {
        supportActionBar.setDisplayShowTitleEnabled(isAtivo);
    }
}

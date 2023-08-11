package br.com.transporte.AppGhn.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Window;

import br.com.transporte.AppGhn.R;

public class DesenvolvimentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desenvolvimento);
        getSupportActionBar().hide();
        setStatusBarColor();
    }

    private void setStatusBarColor() {
        int color = ContextCompat.getColor(this, R.color.midnightblue);
        getWindow().setStatusBarColor(color);

    }
}
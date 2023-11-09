package br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.extensions;

import static br.com.transporte.appGhn.ui.fragment.seguros.TipoDeSeguro.FROTA;
import static br.com.transporte.appGhn.ui.fragment.seguros.TipoDeSeguro.VIDA;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.ui.fragment.seguros.TipoDeSeguro;

public class SeguroInformacoesGeraisMenuProviderExt implements MenuProvider {

    private final TipoDeSeguro tipoDeSeguro;
    private SeguroInformacoesGeraisMenuProviderListener listener;

    public SeguroInformacoesGeraisMenuProviderExt(TipoDeSeguro tipoDeSeguro) {
        this.tipoDeSeguro = tipoDeSeguro;
    }

    public void setListener(SeguroInformacoesGeraisMenuProviderListener listener) {
        this.listener = listener;
    }

    public interface SeguroInformacoesGeraisMenuProviderListener {
        void onLogoutClick();
        void onHomeClick();
        void onEditClickQuandoSeguroFrota();
        void onEditClickQuandoSeguroVida();
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.removeItem(R.id.menu_padrao_logout);
        menu.removeItem(R.id.menu_padrao_search);
        menu.findItem(R.id.menu_padrao_editar).setVisible(true);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                listener.onLogoutClick();
                break;

            case R.id.menu_padrao_editar:
                onEditClickUseCase();
                break;

            case android.R.id.home:
                listener.onHomeClick();
                break;
        }
        return false;
    }

    private void onEditClickUseCase() {
        if (tipoDeSeguro == FROTA) {
           listener.onEditClickQuandoSeguroFrota();
        } else if (tipoDeSeguro == VIDA) {
            listener.onEditClickQuandoSeguroVida();
        }
    }
}


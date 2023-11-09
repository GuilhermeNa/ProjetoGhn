package br.com.transporte.appGhn.ui.activity.areaMotoristaActivity;


import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import br.com.transporte.AppGhn.R;

public class AreaMotoristaMenuProviderHelper implements MenuProvider {

    public MenuProviderCallback callback;

    public void setMenuProviderCallback(MenuProviderCallback callback) {
        this.callback = callback;
    }

    public interface MenuProviderCallback {
        void onLogoutCLick();

        void onHomeClick();
    }

//--------------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.removeItem(R.id.menu_padrao_editar);
        menu.removeItem(R.id.menu_padrao_search);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                callback.onLogoutCLick();
                break;
            case android.R.id.home:
                callback.onHomeClick();
                break;
        }
        return false;
    }
}

package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.comissoesDetalhesHelpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import br.com.transporte.AppGhn.R;

public class ComissoesMenuProviderHelper implements MenuProvider {

    private final Context context;
    private CallbackMenuProvider callbackMenuProvider;

    public void setCallbackMenuProvider(CallbackMenuProvider callbackMenuProvider) {
        this.callbackMenuProvider = callbackMenuProvider;
    }

    public ComissoesMenuProviderHelper(Context context) {
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

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
                callbackMenuProvider.logoutCLick();
                break;

            case android.R.id.home:
                callbackMenuProvider.homeClick();

                break;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Callback                                          ||
    //----------------------------------------------------------------------------------------------

   public interface CallbackMenuProvider {
        void logoutCLick();

        void homeClick();
   }

}

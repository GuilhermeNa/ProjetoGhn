package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;

public class SeguroFrotaMenuProviderHelper implements MenuProvider {

    private final List<DespesaComSeguroFrota> copiaDataSet;
    private CallbackMenuProvider callbackMenuProvider;
    private final RoomCavaloDao cavaloDao;

    public void setCallbackMenuProvider(CallbackMenuProvider callbackMenuProvider) {
        this.callbackMenuProvider = callbackMenuProvider;
    }

    public SeguroFrotaMenuProviderHelper(List<DespesaComSeguroFrota> copiaDataSet, RoomCavaloDao cavaloDao) {
        this.copiaDataSet = copiaDataSet;
        this.cavaloDao = cavaloDao;
    }

    //----------------------------------------------------------------------------------------------
    //                                        OnCreate                                            ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    //----------------------------------------------------------------------------------------------
    //                                        OnPrepare                                           ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.removeItem(R.id.menu_padrao_editar);
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        SearchView search = (SearchView) busca.getActionView();

        search.setOnSearchClickListener(v -> {
            logout.setVisible(false);
            callbackMenuProvider.searchViewAtiva();
        });
        search.setOnCloseListener(() -> {
            logout.setVisible(true);
            callbackMenuProvider.searchViewInativa();
            return false;
        });
        configuraBusca(search);
    }

    private void  configuraBusca(@NonNull SearchView search) {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<DespesaComSeguroFrota> dataSet_search = new ArrayList<>();
                realizaBuscaNoBancoDeDados(newText, dataSet_search);
                callbackMenuProvider.realizaBusca(dataSet_search);
                return false;
            }

            private void realizaBuscaNoBancoDeDados(String newText, List<DespesaComSeguroFrota> lista) {
                for (DespesaComSeguroFrota d : copiaDataSet) {
             //       String placa = cavaloDao.localizaPeloId(d.getRefCavaloId()).getPlaca();
              //      if (placa.toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
              //          lista.add(d);
              //      }
                }
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //                                        OnMenuItemSelected                                  ||
    //----------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                callbackMenuProvider.logoutClick();
                break;

            case android.R.id.home:
                callbackMenuProvider.homeClick();
                break;
        }
        return false;
    }

    public interface CallbackMenuProvider {
        void realizaBusca(List<DespesaComSeguroFrota> dataSet_search);
        void searchViewAtiva();
        void searchViewInativa();

        void logoutClick();

        void homeClick();

    }
}

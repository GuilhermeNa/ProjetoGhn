package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.comissoesPagasHelpers;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;

public class ComissoesPagasMenuProviderHelper implements MenuProvider {

    private final List<CustosDeSalario> dataSet;
    private final RoomCavaloDao cavaloDao;
    public CallbackSearch callbackSearch;

    public void setCallbackSearch(CallbackSearch callbackSearch) {
        this.callbackSearch = callbackSearch;
    }

    public ComissoesPagasMenuProviderHelper(List<CustosDeSalario> dataSet, @NonNull GhnDataBase dataBase) {
        this.dataSet = dataSet;
        cavaloDao = dataBase.getRoomCavaloDao();

    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateMenu                                      ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.removeItem(R.id.menu_padrao_editar);
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        SearchView searchView = (SearchView) busca.getActionView();

        Objects.requireNonNull(searchView).setOnSearchClickListener(v -> {
            logout.setVisible(false);
            callbackSearch.searchViewAtiva();
        });

        searchView.setOnCloseListener(() -> {
            logout.setVisible(true);
            callbackSearch.searchViewInativa();
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
               /* List<CustosDeSalario> dataSet_search = new ArrayList<>();
                for (CustosDeSalario s : dataSet) {
                    String placa = cavaloDao.localizaPeloId(s.getRefCavaloId()).getPlaca();
                    if (placa.toLowerCase().contains(newText.toLowerCase())) {
                        dataSet_search.add(s);
                    }
                }
                callbackSearch.realizaBusca(dataSet_search);*/
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                callbackSearch.onLogoutClick();
                break;

            case android.R.id.home:
                callbackSearch.onHomeClick();
                break;

        }
        return false;
    }

    public void atualiza(List<CustosDeSalario> copiaDataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(copiaDataSet);
    }

    public interface CallbackSearch {
        void searchViewAtiva();
        void searchViewInativa();
        void realizaBusca(List<CustosDeSalario> dataSet_search);
        void onLogoutClick();
        void onHomeClick();
    }
}

package br.com.transporte.appGhn.ui.fragment.selecionaCavalo;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.model.Cavalo;

public class SelecionaCavaloMenuProviderHelper implements MenuProvider {

    private final List<Cavalo> copiaDataSet;
    private MenuProviderCallback callback;

    public SelecionaCavaloMenuProviderHelper(List<Cavalo> copiaDataSet) {
        this.copiaDataSet = copiaDataSet;
    }

    public void setCallback(MenuProviderCallback callback) {
        this.callback = callback;
    }

    public void atualizaDataSet(List<Cavalo> copiaDataSet) {
        this.copiaDataSet.clear();
        this.copiaDataSet.addAll(copiaDataSet);
    }

    public interface MenuProviderCallback {
        void buscaFinalizada(List<Cavalo> dataSet_search);

        void onSearchSelecionada();

        void onSearchDesselecionada();

        void onLogoutClick();

        void onHomeClick();
    }

    //----------------------------------------------------------------------------------------------
    //                                         On Create                                          ||
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
        SearchView searchView = getSearchView(menu);

        if (searchView != null) {
            configuraListenerDeSelecaoDaSearchView(logout, searchView);
            configuraListenerDeDesselecaoDaSearchView(logout, searchView);
            configuraBuscaNaSearchView(searchView);
        }
    }

    private void configuraBuscaNaSearchView(@NonNull final SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Cavalo> dataSet_search = new ArrayList<>();
                for (Cavalo c : copiaDataSet) {
                    if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        dataSet_search.add(c);
                    }
                }
                callback.buscaFinalizada(dataSet_search);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void configuraListenerDeDesselecaoDaSearchView(MenuItem logout, @NonNull SearchView searchView) {
        searchView.setOnCloseListener(() -> {
            logout.setVisible(true);
            callback.onSearchDesselecionada();
            return false;
        });
    }

    private void configuraListenerDeSelecaoDaSearchView(final MenuItem logout, @NonNull final SearchView searchView) {
        searchView.setOnSearchClickListener(v -> {
            logout.setVisible(false);
            callback.onSearchSelecionada();
        });
    }

    @Nullable
    private SearchView getSearchView(@NonNull final Menu menu) {
        MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        return (SearchView) busca.getActionView();
    }

    //----------------------------------------------------------------------------------------------
    //                                     Menu Item Selected                                     ||
    //----------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                callback.onLogoutClick();
                break;

            case android.R.id.home:
                callback.onHomeClick();
                break;
        }
        return false;
    }

}

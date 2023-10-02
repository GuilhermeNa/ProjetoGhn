package br.com.transporte.AppGhn.ui.activity.freteAReceberActivity.helpers;

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
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;

public class FreteAReceberActMenuProviderHelper implements MenuProvider {
    private final List<Frete> dataSetFrete = new ArrayList<>();
    private final List<Cavalo> dataSetCavalo = new ArrayList<>();
    private MenuProviderCallback callback;
    public MenuItem edita;
    public MenuItem busca;

    public void setCallback(MenuProviderCallback callback) {
        this.callback = callback;
    }

    public void defineDataSetFrete(List<Frete> dataSet) {
        this.dataSetFrete.clear();
        this.dataSetFrete.addAll(dataSet);
    }

    public void defineDataSetCavalo(List<Cavalo> dataSet) {
        this.dataSetCavalo.clear();
        this.dataSetCavalo.addAll(dataSet);
    }

    public interface MenuProviderCallback {
        void onSearchClick();

        void onSearchRemoved();

        void realizaBusca(List<Frete> dataSet_search);

        void onLogoutCLick();

        void onHomeClick();

        void onEditClick();
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        busca = menu.findItem(R.id.menu_padrao_search);
        edita = menu.findItem(R.id.menu_padrao_editar);
        SearchView searchView = (SearchView) busca.getActionView();

        Objects.requireNonNull(searchView).setOnSearchClickListener(v -> {
            logout.setVisible(false);
            callback.onSearchClick();
        });

        searchView.setOnCloseListener(() -> {
            logout.setVisible(true);
            callback.onSearchRemoved();
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Frete> listaFrete = new ArrayList<>();
                String placa;
                for (Frete f : dataSetFrete) {
                    placa = Objects.requireNonNull(FiltraCavalo.localizaPeloId(dataSetCavalo, f.getRefCavaloId())).getPlaca().toUpperCase(Locale.ROOT);
                    if (placa.contains(newText.toUpperCase(Locale.ROOT))) {
                        listaFrete.add(f);
                    }
                }
                callback.realizaBusca(listaFrete);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_padrao_logout:
                callback.onLogoutCLick();
                break;

            case android.R.id.home:
                callback.onHomeClick();
                break;

            case R.id.menu_padrao_editar:
                callback.onEditClick();
                break;
        }
       return false;
    }

}

package br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.extensions;

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
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.domain.model.DespesaAdmDiretaObject;

public class DespesaAdmDiretaMenuProviderExt implements MenuProvider {
    private DespesaAdmDiretaMenuCallback callback;
    private final List<DespesaAdmDiretaObject> dataSet;

    public DespesaAdmDiretaMenuProviderExt(List<DespesaAdmDiretaObject> dataSet) {
        this.dataSet = dataSet;
    }

    public void setCallback(DespesaAdmDiretaMenuCallback callback) {
        this.callback = callback;
    }

    public interface DespesaAdmDiretaMenuCallback {
        void realizaBusca(List<DespesaAdmDiretaObject> dataSearch);

        void onLogoutClick();

        void onSearchClick();

        void onSearchClear();

        void onHomeClick();

    }

    public void atualizaDataSet(final List<DespesaAdmDiretaObject> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        SearchView searchView = (SearchView) busca.getActionView();

        Objects.requireNonNull(searchView).setOnSearchClickListener(v -> {
            logout.setVisible(false);
            callback.onSearchClick();
        });

        searchView.setOnCloseListener(() -> {
            logout.setVisible(true);
            callback.onSearchClick();
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                final List<DespesaAdmDiretaObject> dataSet_searchView = new ArrayList<>();
                for (DespesaAdmDiretaObject d : dataSet) {
                    String placa = d.getPlaca();
                    if (placa.toUpperCase(Locale.ROOT)
                            .contains(
                                    newText.toUpperCase(Locale.ROOT))
                    ) {
                        dataSet_searchView.add(d);
                    }
                    callback.realizaBusca(dataSet_searchView);
                }
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
                callback.onLogoutClick();
                break;

            case android.R.id.home:
                callback.onHomeClick();
                break;
        }
        return false;
    }


}

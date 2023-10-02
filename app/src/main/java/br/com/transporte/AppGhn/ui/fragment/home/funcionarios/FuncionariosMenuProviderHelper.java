package br.com.transporte.AppGhn.ui.fragment.home.funcionarios;

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
import br.com.transporte.AppGhn.model.Motorista;

public class FuncionariosMenuProviderHelper implements MenuProvider {
    private final List<Motorista> copiaDataSet;
    private MenuProviderCallback callBack;

    public void setCallBack(MenuProviderCallback callBack) {
        this.callBack = callBack;
    }

    public FuncionariosMenuProviderHelper(List<Motorista> copiaDataSet) {
        this.copiaDataSet = copiaDataSet;
    }

    public void atualizaDataSet(List<Motorista> copiaDataSet){
        this.copiaDataSet.clear();
        this.copiaDataSet.addAll(copiaDataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                       OnCreateMenu                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
    }

    //----------------------------------------------------------------------------------------------
    //                                       OnPrepareMenu                                        ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.removeItem(R.id.menu_padrao_editar);
        MenuItem busca = menu.findItem(R.id.menu_padrao_search);
        SearchView search = (SearchView) busca.getActionView();
        configuraExibicaoDeIconesAoAcionarSearchView(menu, search);
        configuraBuscaNoBancoDeDados(search);
    }

    private void configuraBuscaNoBancoDeDados(@NonNull SearchView search) {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Motorista> dataSet_search = new ArrayList<>();
                for (Motorista m : copiaDataSet) {
                    if (m.getNome().toUpperCase(Locale.US).contains(newText.toUpperCase(Locale.US))) {
                        dataSet_search.add(m);
                    }
                }
                callBack.realizaBusca(dataSet_search);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void configuraExibicaoDeIconesAoAcionarSearchView(@NonNull Menu menu, SearchView search) {
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        Objects.requireNonNull(search).setOnSearchClickListener(v -> {
            logout.setVisible(false);
            callBack.searchViewAtivada();
        });
        search.setOnCloseListener(() -> {
            logout.setVisible(true);
            callBack.searchViewDesativada();
            return false;
        });
    }

    //----------------------------------------------------------------------------------------------
    //                                     OnMenuItemSelected                                     ||
    //----------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
               callBack.onLogoutClick();
                break;

            case android.R.id.home:
                callBack.onHomeCLick();
                break;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //                                     OnMenuItemSelected                                     ||
    //----------------------------------------------------------------------------------------------

    public interface MenuProviderCallback {
        void realizaBusca(List<Motorista> dataSet);
        void searchViewAtivada();
        void searchViewDesativada();
        void onLogoutClick();
        void onHomeCLick();
    }

}

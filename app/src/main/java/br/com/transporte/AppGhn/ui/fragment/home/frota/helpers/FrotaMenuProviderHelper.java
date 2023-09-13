package br.com.transporte.AppGhn.ui.fragment.home.frota.helpers;

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
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.fragment.home.frota.FrotaFragment;

public class FrotaMenuProviderHelper implements MenuProvider {

    private List<Cavalo> copiaDataSetCavalos;
    private List<SemiReboque> copiaDataSetReboques;
    private List<Cavalo> dataSet_searchView_cavalo;
    private List<SemiReboque> dataSet_searchView_semiReboque;
    private MenuProviderCallback callBack;

    public interface MenuProviderCallback {
        void realizaBusca(List<Cavalo> dataSet_searchView_cavalo, List<SemiReboque> dataSet_searchView_semiReboque);

        void searchViewAtivada();

        void searchViewDesativada();

        void onLogoutClick();

        void onHomeClick();
    }

    public void setCallBack(MenuProviderCallback callBack) {
        this.callBack = callBack;
    }

    public void atualizaCavalos(List<Cavalo> listaDeCavalos) {
        // Usado pelo fragment para atualizar a copia do Dataset
        if(copiaDataSetCavalos == null) copiaDataSetCavalos = new ArrayList<>();
        this.copiaDataSetCavalos.clear();
        this.copiaDataSetCavalos.addAll(listaDeCavalos);
    }

    public void atualizaReboques(List<SemiReboque> listaDeReboques) {
        // Usado pelo fragment para atualizar a copia do Dataset
        if(copiaDataSetReboques == null) copiaDataSetReboques = new ArrayList<>();
        this.copiaDataSetReboques.clear();
        this.copiaDataSetReboques.addAll(listaDeReboques);
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
        if(search != null){
            configuraExibicaoDeIconesAoAcionarSearchView(menu, search);
            configuraBuscaNoBancoDeDados(search);
        }
    }

    private void configuraExibicaoDeIconesAoAcionarSearchView(@NonNull Menu menu, SearchView search) {
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
        if (search != null && logout != null) {
            search.setOnSearchClickListener(v -> {
                logout.setVisible(false);
                callBack.searchViewAtivada();
            });
            search.setOnCloseListener(() -> {
                logout.setVisible(true);
                callBack.searchViewDesativada();
                return false;
            });
        }

    }

    private void configuraBuscaNoBancoDeDados(@NonNull SearchView search) {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                dataSet_searchView_cavalo = buscaPorCavalosNoBd(newText);
                dataSet_searchView_semiReboque = buscaPorSrNoBd(newText);
                callBack.realizaBusca(dataSet_searchView_cavalo, dataSet_searchView_semiReboque);
                return false;
            }

            @NonNull
            private List<Cavalo> buscaPorCavalosNoBd(String newText) {
                List<Cavalo> lista = new ArrayList<>();
                for (Cavalo c : copiaDataSetCavalos) {
                    if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        lista.add(c);
                    }
                }
                return lista;
            }

            @NonNull
            private List<SemiReboque> buscaPorSrNoBd(String newText) {
                List<SemiReboque> lista = new ArrayList<>();
                for (SemiReboque s : copiaDataSetReboques) {
                    if (s.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        lista.add(s);
                    }
                }
                return lista;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //                                       OnMenuItemSelected                                   ||
    //----------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                callBack.onLogoutClick();
                break;

            case android.R.id.home:
                callBack.onHomeClick();
                break;
        }
        return false;
    }

}

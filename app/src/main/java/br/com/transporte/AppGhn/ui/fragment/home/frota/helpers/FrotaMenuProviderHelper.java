package br.com.transporte.AppGhn.ui.fragment.home.frota.helpers;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.fragment.home.frota.FrotaFragment;

public class FrotaMenuProviderHelper implements MenuProvider {

    private final List<Cavalo> copiaDataSetCavalos_fragment;
    private final List<SemiReboque> copiaDataSetReboques_fragment;
    private List<Cavalo> dataSet_searchView_cavalo;
    private List<SemiReboque> dataSet_searchView_semiReboque;
    private final FrotaFragment fragment;
    private MenuProviderCallback menuProviderCallback;

    public void setMenuProviderCallback(MenuProviderCallback menuProviderCallback) {
        this.menuProviderCallback = menuProviderCallback;
    }

    public FrotaMenuProviderHelper(FrotaFragment fragment, List<Cavalo> copiaDataSetCavalos, List<SemiReboque> copiaDataSetReboques) {
        this.fragment = fragment;
        this.copiaDataSetCavalos_fragment = copiaDataSetCavalos;
        this.copiaDataSetReboques_fragment = copiaDataSetReboques;
    }




    //----------------------------------------------------------------------------------------------
    //                                       OnCreateMenu                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);
    }

    //----------------------------------------------------------------------------------------------
    //                                       OnPrepareMenu                                        ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
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
                menuProviderCallback.searchViewAtivada();
            });
            search.setOnCloseListener(() -> {
                logout.setVisible(true);
                menuProviderCallback.searchViewDesativada();
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
                menuProviderCallback.realizaBusca(dataSet_searchView_cavalo, dataSet_searchView_semiReboque);
                return false;
            }

            @NonNull
            private List<Cavalo> buscaPorCavalosNoBd(String newText) {
                List<Cavalo> lista = new ArrayList<>();
                for (Cavalo c : copiaDataSetCavalos_fragment) {
                    if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        lista.add(c);
                    }
                }
                return lista;
            }

            @NonNull
            private List<SemiReboque> buscaPorSrNoBd(String newText) {
                List<SemiReboque> lista = new ArrayList<>();
                for (SemiReboque s : copiaDataSetReboques_fragment) {
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
                Toast.makeText(fragment.requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                NavController controlador = Navigation.findNavController(fragment.requireView());
                controlador.popBackStack();
                break;
        }
        return false;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    public void atualizaCavalos(List<Cavalo> listaDeCavalos) {
        // Usado pelo fragment para atualizar a copia do Dataset
        this.copiaDataSetCavalos_fragment.clear();
        this.copiaDataSetCavalos_fragment.addAll(listaDeCavalos);
    }

    public void atualizaReboques(List<SemiReboque> listaDeReboques) {
        // Usado pelo fragment para atualizar a copia do Dataset
        this.copiaDataSetReboques_fragment.clear();
        this.copiaDataSetReboques_fragment.addAll(listaDeReboques);
    }

    //----------------------------------------------------------------------------------------------
    //                                       Callback                                              ||
    //----------------------------------------------------------------------------------------------

    public interface MenuProviderCallback {
        void realizaBusca(List<Cavalo> dataSet_searchView_cavalo, List<SemiReboque> dataSet_searchView_semiReboque);

        void searchViewAtivada();

        void searchViewDesativada();
    }
}

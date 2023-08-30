package br.com.transporte.AppGhn.ui.fragment.home.frota.helpers;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;

import android.annotation.SuppressLint;
import android.util.Log;
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

    private final List<Cavalo> dataSetcavalo;
    private final List<SemiReboque> dataSetSemiReboque;
    private final FrotaFragment fragment;
    private MenuProviderCallback menuProviderCallback;

    public void setMenuProviderCallback(MenuProviderCallback menuProviderCallback) {
        this.menuProviderCallback = menuProviderCallback;
    }

    public FrotaMenuProviderHelper(FrotaFragment fragment, List<Cavalo> dataSetcavalo, List<SemiReboque> dataSetSemiReboque) {
        this.fragment = fragment;
        this.dataSetcavalo = dataSetcavalo;
        this.dataSetSemiReboque = dataSetSemiReboque;


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
        configuraExibicaoDeIconesAoAcionarSearchView(menu, search);
        configuraBuscaNoBancoDeDados(search);
    }

    private void configuraBuscaNoBancoDeDados(@NonNull SearchView search) {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cavalo> dataSet_searchView_cavalo = buscaPorCavalosNoBd(newText);
                List<SemiReboque> dataSet_searchView_semiReboque = buscaPorSrNoBd(newText);
                menuProviderCallback.realizaBusca(dataSet_searchView_cavalo, dataSet_searchView_semiReboque);
                return false;
            }

            @NonNull
            private List<Cavalo> buscaPorCavalosNoBd(String newText) {
                List<Cavalo> dataSet = new ArrayList<>();
                for (Cavalo c : dataSetcavalo) {
                    if (c.getPlaca().toLowerCase().contains(newText.toLowerCase())) {
                        dataSet.add(c);
                    }
                }
                return dataSet;
            }

            @NonNull
            private List<SemiReboque> buscaPorSrNoBd(String newText) {
                List<SemiReboque> dataSet = new ArrayList<>();
                for (SemiReboque s : dataSetSemiReboque) {
                    if (s.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        dataSet.add(s);
                    }
                }
                return dataSet;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void configuraExibicaoDeIconesAoAcionarSearchView(@NonNull Menu menu, SearchView search) {
        MenuItem logout = menu.findItem(R.id.menu_padrao_logout);

        if (search == null) {
            Log.d("debug", "SearchView Nula");
            fragment.requireActivity().finish();
        }
        if(logout == null){
            Log.d("debug", "SearchView Nula");
            fragment.requireActivity().finish();
        }

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


    //----------------------------------------------------------------------------------------------
    //                                       Callback                                              ||
    //----------------------------------------------------------------------------------------------

    public interface MenuProviderCallback {
        void realizaBusca(List<Cavalo> dataSet_searchView_cavalo, List<SemiReboque> dataSet_searchView_semiReboque);
        void searchViewAtivada();
        void searchViewDesativada();
    }
}

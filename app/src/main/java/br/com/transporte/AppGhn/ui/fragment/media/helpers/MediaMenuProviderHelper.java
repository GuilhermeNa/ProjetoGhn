package br.com.transporte.AppGhn.ui.fragment.media.helpers;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.adapter.MediaAdapter_Cavalos;
import br.com.transporte.AppGhn.ui.fragment.media.MediaFragment;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class MediaMenuProviderHelper implements MenuProvider {
    private final List<Cavalo> dataSet;
    private final MediaFragment fragment;
    private MenuProviderCallback menuProviderCallback;

    public void setMenuProviderCallback(MenuProviderCallback menuProviderCallback) {
        this.menuProviderCallback = menuProviderCallback;
    }

    public MediaMenuProviderHelper(MediaFragment fragment, List<Cavalo> listaDoAdapter) {
        this.fragment = fragment;
        this.dataSet = new ArrayList<>(listaDoAdapter);
    }

    //----------------------------------------------------------------------------------------------
    //                                       OnCreateMenu                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_editar);

        SearchView busca = configuraSearchIcon(menu);
        configuraExibicaoDeIconesAoAcionarSearchView(menu, busca);
        configuraBuscaNoBancoDeDados(busca);
    }

    private void configuraBuscaNoBancoDeDados(@NonNull SearchView busca) {
        busca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cavalo> dataSet_searchView = new ArrayList<>();

                for (Cavalo c : dataSet) {
                    if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        dataSet_searchView.add(c);
                    }
                }

                menuProviderCallback.realizaBusca(dataSet_searchView);

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void configuraExibicaoDeIconesAoAcionarSearchView(@NonNull Menu menu, @NonNull SearchView busca) {
        MenuItem itemLogout = menu.findItem(R.id.menu_padrao_logout);

        busca.setOnSearchClickListener(v -> itemLogout.setVisible(false));

        busca.setOnCloseListener(() -> {
            itemLogout.setVisible(true);
            return false;
        });
    }

    private SearchView configuraSearchIcon(@NonNull Menu menu) {
        MenuItem itemSearch = menu.findItem(R.id.menu_padrao_search);
        return (SearchView) itemSearch.getActionView();
    }

    //----------------------------------------------------------------------------------------------
    //                                       OnMenuItemSelected                                   ||
    //----------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                MensagemUtil.toast(fragment.requireContext(), "Logout");
                break;

            case android.R.id.home:
                NavController controlador = Navigation.findNavController(fragment.requireView());
                controlador.popBackStack();
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //                                       Metodos Publicos                                     ||
    //----------------------------------------------------------------------------------------------

    public void atualizaDataSet(List<Cavalo> novaListaDoAdapter){
        this.dataSet.clear();
        this.dataSet.addAll(novaListaDoAdapter);
    }

    public interface MenuProviderCallback {
        void realizaBusca(List<Cavalo> dataSet_searchView);
    }

}

package br.com.transporte.AppGhn.ui.fragment.media;

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
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.adapter.MediaAdapter_Cavalos;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class MenuProviderHelperMedia implements MenuProvider {

    private final MediaFragment fragment;
    private final MediaAdapter_Cavalos adapter;

    public MenuProviderHelperMedia(MediaFragment fragment, MediaAdapter_Cavalos adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
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

    private void configuraBuscaNoBancoDeDados(SearchView busca) {
        CavaloDAO cavaloDao = new CavaloDAO();
        busca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cavalo> dataSet = new ArrayList<>();

                for (Cavalo c : cavaloDao.listaTodos()) {
                    if (c.getPlaca().toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                        dataSet.add(c);
                    }
                    adapter.atualiza(dataSet);
                }

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void configuraExibicaoDeIconesAoAcionarSearchView(@NonNull Menu menu, SearchView busca) {
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


}

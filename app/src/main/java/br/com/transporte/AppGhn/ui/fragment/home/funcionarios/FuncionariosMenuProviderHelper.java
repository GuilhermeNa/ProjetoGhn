package br.com.transporte.AppGhn.ui.fragment.home.funcionarios;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.fragment.desempenho.helpers.DesempenhoMenuProviderHelper;

public class FuncionariosMenuProviderHelper implements MenuProvider {

    private final FuncionariosFragment context;
    private MenuProviderCallback menuProviderCallback;

    public void setMenuProviderCallback(MenuProviderCallback menuProviderCallback) {
        this.menuProviderCallback = menuProviderCallback;
    }

    public FuncionariosMenuProviderHelper(FuncionariosFragment context) {
        this.context = context;
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
            RoomMotoristaDao motoristaDao = GhnDataBase.getInstance(context.requireContext()).getRoomMotoristaDao();

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Motorista> dataSet = new ArrayList<>();
                for (Motorista m : motoristaDao.todos()) {
                    if (m.getNome().toLowerCase().contains(newText.toLowerCase())) {
                        dataSet.add(m);
                    }
                }
                menuProviderCallback.realizaBusca(dataSet);
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
            menuProviderCallback.searchViewAtivada();
        });
        search.setOnCloseListener(() -> {
            logout.setVisible(true);
            menuProviderCallback.searchViewDesativada();
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
                Toast.makeText(context.requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                NavController controlador = Navigation.findNavController(context.requireView());
                controlador.popBackStack();
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
    }


}

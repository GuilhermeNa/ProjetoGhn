package br.com.transporte.appGhn.ui.fragment.desempenho.helpers;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.appGhn.ui.fragment.desempenho.DesempenhoFragmentDirections;
import br.com.transporte.appGhn.ui.fragment.desempenho.dialog.BottomDialogDesempenho;

public class DesempenhoMenuProviderHelper implements MenuProvider {
    private final Fragment fragment;
    private Callback callback;
    private List<Cavalo> dataSet;
    private final NavController controlador;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DesempenhoMenuProviderHelper(Fragment fragment) {
        this.fragment = fragment;
        controlador = Navigation.findNavController(fragment.requireView());
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_search);
        MenuItem item = menu.findItem(R.id.menu_padrao_editar);
        item.setVisible(true);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                final NavDirections direction =
                        DesempenhoFragmentDirections.actionGlobalNavLogin();

                controlador.navigate(direction);
                break;

            case R.id.menu_padrao_editar:
                BottomDialogDesempenho dialog = new BottomDialogDesempenho(fragment.requireContext(), fragment);
                dialog.showBottomDialog(dataSet);
                dialog.setCallback((tipo, ano, cavaloId) ->
                        callback.repassaRequisicaoDeFiltragem(tipo, ano, cavaloId)
                );
                break;

            case android.R.id.home:

                controlador.popBackStack();
                break;
        }
        return false;
    }

    public void setListaCavalos(final List<Cavalo> dataSet) {
        this.dataSet = dataSet;
    }

    //----------------------------------------------------------------------------------------------
    //                                          CallBack::this                                    ||
    //----------------------------------------------------------------------------------------------

    public interface Callback {
        void repassaRequisicaoDeFiltragem(TipoDeRequisicao tipo, int ano, Long cavaloId);
    }

}

package br.com.transporte.AppGhn.ui.fragment.desempenho.helpers;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.ui.fragment.desempenho.dialog.BottomDialogDesempenho;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class DesempenhoMenuProviderHelper implements MenuProvider {
    private final Fragment fragment;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DesempenhoMenuProviderHelper(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_padrao, menu);
        menu.removeItem(R.id.menu_padrao_search);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_padrao_logout:
                MensagemUtil.toast(fragment.requireContext(), "Logout");
                break;

            case R.id.menu_padrao_editar:
                BottomDialogDesempenho dialog = new BottomDialogDesempenho(fragment.requireContext(), fragment);
                dialog.showBottomDialog();
                dialog.setCallback((tipo, ano, cavaloId) ->
                        callback.repassaRequisicaoDeFiltragem(tipo, ano, cavaloId)
                );
                break;

            case android.R.id.home:
                NavController controlador = Navigation.findNavController(fragment.requireView());
                controlador.popBackStack();
                break;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //                                          CallBack::this                                    ||
    //----------------------------------------------------------------------------------------------

    public interface Callback {
        void repassaRequisicaoDeFiltragem(TipoDeRequisicao tipo, int ano, Long cavaloId);
    }

}

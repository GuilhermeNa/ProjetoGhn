package br.com.transporte.appGhn.ui.activity.despesaAdm.extensions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.DespesasAdmDiretasFragment;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.indireta.DespesasAdmIndiretasFragment;

public class DespesasActOnFragmentsAttachExt {

    private final NavHostFragment navHost;

    public DespesasActOnFragmentsAttachExt(@NonNull FragmentManager fragmentManager) {
        navHost = (NavHostFragment) fragmentManager
                .findFragmentById(R.id.nav_host_fragment_container_despesa);
    }

    public interface DespesasFragmentsCallback {
        void despesaDiretaFragmentAttached(DespesasAdmDiretasFragment fragment);

        void despesaIndiretaFragmentAttached(DespesasAdmIndiretasFragment fragment);
    }

    //----------------------------------------------------------------------------------------------

    public void getFragmentsWhenAttach(final DespesasFragmentsCallback callback) {
        getFragmentInicialDoNavGraph(callback);
        getDemaisFragmentsAoAtachar(callback);
    }

    private void getFragmentInicialDoNavGraph(@NonNull final DespesasFragmentsCallback callback) {
        final DespesasAdmDiretasFragment fragment =
                (DespesasAdmDiretasFragment) navHost.
                        getChildFragmentManager().getFragments().get(0);
        callback.despesaDiretaFragmentAttached(fragment);
    }

    private void getDemaisFragmentsAoAtachar(final DespesasFragmentsCallback callback) {
        navHost.getChildFragmentManager()
                .addFragmentOnAttachListener(
                        (fragmentManager, fragment) -> {
                            getFragmentDespesaDiretaOnAttach(callback, fragment);
                            getFragmentDespesaIndiretaOnAttach(callback, fragment);
                        });
    }

    private void getFragmentDespesaIndiretaOnAttach(DespesasFragmentsCallback callback, Fragment fragment) {
        if(fragment instanceof DespesasAdmIndiretasFragment){
            callback.despesaIndiretaFragmentAttached((DespesasAdmIndiretasFragment) fragment);
        }
    }

    private void getFragmentDespesaDiretaOnAttach(DespesasFragmentsCallback callback, Fragment fragment) {
        if(fragment instanceof DespesasAdmDiretasFragment){
            callback.despesaDiretaFragmentAttached((DespesasAdmDiretasFragment) fragment);
        }
    }

}

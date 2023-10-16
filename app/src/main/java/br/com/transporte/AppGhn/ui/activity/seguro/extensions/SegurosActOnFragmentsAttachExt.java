package br.com.transporte.AppGhn.ui.activity.seguro.extensions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.SeguroFrotaFragment;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroHome.SegurosHomeFragment;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroInfo.SegurosInformacoesGeraisFragment;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.SeguroVidaFragment;

public class SegurosActOnFragmentsAttachExt {
    private final NavHostFragment navHost;

    public SegurosActOnFragmentsAttachExt(@NonNull NavHostFragment navHost) {
        this.navHost = navHost;
    }

    public interface SegurosFragmentsCallback {
        void seguroHomeFragmentAttached(SegurosHomeFragment fragment);
        void seguroFrotaFragmentAttached(SeguroFrotaFragment fragment);
        void seguroVidaFragmentAttached(SeguroVidaFragment fragment);
        void seguroResumoFragmentAttached(SegurosInformacoesGeraisFragment fragment);
    }

    //----------------------------------------------------------------------------------------------

    public void getFragmentsWhenAttach(final SegurosFragmentsCallback callback) {
        //getFragmentInicialDoNavGraph(callback); //todo -> ainda nao necessario
        getDemaisFragmentsAoAtachar(callback);
    }

    /*private void getFragmentInicialDoNavGraph(@NonNull final SegurosFragmentsCallback callback) {
        final SegurosHomeFragment fragment =
                (SegurosHomeFragment) navHost.
                        getChildFragmentManager().getFragments().get(0);
        callback.seguroHomeFragmentAttached(fragment);
    }*/

    private void getDemaisFragmentsAoAtachar(final SegurosFragmentsCallback callback) {
        navHost.getChildFragmentManager()
                .addFragmentOnAttachListener(
                        (fragmentManager, fragment) -> {
                            getFragmentSeguroFrotaOnAttach(callback, fragment);
                            getFragmentSeguroVidaOnAttach(callback, fragment);
                            getFragmentSeguroResumoOnAttach(callback, fragment);
                        });
    }

    private void getFragmentSeguroFrotaOnAttach(
            final SegurosFragmentsCallback callback,
            final Fragment fragment
    ) {
        if (fragment instanceof SeguroFrotaFragment) {
            callback.seguroFrotaFragmentAttached((SeguroFrotaFragment) fragment);
        }
    }

    private void getFragmentSeguroVidaOnAttach(
            final SegurosFragmentsCallback callback,
            final Fragment fragment
    ) {
        if (fragment instanceof SeguroVidaFragment) {
            callback.seguroVidaFragmentAttached((SeguroVidaFragment) fragment);
        }
    }

    private void getFragmentSeguroResumoOnAttach(
            final SegurosFragmentsCallback callback,
            final Fragment fragment
    ) {
        if (fragment instanceof SegurosInformacoesGeraisFragment) {
            callback.seguroResumoFragmentAttached((SegurosInformacoesGeraisFragment) fragment);
        }
    }

}

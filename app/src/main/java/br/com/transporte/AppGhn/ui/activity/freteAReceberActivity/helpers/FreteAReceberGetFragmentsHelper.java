package br.com.transporte.AppGhn.ui.activity.freteAReceberActivity.helpers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.CarregandoDadosFreteReceberFragment;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberPagosFragment;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberResumoFragment;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.freteEmAberto.FreteAReceberEmAbertoFragment;

public class FreteAReceberGetFragmentsHelper {
    private final FragmentManager fragmentManager;

    public FreteAReceberGetFragmentsHelper(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public interface GetFragmentsCallback {
        void fragment_carregaDados(CarregandoDadosFreteReceberFragment fragment);

        void fragment_FreteEmAberto(FreteAReceberEmAbertoFragment fragment);

        void fragment_FretePago(FreteAReceberPagosFragment fragment);

        void fragment_resumo(FreteAReceberResumoFragment fragment);

    }

    //----------------------------------------------------------------------------------------------

    public void run(final GetFragmentsCallback callback) {
        NavHostFragment navHost = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_container_frete_receber);
        if (navHost != null) {
            getFragmentDeAbertura(navHost, callback);
            getDemaisFragments(navHost, callback);
        }
    }

    //----------------------------------------------------------------------------------------------

    private void getFragmentDeAbertura(
            final NavHostFragment navHost,
            @NonNull final GetFragmentsCallback callback
    ) {
        final CarregandoDadosFreteReceberFragment fragCarregaDados =
                (CarregandoDadosFreteReceberFragment)
                        navHost.getChildFragmentManager()
                        .getFragments().get(0);
        callback.fragment_carregaDados(fragCarregaDados);
    }

    //----------------------------------------------------------------------------------------------

    private void getDemaisFragments(
            @NonNull final NavHostFragment navHost,
            final GetFragmentsCallback callback
    ) {
        navHost.getChildFragmentManager().addFragmentOnAttachListener(
                (fragmentManager, fragment) -> {
                    getFragment_freteAReceberEmAberto(fragment, callback);
                    getFragment_freteAReceberJaPago(fragment, callback);
                    getFragment_freteAReceberResumo(fragment, callback);
                });
    }

    private void getFragment_freteAReceberEmAberto(
            final Fragment fragment,
            final GetFragmentsCallback callback
    ) {
        if (fragment instanceof FreteAReceberEmAbertoFragment) {
            final FreteAReceberEmAbertoFragment fragEmAberto = (FreteAReceberEmAbertoFragment) fragment;
            callback.fragment_FreteEmAberto(fragEmAberto);
        }

    }

    private void getFragment_freteAReceberJaPago(
            final Fragment fragment,
            final GetFragmentsCallback callback
    ) {
        if (fragment instanceof FreteAReceberPagosFragment) {
            final FreteAReceberPagosFragment fragPagos = (FreteAReceberPagosFragment) fragment;
            callback.fragment_FretePago(fragPagos);
        }
    }

    private void getFragment_freteAReceberResumo(
            final Fragment fragment,
            final GetFragmentsCallback callback
    ) {
        if (fragment instanceof FreteAReceberResumoFragment) {
            final FreteAReceberResumoFragment fragResumo = (FreteAReceberResumoFragment) fragment;
            callback.fragment_resumo(fragResumo);
        }
    }

}

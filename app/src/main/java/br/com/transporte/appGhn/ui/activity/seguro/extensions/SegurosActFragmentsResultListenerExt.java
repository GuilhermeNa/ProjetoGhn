package br.com.transporte.appGhn.ui.activity.seguro.extensions;

import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.FRAGMENTS_LISTENER;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.ON_SEARCH_CLEAR;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.ON_SEARCH_CLICK;

import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;

public class SegurosActFragmentsResultListenerExt {
    private final NavHostFragment navHost;
    private final LifecycleOwner lifecycleOwner;
    private SegurosActFragmentsResultListener callback;

    public SegurosActFragmentsResultListenerExt(
            NavHostFragment navHost,
            LifecycleOwner lifecycleOwner
    ) {
        this.navHost = navHost;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface SegurosActFragmentsResultListener {
        void onMenuProviderSearchViewClick();

        void onMenuProviderSearchViewClear();
    }

    public void getResult(final SegurosActFragmentsResultListener callback) {
        navHost.getChildFragmentManager().setFragmentResultListener(
                FRAGMENTS_LISTENER, lifecycleOwner,
                (requestKey, result) -> {
                    if (result.containsKey(ON_SEARCH_CLICK)) {
                        callback.onMenuProviderSearchViewClick();
                    } else if (result.containsKey(ON_SEARCH_CLEAR)) {
                        callback.onMenuProviderSearchViewClear();
                    }
                });
    }

}

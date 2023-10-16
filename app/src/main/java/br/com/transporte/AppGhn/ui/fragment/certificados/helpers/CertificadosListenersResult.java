package br.com.transporte.AppGhn.ui.fragment.certificados.helpers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class CertificadosListenersResult {

    public static void notificaSearchClicked(
            final String REQUEST_KEY,
            final String BUNDLE_KEY,
            @NonNull final FragmentManager fragmentManager,
            boolean isClicked
    ){
        final Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_KEY, isClicked);
        fragmentManager.setFragmentResult(REQUEST_KEY, bundle);
    }

}


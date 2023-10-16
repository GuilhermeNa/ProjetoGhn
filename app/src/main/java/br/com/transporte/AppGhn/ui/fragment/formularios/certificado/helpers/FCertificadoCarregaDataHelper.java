package br.com.transporte.AppGhn.ui.fragment.formularios.certificado.helpers;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.AppGhn.ui.fragment.formularios.certificado.viewmodel.FormularioCertificadoViewModel;

public class FCertificadoCarregaDataHelper {
    private final LifecycleOwner lifeCycleOwner;

    public FCertificadoCarregaDataHelper(LifecycleOwner lifeCycleOwner) {
        this.lifeCycleOwner = lifeCycleOwner;
    }

    public interface FCertCarregaCarregaDataCallback {
        void listaDePlacasCarregada();
        void outro();
    }

    //----------------------------------------------------------------------------------------------

    public void run(
            final FormularioCertificadoViewModel viewModel,
            final FCertCarregaCarregaDataCallback callback
    ) {
       carregaPlacas(viewModel, callback);


    }

    private void carregaPlacas(
            @NonNull final FormularioCertificadoViewModel viewModel,
            final FCertCarregaCarregaDataCallback callback
    ) {

    }




}

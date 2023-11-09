package br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.exibeParcelasDialog.domain;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.repository.ParcelaSeguroVidaRepository;

public class EditaParcelaSeguroVidaUseCase {
    private final ParcelaSeguroVidaRepository vidaRepository;
    private final LifecycleOwner lifecycleOwner;

    public EditaParcelaSeguroVidaUseCase(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.vidaRepository = new ParcelaSeguroVidaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface EditaParcelaVidaUseCaseCallback {
        void quandoFinaliza();
    }

    //----------------------------------------------------------------------------------------------

    public void editaParcela(
            final Parcela_seguroVida parcela,
            final EditaParcelaVidaUseCaseCallback callback
    ) {
        vidaRepository.edita(parcela).observe(lifecycleOwner,
                ignore -> callback.quandoFinaliza());
    }

}

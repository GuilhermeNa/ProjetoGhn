package br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.editaParcelaDialog.domain;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.repository.ParcelaSeguroVidaRepository;

public class EditaParcelaVidaUseCase {
    private final ParcelaSeguroVidaRepository vidaRepository;
    private final LifecycleOwner lifecycleOwner;

    public EditaParcelaVidaUseCase(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.vidaRepository = new ParcelaSeguroVidaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface EditaParcelaFrotaUseCaseCallback {
        void quandoFinaliza();
    }

    //----------------------------------------------------------------------------------------------

    public void editaParcela(
            final Parcela_seguroVida parcela,
            final EditaParcelaFrotaUseCaseCallback callback
    ) {
        vidaRepository.edita(parcela).observe(lifecycleOwner,
                ignore -> callback.quandoFinaliza());
    }

}

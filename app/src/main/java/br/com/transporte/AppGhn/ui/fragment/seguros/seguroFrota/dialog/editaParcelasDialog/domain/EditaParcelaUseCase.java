package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.dialog.editaParcelasDialog.domain;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.repository.ParcelaSeguroFrotaRepository;

public class EditaParcelaUseCase {
    private final ParcelaSeguroFrotaRepository frotaRepository;
    private final LifecycleOwner lifecycleOwner;

    public EditaParcelaUseCase(
           Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.frotaRepository = new ParcelaSeguroFrotaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface EditaParcelaFrotaUseCaseCallback {
        void quandoFinaliza();
    }

    //----------------------------------------------------------------------------------------------

    public void editaParcela(
            final Parcela_seguroFrota parcela,
            final EditaParcelaFrotaUseCaseCallback callback
    ) {
        frotaRepository.edita(parcela).observe(lifecycleOwner,
                ignore -> {
                    callback.quandoFinaliza();
                });
    }

}

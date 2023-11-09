package br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.dialog.exibeParcelasDialog.domain;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.repository.ParcelaSeguroFrotaRepository;

public class PacelasFrotaDataUseCase {
    private final ParcelaSeguroFrotaRepository repository;
    private final LifecycleOwner lifecycleOwner;

    public PacelasFrotaDataUseCase(Context context, LifecycleOwner lifecycleOwner) {
        this.repository = new ParcelaSeguroFrotaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public void atualizaDataSet(final Long idSeguro) {
        repository.buscaPorSeguroId(idSeguro);
    }

    public interface ParcelaFrotaUseCaseCallback {
        void quandoFinaliza(List<Parcela_seguroFrota> parcelas);
    }

    //----------------------------------------------------------------------------------------------

    public void getParcelasRelacionadasAEsteSeguroId(
            final Long seguroId,
            final ParcelaFrotaUseCaseCallback callback
    ) {
        repository.buscaPorSeguroId(seguroId).observe(lifecycleOwner,
               parcelasDoSeguro -> {
                    if(parcelasDoSeguro != null){
                        callback.quandoFinaliza(parcelasDoSeguro);
                    }
                });
    }

}

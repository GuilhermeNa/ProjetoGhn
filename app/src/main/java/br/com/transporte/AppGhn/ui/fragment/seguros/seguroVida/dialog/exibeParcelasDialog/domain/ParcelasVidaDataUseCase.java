package br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.dialog.exibeParcelasDialog.domain;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.repository.ParcelaSeguroVidaRepository;

public class ParcelasVidaDataUseCase {
    private final ParcelaSeguroVidaRepository repository;
    private final LifecycleOwner lifecycleOwner;

    public ParcelasVidaDataUseCase(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.repository = new ParcelaSeguroVidaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public void atualizaDataSet(final Long idSeguro) {
        repository.buscaPorSeguroId(idSeguro);
    }

    public interface ParcelaVidaUseCaseCallback {
        void quandoFinaliza(List<Parcela_seguroVida> parcelas);
    }

    //----------------------------------------------------------------------------------------------

    public void getParcelasRelacionadasAEsteSeguroId(
            final Long seguroId,
            final ParcelaVidaUseCaseCallback callback
    ) {
        repository.buscaPorSeguroId(seguroId).observe(lifecycleOwner,
                parcelasDoSeguro -> {
                    if (parcelasDoSeguro != null) {
                        callback.quandoFinaliza(parcelasDoSeguro);
                    }
                });
    }

}

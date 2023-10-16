package br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.repository.SeguroVidaRepository;

public class BuscaDataSeguroVidaUseCase {
    private final SeguroVidaRepository seguroVidaRepository;
    private final LifecycleOwner lifecycleOwner;

    public BuscaDataSeguroVidaUseCase(
           Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.seguroVidaRepository = new SeguroVidaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface SeguroVidaUseCaseCallback {
        void quandoFinaliza(List<DespesaComSeguroDeVida> dataSet);
    }

//--------------------------------------------------------------------------------------------------

    public void buscaDataRelacionadaSeguroVida(@NonNull final SeguroVidaUseCaseCallback callback) {
        seguroVidaRepository.buscaPorStatus(true)
                .observe(lifecycleOwner,
                        callback::quandoFinaliza);
    }


}

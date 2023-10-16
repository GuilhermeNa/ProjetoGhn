package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroFrota.domain;

import androidx.annotation.NonNull;

import br.com.transporte.AppGhn.model.enums.TipoFormulario;

public class FSeguroFrotaPreparaAmbienteUseCase {

    public interface FSeguroFrotaPreparaAmbienteUseCaseCallback {
        void quandoAdicionando();
        void quandoEditando();
        void quandoRenovando();
    }

    //----------------------------------------------------------------------------------------------

    public void run(
            @NonNull final TipoFormulario tipo,
            final FSeguroFrotaPreparaAmbienteUseCaseCallback callback
    ){
        switch (tipo){
            case ADICIONANDO:
                callback.quandoAdicionando();
                break;

            case EDITANDO:
                callback.quandoEditando();
                break;

            case RENOVANDO:
                callback.quandoRenovando();
                break;
        }
    }

}

package br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.extensions;

import android.os.Bundle;

import androidx.annotation.NonNull;

import br.com.transporte.appGhn.ui.fragment.seguros.TipoDeSeguro;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroInfo.SegurosInformacoesGeraisFragmentArgs;

public class CarregaArgumentsExt {
    private final Bundle arguments;

    public CarregaArgumentsExt(Bundle arguments) {
        this.arguments = arguments;
    }

    public interface CarregaArgumentsSeguroInfoCallback {
        void carregaId(Long id);

        void carregaTipo(TipoDeSeguro tipoDeSeguro);
    }


    //----------------------------------------------------------------------------------------------
    public void tentaCarregarArguments(final CarregaArgumentsSeguroInfoCallback callback) {
        carregaId(callback);
        carregaTipoDeSeguro(callback);
    }

    private void carregaTipoDeSeguro(@NonNull CarregaArgumentsSeguroInfoCallback callback) {
        final TipoDeSeguro tipoDeSeguroRecebido = SegurosInformacoesGeraisFragmentArgs
                .fromBundle(arguments)
                .getTipoSeguro();
        callback.carregaTipo(tipoDeSeguroRecebido);
    }

    private void carregaId(@NonNull CarregaArgumentsSeguroInfoCallback callback) {
        long seguroId = SegurosInformacoesGeraisFragmentArgs
                .fromBundle(arguments)
                .getSeguroId();
        callback.carregaId(seguroId);
    }


}

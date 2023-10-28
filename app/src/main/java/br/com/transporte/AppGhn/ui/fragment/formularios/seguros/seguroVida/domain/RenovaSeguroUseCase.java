package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.domain;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.repository.SeguroVidaRepository;

public class RenovaSeguroUseCase {
    private final DespesaComSeguro seguroNovo;
    private final DespesaComSeguro seguroSendoRenovado;

    public RenovaSeguroUseCase(DespesaComSeguroDeVida seguroArmazenado, DespesaComSeguroDeVida seguroRenovado) {
        this.seguroNovo = seguroArmazenado;
        this.seguroSendoRenovado = seguroRenovado;
    }

    //----------------------------------------------------------------------------------------------
    public LiveData<Long> renovaSeguroVida(
            @NonNull final SeguroVidaRepository repository
    ) {
        defineSeguroQueEstaSendoRenovadoComoInativo();
        repository.edita((DespesaComSeguroDeVida) seguroSendoRenovado);
        return repository.adiciona((DespesaComSeguroDeVida) seguroNovo);
    }

    private void defineSeguroQueEstaSendoRenovadoComoInativo() {
        seguroSendoRenovado.setValido(false);
    }


}

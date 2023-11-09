package br.com.transporte.appGhn.ui.fragment.formularios.seguros.seguroVida.domain;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;

import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;

public class ConfiguraSeguroNaPrimeiraInsercaoUseCase {

    public void configuraSeguroVida(@NonNull final DespesaComSeguroDeVida seguroInserido){
        seguroInserido.setTipoDespesa(INDIRETA);
        seguroInserido.setValido(true);
        seguroInserido.setRefCavaloId(0L);
    }

}

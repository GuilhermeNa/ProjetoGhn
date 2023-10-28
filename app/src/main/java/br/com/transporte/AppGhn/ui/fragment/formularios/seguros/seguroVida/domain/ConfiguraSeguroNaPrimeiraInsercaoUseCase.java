package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.domain;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;

import androidx.annotation.NonNull;

import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;

public class ConfiguraSeguroNaPrimeiraInsercaoUseCase {

    public void configuraSeguroVida(@NonNull final DespesaComSeguroDeVida seguroInserido){
        seguroInserido.setTipoDespesa(INDIRETA);
        seguroInserido.setValido(true);
        seguroInserido.setRefCavaloId(0L);
    }

}

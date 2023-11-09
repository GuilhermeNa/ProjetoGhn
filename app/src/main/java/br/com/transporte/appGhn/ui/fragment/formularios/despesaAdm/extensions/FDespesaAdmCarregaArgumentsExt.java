package br.com.transporte.appGhn.ui.fragment.formularios.despesaAdm.extensions;

import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;

import android.os.Bundle;

import br.com.transporte.appGhn.model.enums.TipoDespesa;

public class FDespesaAdmCarregaArgumentsExt {
    private final Bundle bundle;

    public FDespesaAdmCarregaArgumentsExt(Bundle bundle) {
        this.bundle = bundle;
    }

    //---------------------------------------------------------------------------------------------

    public Long tentaCarregarDespesaId() {
        return (Long) bundle.getLong(CHAVE_ID, 0L);
    }

    public TipoDespesa getTipoDespesa(){
        return (TipoDespesa) bundle.getSerializable(CHAVE_TIPO_DESPESA);
    }

}

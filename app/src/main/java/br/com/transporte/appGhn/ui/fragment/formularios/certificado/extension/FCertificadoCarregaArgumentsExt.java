package br.com.transporte.appGhn.ui.fragment.formularios.certificado.extension;

import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;

import android.os.Bundle;

import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.model.enums.TipoFormulario;

public class FCertificadoCarregaArgumentsExt {
    private final Bundle bundle;

    public FCertificadoCarregaArgumentsExt(Bundle bundle) {
        this.bundle = bundle;
    }

    //-----------------------------------------------------------------------------------

    public TipoDespesa getTipoDespesa() {
        return (TipoDespesa) bundle.getSerializable(CHAVE_DESPESA);
    }

    public TipoFormulario getTipoRequisicao() {
        return (TipoFormulario) bundle.getSerializable(CHAVE_REQUISICAO);
    }

    public Long tentaCarregarCertificadoId(){
        return bundle.getLong(CHAVE_ID, 0L);
    }

}

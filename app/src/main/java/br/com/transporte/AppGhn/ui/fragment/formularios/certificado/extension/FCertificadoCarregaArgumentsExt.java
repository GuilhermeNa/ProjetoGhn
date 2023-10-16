package br.com.transporte.AppGhn.ui.fragment.formularios.certificado.extension;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.CHAVE_DESPESA;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_TIPO_DESPESA;

import android.os.Bundle;

import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.model.enums.TipoFormulario;

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

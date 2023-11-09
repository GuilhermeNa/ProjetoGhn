package br.com.transporte.appGhn.ui.fragment.formularios.seguros;

import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;

import android.os.Bundle;

import androidx.annotation.Nullable;

import br.com.transporte.appGhn.model.enums.TipoFormulario;

public class CarregaArgumentsSegurosExt {

    public static long carregaId(@Nullable final Bundle arguments) {
        if (arguments != null) {
            return arguments.getLong(CHAVE_ID, 0L);
        } else {
            return 0L;
        }
    }

    /**
     * @noinspection DataFlowIssue
     */
    public static TipoFormulario carregaTipoDeFormulario(@Nullable final Bundle arguments) {
        return (TipoFormulario) arguments.getSerializable(CHAVE_REQUISICAO);
    }

}

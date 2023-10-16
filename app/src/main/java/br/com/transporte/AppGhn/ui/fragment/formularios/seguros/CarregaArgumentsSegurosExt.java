package br.com.transporte.AppGhn.ui.fragment.formularios.seguros;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_REQUISICAO;

import android.os.Bundle;

import androidx.annotation.Nullable;

import br.com.transporte.AppGhn.model.enums.TipoFormulario;

public class CarregaArgumentsSegurosExt {

    public static long carregaId(@Nullable final Bundle arguments) {
        return arguments.getLong(CHAVE_ID, 0L);

    }

    public static TipoFormulario carregaTipoDeFormulario(@Nullable final Bundle arguments) {
       return (TipoFormulario) arguments.getSerializable(CHAVE_REQUISICAO);
    }

}

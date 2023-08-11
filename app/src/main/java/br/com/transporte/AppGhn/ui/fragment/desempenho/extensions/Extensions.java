package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import static br.com.transporte.AppGhn.ui.fragment.desempenho.dialog.BottomDialogDesempenho.CHAVE_TIPO;
import static br.com.transporte.AppGhn.ui.fragment.desempenho.dialog.BottomDialogDesempenho.SELECIONA_TIPO;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;

public class Extensions {

    public static void setResult(TipoDeRequisicao tipoDeRequisicao, Bundle bundle, FragmentManager parentFragmentManager) {
        bundle.putSerializable(CHAVE_TIPO, tipoDeRequisicao);
        parentFragmentManager.setFragmentResult(SELECIONA_TIPO, bundle);
    }

}

package br.com.transporte.AppGhn.ui.fragment.manutencao.extensions;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;

public class FragmentManutencaoBindAdapterExtension {

public interface FragmentManutencaoBindAdapterCallback {
    void incompletoParaBind();

    void fazBind();
}

    public void run(
            final List<CustosDeManutencao> dataSet,
            final FragmentManutencaoBindAdapterCallback callback
            ) {
        if (dataSet == null) {
            callback.incompletoParaBind();
        } else {
            verificaSeAListaContemItens(dataSet, callback);
        }
    }

    private void verificaSeAListaContemItens(
            @NonNull final List<CustosDeManutencao> dataSet,
            final FragmentManutencaoBindAdapterCallback callback
    ) {
        if (dataSet.isEmpty()) {
            callback.incompletoParaBind();
        } else {
            callback.fazBind();
        }
    }

}

package br.com.transporte.AppGhn.ui.fragment.home.frota.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.filtros.FiltraReboque;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.fragment.home.frota.FrotaFragment;

public class CavaloAdapterInnerAdapterHelper {

    private final FrotaFragment context;
    private InterfaceCavaloInnerAdapter callbackCavaloInnerAdapter;
    private Cavalo cavalo;
    private SemiReboqueAdapter adapter;
    private final List<SemiReboque> copiaDataSet_reboque;

    public CavaloAdapterInnerAdapterHelper(FrotaFragment context, List<SemiReboque> copiaDataSet_reboque) {

        this.context = context;
        this.copiaDataSet_reboque = copiaDataSet_reboque;
    }

    public void setCallbackCavaloInnerAdapter(InterfaceCavaloInnerAdapter callbackCavaloInnerAdapter) {
        this.callbackCavaloInnerAdapter = callbackCavaloInnerAdapter;
    }

    public interface InterfaceCavaloInnerAdapter {
        // Id do semireboque que foi clicado para realizar alterações
        // será enviado de volta ao Fragment para abrir Formulario modo Edição
        void solicitaAlteracao_clickEmEditarReboque(Long reboqueId, Long cavaloId);

        // Id do semireboque que teve seu cavalo alterado,
        // será enviado :
        // -> Inner Adapter para aplicar comportamento de remoção da Recycler
        // -> Adapter Pai para atualizar o novo cavalo que está recebendo o reboque
        void solicitaAlteracao_mudaReferenciaDeCavalo(Long reboqueId);
    }

    //----------------------------------------------------------------------------------------------

    public void atualizaAdapter(List<SemiReboque> copiaDataSet_reboque){
        List<SemiReboque> listaSrPorCavalo = FiltraReboque.listaPorCavaloId(copiaDataSet_reboque, cavalo.getId());
        adapter.atualiza(listaSrPorCavalo);
    }

    //----------------------------------------------------------------------------------------------
    //                                       Configura Recycler                                   ||
    //----------------------------------------------------------------------------------------------

    public void configuraRecycler(@NonNull CavaloAdapter.ViewHolder holder, @NonNull Cavalo cavalo) {
        this.cavalo = cavalo;
        configuraAdapter(holder);
        configuraItemDecoration(holder);
    }

    private void configuraAdapter(@NonNull CavaloAdapter.ViewHolder holder) {
        List<SemiReboque> semiReboques = FiltraReboque.listaPorCavaloId(copiaDataSet_reboque, cavalo.getId());
        adapter = new SemiReboqueAdapter(context.getContext(), semiReboques);
        holder.recyclerFilha.setAdapter(adapter);
        adapter.setOnItemClickListener(new SemiReboqueAdapter.OnItemClickListener() {
            @Override
            public void onEditaSrClick(Long reboqueId) {
                callbackCavaloInnerAdapter.solicitaAlteracao_clickEmEditarReboque(reboqueId, cavalo.getId());
            }

            @Override
            public void onAlteraReferenciaDeCavalo(Long reboqueAlteradoId) {
                callbackCavaloInnerAdapter.solicitaAlteracao_mudaReferenciaDeCavalo(reboqueAlteradoId);
            }
        });
    }

    private void configuraItemDecoration(@NonNull CavaloAdapter.ViewHolder holder) {
        Drawable divider = ContextCompat.getDrawable(context.requireContext(), R.drawable.divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context.requireContext(), DividerItemDecoration.VERTICAL);
        if (divider != null) itemDecoration.setDrawable(divider);
        holder.recyclerFilha.addItemDecoration(itemDecoration);
    }

}

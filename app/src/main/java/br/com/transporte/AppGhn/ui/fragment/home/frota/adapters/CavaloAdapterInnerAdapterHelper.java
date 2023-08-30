package br.com.transporte.AppGhn.ui.fragment.home.frota.adapters;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.fragment.home.frota.FrotaFragment;

public class CavaloAdapterInnerAdapterHelper implements SemiReboqueAdapter.OnItemClickListener{

    private final FrotaFragment context;
    private InterfaceCavaloInnerAdapter callbackCavaloInnerAdapter;
    private Cavalo cavalo;
    private SemiReboqueAdapter adapter;
    private RoomSemiReboqueDao reboqueDao;

    public void setCallbackCavaloInnerAdapter(InterfaceCavaloInnerAdapter callbackCavaloInnerAdapter) {
        this.callbackCavaloInnerAdapter = callbackCavaloInnerAdapter;
    }

    public CavaloAdapterInnerAdapterHelper(FrotaFragment context) {
        this.context = context;
        reboqueDao = GhnDataBase.getInstance(context.requireContext()).getRoomReboqueDao();
    }

    //----------------------------------------------------------------------------------------------
    //                                       Configura Recycler                                   ||
    //----------------------------------------------------------------------------------------------

    public void configuraRecycler(@NonNull CavaloAdapter.ViewHolder holder, @NonNull Cavalo cavalo) {
        this.cavalo = cavalo;
        List<SemiReboque> listaSrPorCavalo = reboqueDao.listaPorCavaloId(cavalo.getId());
        configuraAdapter(holder, listaSrPorCavalo);
        configuraItemDecoration(holder);
    }

    private void configuraAdapter(@NonNull CavaloAdapter.ViewHolder holder, List<SemiReboque> listaSrPorCavalo) {
        adapter = new SemiReboqueAdapter(context.getContext(), listaSrPorCavalo);
        holder.recyclerFilha.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void configuraItemDecoration(@NonNull CavaloAdapter.ViewHolder holder) {
        Drawable divider = ContextCompat.getDrawable(context.requireContext(), R.drawable.divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context.requireContext(), DividerItemDecoration.VERTICAL);
        if (divider != null) itemDecoration.setDrawable(divider);
        holder.recyclerFilha.addItemDecoration(itemDecoration);
    }

    //---------------------------------------- Metodos publicos ------------------------------------

    public void atualizaAdapter(){
        adapter.atualiza();
    }



    //----------------------------------------------------------------------------------------------
    //                                 Callback -> SemiReboqueAdapter                             ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onEditaSrClick(int reboqueId) {
        callbackCavaloInnerAdapter.solicitaAlteracao_clickEmEditarReboque(reboqueId, cavalo.getId());
    }

    @Override
    public void onAlteraReferenciaDeCavalo(int reboqueAlteradoId) {
        callbackCavaloInnerAdapter.solicitaAlteracao_mudaReferenciaDeCavalo(reboqueAlteradoId);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface InterfaceCavaloInnerAdapter {
        // Id do semireboque que foi clicado para realizar alterações
        // será enviado de volta ao Fragment para abrir Formulario modo Edição
        void solicitaAlteracao_clickEmEditarReboque(int reboqueId, int cavaloId);

        // Id do semireboque que teve seu cavalo alterado,
        // será enviado :
        // -> Inner Adapter para aplicar comportamento de remoção da Recycler
        // -> Adapter Pai para atualizar o novo cavalo que está recebendo o reboque
        void solicitaAlteracao_mudaReferenciaDeCavalo(int reboqueId);
    }


}

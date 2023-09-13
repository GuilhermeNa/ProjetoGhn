package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.comissoesDetalhesHelpers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.DetalhesFreteAdapter;

public class ComissoesRecyclerFreteHelper {

    private final Context context;
    private CallbackFrete callbackFrete;
    private DetalhesFreteAdapter adapter;

    public void setCallbackReembolso(CallbackFrete callbackFrete) {
        this.callbackFrete = callbackFrete;
    }

    public ComissoesRecyclerFreteHelper(Context context) {
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Build                                             ||
    //----------------------------------------------------------------------------------------------

    public void build(RecyclerView recycler, List<Frete> copiaDataSet) {
        configuraAdapter(recycler, copiaDataSet);
        configuraLayoutManager(recycler);
        adapter.setOnLongClickListener(t -> callbackFrete.onLongClick());
    }

    private void configuraLayoutManager(@NonNull RecyclerView recycler) {
        LinearLayoutManager layoutManagerHorizontal1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManagerHorizontal1);
    }

    private void configuraAdapter(@NonNull RecyclerView recycler, List<Frete> copiaDataSet) {
        adapter = new DetalhesFreteAdapter(context, copiaDataSet);
        recycler.setAdapter(adapter);
    }

    //----------------------------------- Metodos Publicos -----------------------------------------

    public void solicitaAtualizacao(List<Frete> copiaDataSetFrete) {
        adapter.atualiza(copiaDataSetFrete);
    }

    public int solicitaPosicao() {
        return adapter.getPosicao();
    }

    public void solicitaAtualizacaoDoItem(int posicaoTemporaria) {
        adapter.atualizaItem(posicaoTemporaria);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Callback                                          ||
    //----------------------------------------------------------------------------------------------

    public interface CallbackFrete {
        void onLongClick();
    }

}

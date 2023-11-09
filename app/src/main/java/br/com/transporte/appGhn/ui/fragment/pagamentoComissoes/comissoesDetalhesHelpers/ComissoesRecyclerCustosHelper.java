package br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesDetalhesHelpers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.ui.adapter.DetalhesReembolsoAdapter;

public class ComissoesRecyclerCustosHelper {

    private final Context context;
    private CallbackReembolso callbackReembolso;
    private DetalhesReembolsoAdapter adapter;

    public void setCallbackReembolso(CallbackReembolso callbackReembolso) {
        this.callbackReembolso = callbackReembolso;
    }

    public ComissoesRecyclerCustosHelper(Context context) {
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Build                                             ||
    //----------------------------------------------------------------------------------------------

    public void build(@NonNull RecyclerView recycler, List<CustosDePercurso> copiaDataSet) {
        configuraAdapter(recycler, copiaDataSet);
        configuraLayoutManager(recycler);
        adapter.setOnItemClickListener(custoId -> callbackReembolso.onClickListener(custoId));
    }

    private void configuraLayoutManager(@NonNull RecyclerView recycler) {
        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManagerHorizontal);
    }

    private void configuraAdapter(@NonNull RecyclerView recycler, List<CustosDePercurso> copiaDataSet) {
        adapter = new DetalhesReembolsoAdapter(context, copiaDataSet);
        recycler.setAdapter(adapter);
    }

    public void solicitaAtualizacao(List<CustosDePercurso> copiaDataSetCustos) {
       adapter.atualiza(copiaDataSetCustos);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Callback                                          ||
    //----------------------------------------------------------------------------------------------

    public interface CallbackReembolso {
        void onClickListener(Long custoId);
    }

}

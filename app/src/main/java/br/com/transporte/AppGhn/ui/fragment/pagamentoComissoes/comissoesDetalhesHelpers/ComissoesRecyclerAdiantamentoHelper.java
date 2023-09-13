package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.comissoesDetalhesHelpers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.ui.adapter.DetalhesAdiantamentoAdapter;

public class ComissoesRecyclerAdiantamentoHelper {

    private final Context context;
    public CallbackAdiantamento callbackAdiantamento;
    private DetalhesAdiantamentoAdapter adapter;

    public void setCallbackAdiantamento(CallbackAdiantamento callbackAdiantamento) {
        this.callbackAdiantamento = callbackAdiantamento;
    }

    public ComissoesRecyclerAdiantamentoHelper(Context context) {
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Build                                             ||
    //----------------------------------------------------------------------------------------------

    public void build(@NonNull RecyclerView recycler, List<Adiantamento> copiaDataSet) {
        Map<Long, BigDecimal> map = configuraMapComValoresDosAdiantamentos(copiaDataSet);
        configuraAdapter(recycler, copiaDataSet, map);
        configuraLayoutManager(recycler);
        configuraListeners(adapter);
    }

    private void configuraListeners(@NonNull DetalhesAdiantamentoAdapter adapter) {
        adapter.setOnItemClickListener(adiantamentoId -> callbackAdiantamento.onClickListener(adiantamentoId));
        adapter.setOnLongClickListener(t -> callbackAdiantamento.onLongClickListener());
    }

    private void configuraLayoutManager(@NonNull RecyclerView recycler) {
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManagerHorizontal);
    }

    private void configuraAdapter(@NonNull RecyclerView recycler, List<Adiantamento> copiaDataSet, Map<Long, BigDecimal> map) {
        adapter = new DetalhesAdiantamentoAdapter(copiaDataSet, context, map);
        recycler.setAdapter(adapter);
    }

    @NonNull
    private Map<Long, BigDecimal> configuraMapComValoresDosAdiantamentos(@NonNull List<Adiantamento> copiaDataSet) {
        Map<Long, BigDecimal> map = new HashMap<>();
        for (Adiantamento a : copiaDataSet) {
            map.put(a.getId(), a.restaReembolsar());
        }
        return map;
    }

    //----------------------------------- Metodos Publicos -----------------------------------------

    public void solicitaAtualizacao(List<Adiantamento> copiaDataSetAdiantamento) {
        adapter.atualiza(copiaDataSetAdiantamento);
    }

    public int solicitaPosicao() {
        return adapter.getPosicao();
    }

    public void solicitaAtualizacaoMap(Map<Long, BigDecimal> map) {
        adapter.atualizaMap(map);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Callback                                          ||
    //----------------------------------------------------------------------------------------------

    public interface CallbackAdiantamento {
        void onClickListener(Long adiantamentoId);

        void onLongClickListener();
    }

}

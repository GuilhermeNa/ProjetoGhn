package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.adapter.listener.OnLongClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.OnItemClickListenerNew;

public class DetalhesAdiantamentoAdapter extends RecyclerView.Adapter<DetalhesAdiantamentoAdapter.ViewHolder> {
    private final List<Adiantamento> dataSet;
    private final Context context;
    private OnItemClickListenerNew onItemClickListener;
    private OnLongClickListener onLongClickListener;
    private Map<Long, BigDecimal> map;
    private int posicao;

    public void setOnItemClickListener(OnItemClickListenerNew onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    public DetalhesAdiantamentoAdapter(List<Adiantamento> lista, Context context, Map<Long, BigDecimal> map) {
        this.dataSet = lista;
        this.context = context;
        this.map = map;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private final TextView dataTxtView, valorTotalTxtView, saldoRestituidoTxtView, emAbertoTxtView, descontarTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_data);
            valorTotalTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_valor);
            saldoRestituidoTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_descontado_valor);
            emAbertoTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_em_aberto_valor);
            descontarTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_descontar_valor);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.editarDesconto, Menu.NONE, "Editar desconto");
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public DetalhesAdiantamentoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_comissoes_detalhes_item_adiantamento, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull DetalhesAdiantamentoAdapter.ViewHolder holder, int position) {
        Adiantamento adiantamento = dataSet.get(position);
        vincula(holder, adiantamento);
        configuraListeners(holder, adiantamento);
    }

    private void configuraListeners(@NonNull ViewHolder holder, Adiantamento adiantamento) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(adiantamento.getId()));
        holder.itemView.setOnLongClickListener(v -> {
            onLongClickListener.onLongClick(ComissoesDetalhesFragment.TipoDeAdapterPressionado.ADIANTAMENTO);
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public void vincula(@NonNull ViewHolder holder, @NonNull Adiantamento adiantamento) {
        BigDecimal valorDescontar = map.get(adiantamento.getId());
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(adiantamento.getData()));
        holder.valorTotalTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.getValorTotal()));
        holder.saldoRestituidoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.getSaldoRestituido()));
        holder.emAbertoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.restaReembolsar()));
        holder.descontarTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorDescontar));
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualizaMap(Map<Long, BigDecimal> map) {
        this.map.clear();
        this.map = map;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Adiantamento> lista){
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public int getPosicao() {
        return posicao;
    }


}

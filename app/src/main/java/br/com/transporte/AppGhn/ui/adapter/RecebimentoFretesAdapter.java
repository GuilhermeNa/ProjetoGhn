package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberResumoFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.OnItemClickListenerNew;

public class RecebimentoFretesAdapter extends RecyclerView.Adapter <RecebimentoFretesAdapter.ViewHolder> {
    private final List<RecebimentoDeFrete> dataSet;
    private final FreteAReceberResumoFragment context;
    private OnItemClickListenerNew onItemClickListener;

    public void setOnItemClickListener(OnItemClickListenerNew onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public RecebimentoFretesAdapter(FreteAReceberResumoFragment context, List<RecebimentoDeFrete> lista) {
        this.dataSet = lista;
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView dataTxtView, valorTxtView, tipoTxtView, descricaoTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_frete_recebimento_data);
            valorTxtView = itemView.findViewById(R.id.rec_item_frete_recebimento_valor);
            tipoTxtView = itemView.findViewById(R.id.rec_item_frete_recebimento_tipo);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_frete_recebimento_descricao);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public RecebimentoFretesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_frete_recebimentos, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull RecebimentoFretesAdapter.ViewHolder holder, int position) {
        RecebimentoDeFrete recebimento = dataSet.get(position);
        vincula(holder, recebimento);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(recebimento.getId()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull RecebimentoDeFrete recebimento){
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(recebimento.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(recebimento.getValor()));
        holder.tipoTxtView.setText(recebimento.getTipoRecebimentoFrete().getDescricao());
        holder.descricaoTxtView.setText(recebimento.getDescricao());
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<RecebimentoDeFrete> lista){
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

}

package br.com.transporte.AppGhn.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberResumoFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class RecebimentoFretesAdapter extends RecyclerView.Adapter <RecebimentoFretesAdapter.ViewHolder> {
    private final List<RecebimentoDeFrete> lista;
    private final FreteAReceberResumoFragment context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public RecebimentoFretesAdapter(FreteAReceberResumoFragment context, List<RecebimentoDeFrete> lista) {
        this.lista = lista;
        this.context = context;
    }

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

    @NonNull
    @Override
    public RecebimentoFretesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_frete_recebimentos, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecebimentoFretesAdapter.ViewHolder holder, int position) {
        RecebimentoDeFrete recebimento = lista.get(position);
        vincula(holder, recebimento);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(recebimento));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<RecebimentoDeFrete> lista){
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, RecebimentoDeFrete recebimento){
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(recebimento.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(recebimento.getValor()));
        holder.tipoTxtView.setText(recebimento.getTipoRecebimentoFrete().getDescricao());
        holder.descricaoTxtView.setText(recebimento.getDescricao());
    }

}

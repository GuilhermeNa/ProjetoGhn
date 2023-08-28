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
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.DespesasAdmIndiretasFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DespesasAdmIndiretasAdapter extends RecyclerView.Adapter <DespesasAdmIndiretasAdapter.ViewHolder>{
    private final DespesasAdmIndiretasFragment context;
    private final List<DespesaAdm> dataSet;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public DespesasAdmIndiretasAdapter(DespesasAdmIndiretasFragment context, List<DespesaAdm> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView placaTxtView, valorTxtView, dataTxtView, descricaoTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_placa);
            valorTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_valor);
            dataTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_data);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_descricao);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public DespesasAdmIndiretasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_despesa_financeira, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull DespesasAdmIndiretasAdapter.ViewHolder holder, int position) {
    DespesaAdm despesa = dataSet.get(position);
    vincula(holder, despesa);
    holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(despesa));

    }

    private void vincula(ViewHolder holder, DespesaAdm despesa) {
        holder.placaTxtView.setVisibility(View.GONE);
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(despesa.getValorDespesa()));
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(despesa.getData()));
        holder.descricaoTxtView.setText(despesa.getDescricao());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<DespesaAdm> lista){
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(DespesaAdm despesaAdm) {
        this.dataSet.add(despesaAdm);
        notifyItemInserted(getItemCount()-1);
    }

    public void remove(DespesaAdm despesaAdm){
        int posicao = -1;
        posicao = this.dataSet.indexOf(despesaAdm);
        this.dataSet.remove(despesaAdm);
        notifyItemRemoved(posicao);
    }

}

package br.com.transporte.appGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.OnItemClickListener_getId;

public class DetalhesReembolsoAdapter extends RecyclerView.Adapter <DetalhesReembolsoAdapter.ViewHolder> {
    private final List<CustosDePercurso> dataSet;
    private final Context context;
    private OnItemClickListener_getId onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public DetalhesReembolsoAdapter(Context context, List<CustosDePercurso> lista) {
        this.dataSet = lista;
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView dataEditText, valorEditText, descricaoEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataEditText = itemView.findViewById(R.id.rec_salario_detalhes_item_reembolso_data);
            valorEditText = itemView.findViewById(R.id.rec_salario_detalhes_item_reembolso_valor);
            descricaoEditText = itemView.findViewById(R.id.rec_salario_detalhes_item_reembolso_descricao);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public DetalhesReembolsoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_comissoes_detalhes_item_reembolso, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull DetalhesReembolsoAdapter.ViewHolder holder, int position) {
        CustosDePercurso custosDePercurso = dataSet.get(position);
        vincula(holder, custosDePercurso);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(custosDePercurso.getId()));
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull CustosDePercurso custosDePercurso){
        holder.dataEditText.setText(ConverteDataUtil.dataParaString(custosDePercurso.getData()));
        holder.valorEditText.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosDePercurso.getValorCusto()));
        holder.descricaoEditText.setText(custosDePercurso.getDescricao());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //-------------------------------------- Metodos publicos --------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<CustosDePercurso> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

}

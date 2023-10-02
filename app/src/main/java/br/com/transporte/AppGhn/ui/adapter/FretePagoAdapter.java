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
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesPagasDetalhesFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class FretePagoAdapter extends RecyclerView.Adapter <FretePagoAdapter.ViewHolder>{
    private final ComissoesPagasDetalhesFragment context;
    private final List<Frete> dataSet;

    public FretePagoAdapter(ComissoesPagasDetalhesFragment context, List<Frete> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualizaData(final List<Frete> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView dataTxtView, comissaoTxtView, origemTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_pagos_fretes_data);
            origemTxtView = itemView.findViewById(R.id.rec_item_pagos_fretes_origem);
            comissaoTxtView = itemView.findViewById(R.id.rec_item_pagos_fretes_comissao);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public FretePagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_pagos_fretes, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull FretePagoAdapter.ViewHolder holder, int position) {
        Frete frete = dataSet.get(position);
        vincula(holder, frete);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull Frete frete) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.comissaoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getComissaoAoMotorista()));
    }
}

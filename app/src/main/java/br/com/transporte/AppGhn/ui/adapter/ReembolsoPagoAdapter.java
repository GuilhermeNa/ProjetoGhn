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
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesPagasDetalhesFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class ReembolsoPagoAdapter extends RecyclerView.Adapter <ReembolsoPagoAdapter.ViewHolder>{
    public static final String VALOR_POSITIVO = "(+) ";
    private final ComissoesPagasDetalhesFragment context;
    private final List<CustosDePercurso> dataSet;

    public ReembolsoPagoAdapter(ComissoesPagasDetalhesFragment context, List<CustosDePercurso> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualizaData(final List<CustosDePercurso> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dataTxtView, valorReembolsoTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_pagos_reembolso_data);
            valorReembolsoTxtView = itemView.findViewById(R.id.rec_item_pagos_reembolso_valor);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ReembolsoPagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_pagos_reembolso, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ReembolsoPagoAdapter.ViewHolder holder, int position) {
        CustosDePercurso custo = dataSet.get(position);
        vincula(holder, custo);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull CustosDePercurso custo) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(custo.getData()));
        String custoFormatado = VALOR_POSITIVO + FormataNumerosUtil.formataMoedaPadraoBr(custo.getValorCusto());
        holder.valorReembolsoTxtView.setText(custoFormatado);
    }

}

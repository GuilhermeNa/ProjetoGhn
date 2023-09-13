package br.com.transporte.AppGhn.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesPagasDetalhesFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class AdiantamentoPagoAdapter extends RecyclerView.Adapter<AdiantamentoPagoAdapter.ViewHolder> {
    private final ComissoesPagasDetalhesFragment context;
    private final List<Adiantamento> dataSet;

    public AdiantamentoPagoAdapter(ComissoesPagasDetalhesFragment context, List<Adiantamento> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxtView, restaPagarTxtView, descontadoTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_pagos_adiantamento_data);
            descontadoTxtView = itemView.findViewById(R.id.rec_item_pagos_adiantamento_descontado);
            restaPagarTxtView = itemView.findViewById(R.id.rec_item_pagos_adiantamento_resta_pagar);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdiantamentoPagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_pagos_adiantamento, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdiantamentoPagoAdapter.ViewHolder holder, int position) {
        Adiantamento adiantamento = dataSet.get(position);
        vincula(holder, adiantamento);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull Adiantamento adiantamento) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(adiantamento.getData()));
        holder.restaPagarTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.restaReembolsar()));

        String descontadoString = "(-) "+ FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.getUltimoValorAbatido());
        holder.descontadoTxtView.setText(descontadoString);
    }

}

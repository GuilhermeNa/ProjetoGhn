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
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesPagasDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class ReembolsoPagoAdapter extends RecyclerView.Adapter <ReembolsoPagoAdapter.ViewHolder>{
    private final ComissoesPagasDetalhesFragment context;
    private final List<CustosDePercurso> lista;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public ReembolsoPagoAdapter(ComissoesPagasDetalhesFragment context, List<CustosDePercurso> lista) {
        this.context = context;
        this.lista = lista;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dataTxtView, valorReemboloTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_pagos_reembolso_data);
            valorReemboloTxtView = itemView.findViewById(R.id.rec_item_pagos_reembolso_valor);
        }
    }

    @NonNull
    @Override
    public ReembolsoPagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_pagos_reembolso, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ReembolsoPagoAdapter.ViewHolder holder, int position) {
        CustosDePercurso custo = lista.get(position);
        vincula(holder, custo);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(custo));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, CustosDePercurso custo) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(custo.getData()));
        holder.valorReemboloTxtView.setText("(+) "+FormataNumerosUtil.formataMoedaPadraoBr(custo.getValorCusto()));
    }


}

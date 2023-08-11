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
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesPagasDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class AdiantamentoPagoAdapter extends RecyclerView.Adapter<AdiantamentoPagoAdapter.ViewHolder> {
    private final ComissoesPagasDetalhesFragment context;
    private final List<Adiantamento> lista;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public AdiantamentoPagoAdapter(ComissoesPagasDetalhesFragment context, List<Adiantamento> lista) {
        this.context = context;
        this.lista = lista;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxtView, restaPagarTxtView, descontadoTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_pagos_adiantamento_data);
            descontadoTxtView = itemView.findViewById(R.id.rec_item_pagos_adiantamento_descontado);
            restaPagarTxtView = itemView.findViewById(R.id.rec_item_pagos_adiantamento_resta_pagar);
        }
    }

    @NonNull
    @Override
    public AdiantamentoPagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_pagos_adiantamento, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdiantamentoPagoAdapter.ViewHolder holder, int position) {
        Adiantamento adiantamento = lista.get(position);
        vincula(holder, adiantamento);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(adiantamento));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, Adiantamento adiantamento) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(adiantamento.getData()));
        holder.descontadoTxtView.setText("(-) "+ FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.getUltimoValorAbatido()));
        holder.restaPagarTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.restaReembolsar()));
    }


}

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
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesPagasDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class FretePagoAdapter extends RecyclerView.Adapter <FretePagoAdapter.ViewHolder>{
    private final ComissoesPagasDetalhesFragment context;
    private final List<Frete> lista;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public FretePagoAdapter(ComissoesPagasDetalhesFragment context, List<Frete> lista) {
        this.context = context;
        this.lista = lista;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView dataTxtView, comissaoTxtView, origemTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_pagos_fretes_data);
            origemTxtView = itemView.findViewById(R.id.rec_item_pagos_fretes_origem);
            comissaoTxtView = itemView.findViewById(R.id.rec_item_pagos_fretes_comissao);
        }
    }

    @NonNull
    @Override
    public FretePagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_pagos_fretes, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull FretePagoAdapter.ViewHolder holder, int position) {
        Frete frete = lista.get(position);
        vincula(holder, frete);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(frete));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, Frete frete) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.comissaoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getComissaoAoMotorista()));
    }
}

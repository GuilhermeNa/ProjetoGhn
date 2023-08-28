package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.temporarios.ObjetoTemporario_representaCavalo;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DetalhesPeriodoAdapter extends RecyclerView.Adapter <DetalhesPeriodoAdapter.ViewHolder>{

    private final Context context;
    private final List<ObjetoTemporario_representaCavalo> dataSet;

    public DetalhesPeriodoAdapter(Context context, List<ObjetoTemporario_representaCavalo> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

     static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView placaTxt, percentualTxt, motoristaTxt, valorTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxt = itemView.findViewById(R.id.rec_detalhes_periodo_placa);
            valorTxt = itemView.findViewById(R.id.rec_detalhes_periodo_valor);
            percentualTxt = itemView.findViewById(R.id.rec_detalhes_periodo_percentual);
            motoristaTxt = itemView.findViewById(R.id.rec_detalhes_periodo_motorista);
        }

    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public DetalhesPeriodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_item_detalhes_periodo, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull DetalhesPeriodoAdapter.ViewHolder holder, int position) {
        ObjetoTemporario_representaCavalo detalhes = dataSet.get(position);
        vincula(holder, detalhes);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //----------------------------------------------
    // -> Vincula                                 ||
    //----------------------------------------------

    private void vincula(@NonNull ViewHolder holder, @NonNull ObjetoTemporario_representaCavalo detalhes) {
        holder.placaTxt.setText(detalhes.getPlaca());
        holder.valorTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(detalhes.getValor()));
        holder.motoristaTxt.setText(detalhes.getNome());

        String percentual = formataPercentual(detalhes.getPercentual());
        holder.percentualTxt.setText(percentual);
    }

    @NonNull
    private String formataPercentual(@NonNull BigDecimal valor) {
        String percentual = valor.toPlainString();
        percentual = percentual.replace(".00", " %");
        return percentual;
    }

    //--------------------------------------- Metodos publicos -------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza (List<ObjetoTemporario_representaCavalo> dataSet){
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

}

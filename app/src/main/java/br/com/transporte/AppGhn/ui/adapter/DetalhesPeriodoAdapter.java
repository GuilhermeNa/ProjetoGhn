package br.com.transporte.AppGhn.ui.adapter;

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
import br.com.transporte.AppGhn.model.temporarios.DetalhesDesempenho;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DetalhesPeriodoAdapter extends RecyclerView.Adapter <DetalhesPeriodoAdapter.ViewHolder>{

    private final Context context;
    private final List<DetalhesDesempenho> dataSet;

    public DetalhesPeriodoAdapter(Context context, List<DetalhesDesempenho> dataSet) {
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
        DetalhesDesempenho detalhes = dataSet.get(position);
        vincula(holder, detalhes);
    }

    private void vincula(ViewHolder holder, DetalhesDesempenho detalhes) {
        holder.placaTxt.setText(detalhes.getPlaca());
        holder.valorTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(detalhes.getValor()));
        holder.motoristaTxt.setText(detalhes.getNome());

        String percentual = formataPercentual(detalhes.getPercentual());
        holder.percentualTxt.setText(percentual);
    }

    private String formataPercentual(BigDecimal valor) {
        String percentual = valor.toPlainString();
        percentual = percentual.replace(".00", " %");
        return percentual;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void atualiza (List<DetalhesDesempenho> dataSet){
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

}

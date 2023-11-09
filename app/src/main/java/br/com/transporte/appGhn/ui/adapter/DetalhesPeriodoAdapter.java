package br.com.transporte.appGhn.ui.adapter;

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
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.util.FormataNumerosUtil;

public class DetalhesPeriodoAdapter extends RecyclerView.Adapter <DetalhesPeriodoAdapter.ViewHolder>{
    private final Context context;
    private final List<MappedRecylerData> dataSet;

    public DetalhesPeriodoAdapter(Context context, List<MappedRecylerData> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

     static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView campoPlaca, campoPercentual, campoMotorista, campoValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campoPlaca = itemView.findViewById(R.id.rec_detalhes_periodo_placa);
            campoValor = itemView.findViewById(R.id.rec_detalhes_periodo_valor);
            campoPercentual = itemView.findViewById(R.id.rec_detalhes_periodo_percentual);
            campoMotorista = itemView.findViewById(R.id.rec_detalhes_periodo_motorista);
        }

    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public DetalhesPeriodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_item_detalhes_periodo, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull DetalhesPeriodoAdapter.ViewHolder holder, int position) {
        final MappedRecylerData cavaloDesempenhoMapped = dataSet.get(position);
        vincula(holder, cavaloDesempenhoMapped);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //----------------------------------------------
    // -> Vincula                                 ||
    //----------------------------------------------

    private void vincula(@NonNull ViewHolder holder, @NonNull MappedRecylerData cavaloDesempenhoMapped) {
        holder.campoPlaca.setText(cavaloDesempenhoMapped.getPlaca());
        holder.campoValor.setText(FormataNumerosUtil.formataMoedaPadraoBr(cavaloDesempenhoMapped.getValor()));
        holder.campoMotorista.setText(cavaloDesempenhoMapped.getNome());

        final String percentual = formataPercentual(cavaloDesempenhoMapped.getPercentual());
        holder.campoPercentual.setText(percentual);
    }

    @NonNull
    private String formataPercentual(@NonNull BigDecimal valor) {
        String percentual;
        try{
            percentual = valor.toPlainString();
            percentual = percentual.replace(".00", " %");
        } catch (NullPointerException e){
            percentual = "-";
        }

        return percentual;
    }

    //--------------------------------------- Metodos publicos -------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza (final List<MappedRecylerData> dataSet){
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

}

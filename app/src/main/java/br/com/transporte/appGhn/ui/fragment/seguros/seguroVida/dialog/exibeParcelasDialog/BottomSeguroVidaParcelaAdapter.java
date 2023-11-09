package br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.dialog.exibeParcelasDialog;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;

public class BottomSeguroVidaParcelaAdapter extends RecyclerView.Adapter<BottomSeguroVidaParcelaAdapter.ViewHolder> {
    private final List<Parcela_seguroVida> dataSet;
    private final Context context;
    private HashMap<Integer, Boolean> map;
    private OnItemCLickListener onItemCLickListener;

    public BottomSeguroVidaParcelaAdapter(List<Parcela_seguroVida> lista, Context context) {
        this.dataSet = lista;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void defineDataSet(List<Parcela_seguroVida> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        map = criaMap();
        notifyDataSetChanged();
    }

    @NonNull
    private HashMap<Integer, Boolean> criaMap() {
        HashMap<Integer, Boolean> map = new HashMap<>();

        for (Parcela_seguroVida p : dataSet) {
            map.put(p.getNumeroDaParcela(), false);
        }

        return map;
    }



    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView numeroParcelaTxt, dataTxt, valorTxt;
        private final CheckBox checkBox;
        private final ImageView icPagoImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroParcelaTxt = itemView.findViewById(R.id.rec_item_parcela_seguro_numero);
            dataTxt = itemView.findViewById(R.id.rec_item_parcela_seguro_data);
            valorTxt = itemView.findViewById(R.id.rec_item_parcela_seguro_valor);
            checkBox = itemView.findViewById(R.id.rec_item_parcela_seguro_check);
            icPagoImg = itemView.findViewById(R.id.rec_item_parcela_seguro_ok);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public BottomSeguroVidaParcelaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_item_pagamento_seguro, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBind                                            ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull BottomSeguroVidaParcelaAdapter.ViewHolder holder, int position) {
        Parcela_seguroVida parcela = dataSet.get(position);
        vincula(holder, parcela);
        configuraListeners(holder, parcela);
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull Parcela_seguroVida parcela) {
        holder.dataTxt.setText(ConverteDataUtil.dataParaString(parcela.getData()));
        holder.numeroParcelaTxt.setText(String.valueOf(parcela.getNumeroDaParcela()));
        holder.valorTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(parcela.getValor()));

        if (parcela.isPaga()) {
            holder.checkBox.setVisibility(GONE);
            holder.icPagoImg.setVisibility(VISIBLE);
        } else {
            holder.checkBox.setVisibility(VISIBLE);
            holder.icPagoImg.setVisibility(View.GONE);
        }
    }

    private void configuraListeners(@NonNull ViewHolder holder, Parcela_seguroVida parcela) {
        holder.itemView.setOnClickListener(v -> onItemCLickListener.onItemClick(parcela));

        holder.checkBox.setOnClickListener(v -> {
            map = verificaSeTemCheckBoxMarcado(parcela, holder.checkBox.isChecked());
            onItemCLickListener.onBoxClick(map);
        });
    }

    private HashMap<Integer, Boolean> verificaSeTemCheckBoxMarcado(Parcela_seguroVida parcela, boolean isChecked) {
        if(isChecked){
            map.replace(parcela.getNumeroDaParcela(), true);
        } else {
            map.replace(parcela.getNumeroDaParcela(), false);
        }
        return map;
    }

    //----------------------------------------- Metodos Publicos -----------------------------------

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Parcela_seguroVida> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface OnItemCLickListener {
        void onBoxClick(HashMap<Integer, Boolean> map);

        void onItemClick(Parcela_seguroVida parcela);
    }

}

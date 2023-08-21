package br.com.transporte.AppGhn.ui.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class BottomSeguroParcelaAdapter extends RecyclerView.Adapter<BottomSeguroParcelaAdapter.ViewHolder> {
    private final List<ParcelaDeSeguro> lista;
    private final Context context;
    private HashMap<Integer, Boolean> map;
    private OnItemCLickListener onItemCLickListener;

    public BottomSeguroParcelaAdapter(List<ParcelaDeSeguro> lista, Context context) {
        this.lista = lista;
        this.context = context;
        map = criaMap();
    }

    private HashMap<Integer, Boolean> criaMap() {
        HashMap<Integer, Boolean> map = new HashMap<>();

        for (ParcelaDeSeguro p : lista) {
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
    public BottomSeguroParcelaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_item_pagamento_seguro, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBind                                            ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull BottomSeguroParcelaAdapter.ViewHolder holder, int position) {
        ParcelaDeSeguro parcela = lista.get(position);
        vincula(holder, parcela);
        configuraListeners(holder, parcela);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, ParcelaDeSeguro parcela) {
        holder.dataTxt.setText(FormataDataUtil.dataParaString(parcela.getData()));
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraListeners(ViewHolder holder, ParcelaDeSeguro parcela) {
        holder.itemView.setOnClickListener(v -> onItemCLickListener.onItemClick(parcela));

        holder.checkBox.setOnClickListener(v -> {
            map = verificaSeTemCheckBoxMarcado(parcela, holder.checkBox.isChecked());
            onItemCLickListener.onBoxClick(map);
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private HashMap<Integer, Boolean> verificaSeTemCheckBoxMarcado(ParcelaDeSeguro parcela, boolean isChecked) {
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
        return lista.size();
    }

    public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener;
    }

    public void atualiza(List<ParcelaDeSeguro> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface OnItemCLickListener {
        void onBoxClick(HashMap<Integer, Boolean> map);

        void onItemClick(ParcelaDeSeguro parcela);
    }


}

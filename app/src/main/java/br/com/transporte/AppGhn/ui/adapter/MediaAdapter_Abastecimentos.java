package br.com.transporte.AppGhn.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class MediaAdapter_Abastecimentos extends RecyclerView.Adapter<MediaAdapter_Abastecimentos.ViewHolder> {
    private final List<CustosDeAbastecimento> dataSet;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MediaAdapter_Abastecimentos(List<CustosDeAbastecimento> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxt, kmTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxt = itemView.findViewById(R.id.item_media_data);
            kmTxt = itemView.findViewById(R.id.item_media_km);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public MediaAdapter_Abastecimentos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_item_media, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBind                                            ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MediaAdapter_Abastecimentos.ViewHolder holder, int position) {
        CustosDeAbastecimento abastecimento = dataSet.get(position);
        vincula(holder, abastecimento);
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(holder.getAdapterPosition(), abastecimento);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, CustosDeAbastecimento abastecimento) {
        String data = FormataDataUtil.dataParaString(abastecimento.getData());
        holder.dataTxt.setText(data);

        String marcacaoKm = FormataNumerosUtil.formataNumero(abastecimento.getMarcacaoKm());
        String marcacaoKmFormatada = "Km " + marcacaoKm;
        holder.kmTxt.setText(marcacaoKmFormatada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Metodos Publicos                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void atualiza(List<CustosDeAbastecimento> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void remove(int posicao) {
        dataSet.remove(posicao);
        notifyItemRemoved(posicao);
    }

    public void adiciona(int posicao, CustosDeAbastecimento abastecimento) {
        this.dataSet.add(posicao, abastecimento);
        notifyItemInserted(posicao);
    }

    public List<CustosDeAbastecimento> getDataSet() {
        return new ArrayList<>(dataSet);
    }


    //----------------------------------------------------------------------------------------------
    //                                          Click Listener                                    ||
    //----------------------------------------------------------------------------------------------
    public interface OnItemClickListener {
        void onClick(int position, CustosDeAbastecimento abastecimento);
    }

}

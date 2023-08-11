package br.com.transporte.AppGhn.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.SelecionaCavalo;

public class SelecionaCavaloAdapter extends RecyclerView.Adapter <SelecionaCavaloAdapter.ViewHolder>{
    private final List<Cavalo> lista;
    private final SelecionaCavalo context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SelecionaCavaloAdapter(SelecionaCavalo context, List<Cavalo> lista) {
        this.lista = lista;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView placaTxtView;
        private final ImageView icCaminhaoImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxtView = itemView.findViewById(R.id.rec_item_seleciona_cavalo_txt);
            icCaminhaoImgView = itemView.findViewById(R.id.rec_item_seleciona_cavalo_img);
        }
    }

    @NonNull
    @Override
    public SelecionaCavaloAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_seleciona_cavalo, parent, false);
        return new ViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecionaCavaloAdapter.ViewHolder holder, int position) {
        Cavalo cavalo = lista.get(position);
        configuraUi(holder);
        vincula(holder, cavalo);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(cavalo));
    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.icCaminhaoImgView.setColorFilter(Color.parseColor("#FFFFFFFF"));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<Cavalo> lista){
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    private void vincula(ViewHolder holder, Cavalo cavalo){
        holder.placaTxtView.setText(cavalo.getPlaca());
    }
}

package br.com.transporte.AppGhn.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Cavalo;

public class MediaAdapter_Cavalos extends RecyclerView.Adapter<MediaAdapter_Cavalos.ViewHolder> {

    private final Context context;
    private final List<Cavalo> dataSet;
    private Cavalo cavaloArmazenado;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MediaAdapter_Cavalos(Context context, List<Cavalo> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView placaTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxt = itemView.findViewById(R.id.rec_item_cavalo_media);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public MediaAdapter_Cavalos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.recycler_cavalos_media, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBind                                            ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull MediaAdapter_Cavalos.ViewHolder holder, int position) {
        Cavalo cavalo = dataSet.get(position);
        vincula(holder, cavalo);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(cavalo, position));
    }

    private void vincula(ViewHolder holder, Cavalo cavalo) {
        holder.placaTxt.setText(cavalo.getPlaca());
    }

    //----------------------------------------------------------------------------------------------
    //                                          Metodos Publicos                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void atualiza(List<Cavalo> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void teste(int posicao) {
        Log.d("Teste", "posicao recebida para alterar -> "+posicao);
        if (cavaloArmazenado == null) {
            cavaloArmazenado = dataSet.get(posicao);
            dataSet.remove(posicao);
            notifyItemRemoved(posicao);
        } else {
            dataSet.add(cavaloArmazenado);
            notifyItemInserted(getItemCount());

            cavaloArmazenado = dataSet.get(posicao);
            dataSet.remove(posicao);
            notifyItemRemoved(posicao);
        }

        Log.d("teste", "Cavalo armazenado ->"+cavaloArmazenado);
        Log.d("teste", "dataSet -> "+ ""+dataSet);
        Log.d("teste", " ");



    }


    public interface OnItemClickListener {
        void onItemClick(Cavalo cavalo, int posicao);
    }


}

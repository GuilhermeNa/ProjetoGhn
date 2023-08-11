package br.com.transporte.AppGhn.ui.adapter;

import static br.com.transporte.AppGhn.ui.fragment.extensions.BitmapImagem.decodificaBitmapEmString;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.home.FuncionariosFragment;
import br.com.transporte.AppGhn.model.Motorista;

public class MotoristasAdapter extends RecyclerView.Adapter<MotoristasAdapter.ViewHolder> {
    private final FuncionariosFragment context;
    private List<Motorista> lista;
    private OnItemClickListener onItemClickListener;

    public MotoristasAdapter(FuncionariosFragment context, List<Motorista> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void setonItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView fotoImgView;
        private final TextView nomeTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoImgView = itemView.findViewById(R.id.rec_item_motorista_foto);
            nomeTxtView = itemView.findViewById(R.id.rec_item_motorista_nome);
        }
    }

    @NonNull
    @Override
    public MotoristasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_motorista, parent, false);
        return new ViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Motorista motorista = lista.get(position);
        vincula(holder, motorista);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(motorista.getId()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<Motorista> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    public void setListaFiltrada(List<Motorista> listaFiltrada){
        this.lista = listaFiltrada;
        notifyDataSetChanged();
    }

    public void vincula(ViewHolder holder, Motorista motorista) {
        try{
            holder.fotoImgView.setImageBitmap(decodificaBitmapEmString(motorista));
        } catch ( NullPointerException e){
            e.printStackTrace();
            e.getMessage();
            holder.fotoImgView.setImageResource(R.drawable.img_motorista);
        }
        holder.nomeTxtView.setText(motorista.getNome());
    }

}

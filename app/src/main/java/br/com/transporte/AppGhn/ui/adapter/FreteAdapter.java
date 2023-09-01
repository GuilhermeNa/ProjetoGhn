package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
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
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaFreteFragment;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class FreteAdapter extends RecyclerView.Adapter<FreteAdapter.ViewHolder> {
    public static final String DRAWABLE_DONE = "done";
    public static final String DRAWABLE_UNDONE = "undone";
    private final List<Frete> dataSet;
    private final AreaMotoristaFreteFragment context;
    private OnItemClickListener onItemClickListener;

    public FreteAdapter(AreaMotoristaFreteFragment context, List<Frete> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxtView, origemTxtView, destinoTxtView, empresaTxtView, cargaTxtView, freteBrutoTxtView, descontosTxtView;
        private final ImageView xvImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_frete_data);
            origemTxtView = itemView.findViewById(R.id.rec_item_frete_origem);
            destinoTxtView = itemView.findViewById(R.id.rec_item_frete_destino);
            empresaTxtView = itemView.findViewById(R.id.rec_item_frete_empresa);
            cargaTxtView = itemView.findViewById(R.id.rec_item_frete_carga);
            freteBrutoTxtView = itemView.findViewById(R.id.rec_item_frete_bruto);
            descontosTxtView = itemView.findViewById(R.id.rec_item_frete_descontos);
            xvImgView = itemView.findViewById(R.id.rec_item_frete_comissaoXV);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public FreteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_frete, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull FreteAdapter.ViewHolder holder, int position) {
        Frete frete = dataSet.get(position);
        vincula(holder, frete);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(frete.getId()));
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull Frete frete) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.destinoTxtView.setText(frete.getDestino());
        holder.cargaTxtView.setText(frete.getCarga());
        holder.empresaTxtView.setText(frete.getEmpresa());
      /*  holder.freteBrutoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getFreteBruto()));
        holder.descontosTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getDescontos()));
        if (frete.getAdmFrete().isComissaoJaFoiPaga()) {
            holder.xvImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_DONE));
        } else {
            holder.xvImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_UNDONE));
        }*/
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //--------------------------------------- Metodos publicos -------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Frete> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(Frete frete){
        this.dataSet.add(frete);
        notifyItemInserted(getItemCount()-1);
    }

    public void remove(Frete frete){
        int posicao = -1;
        posicao = this.dataSet.indexOf(frete);
        this.dataSet.remove(frete);
        notifyItemRemoved(posicao);
    }

}

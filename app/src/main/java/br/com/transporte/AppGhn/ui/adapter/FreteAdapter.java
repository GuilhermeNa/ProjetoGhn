package br.com.transporte.AppGhn.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaFreteFragment;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class FreteAdapter extends RecyclerView.Adapter<FreteAdapter.ViewHolder> {
    private final List<Frete> lista;
    private final AreaMotoristaFreteFragment context;
    private OnItemClickListener onItemClickListener;

    public FreteAdapter(AreaMotoristaFreteFragment context, List<Frete> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

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

    @NonNull
    @Override
    public FreteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_frete, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull FreteAdapter.ViewHolder holder, int position) {
        Frete frete = lista.get(position);
        vincula(holder, frete);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(frete.getId()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<Frete> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, Frete frete) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.destinoTxtView.setText(frete.getDestino());
        holder.cargaTxtView.setText(frete.getCarga());
        holder.empresaTxtView.setText(frete.getEmpresa());
        holder.freteBrutoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getFreteBruto()));
        holder.descontosTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getDescontos()));
        if (frete.getAdmFrete().isComissaoJaFoiPaga()) {
            holder.xvImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "done"));
        } else {
            holder.xvImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "undone"));
        }
    }


}

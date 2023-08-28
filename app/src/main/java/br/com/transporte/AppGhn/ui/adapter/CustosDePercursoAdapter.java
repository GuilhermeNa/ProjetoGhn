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
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaCustosDePercursoFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class CustosDePercursoAdapter extends RecyclerView.Adapter<CustosDePercursoAdapter.ViewHolder> {
    public static final String DRAWABLE_NAO_PRECISA_REEMBOLSO = "nao_precisa_reembolso";
    public static final String DRAWABLE_UNDONE = "undone";
    public static final String DRAWABLE_DONE = "done";
    private final List<CustosDePercurso> dataSet;
    private final AreaMotoristaCustosDePercursoFragment context;
    private OnItemClickListener onItemClickListener;

    public CustosDePercursoAdapter(AreaMotoristaCustosDePercursoFragment context, List<CustosDePercurso> lista) {
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
        private final TextView dataTxtView, valorTxtView, descricaoTxtView, reembolsoTxtView;
        private final ImageView jaFoiReembolsadoImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_despesa_data);
            valorTxtView = itemView.findViewById(R.id.rec_item_despesa_valor);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_despesa_descricao);
            jaFoiReembolsadoImgView = itemView.findViewById(R.id.rec_item_despesaXV);
            reembolsoTxtView = itemView.findViewById(R.id.rec_item_despesa_reembolso);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public CustosDePercursoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_despesa, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull CustosDePercursoAdapter.ViewHolder holder, int position) {
        CustosDePercurso despesa = dataSet.get(position);
        vincula(holder, despesa);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(despesa.getId()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull CustosDePercurso despesa) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(despesa.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(despesa.getValorCusto()));
        holder.descricaoTxtView.setText(despesa.getDescricao());

        switch (despesa.getTipo()) {
            case NAO_REEMBOLSAVEL:
                holder.reembolsoTxtView.setText(R.string.nao);
                holder.jaFoiReembolsadoImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_NAO_PRECISA_REEMBOLSO));
                break;
            case REEMBOLSAVEL_EM_ABERTO:
                holder.reembolsoTxtView.setText(R.string.sim);
                holder.jaFoiReembolsadoImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_UNDONE));
                break;
            case REEMBOLSAVEL_JA_PAGO:
                holder.reembolsoTxtView.setText(R.string.sim);
                holder.jaFoiReembolsadoImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_DONE));
                break;
        }

    }

    //-------------------------------------- Metodos Publicos --------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<CustosDePercurso> listaTodos) {
        this.dataSet.clear();
        this.dataSet.addAll(listaTodos);
        notifyDataSetChanged();
    }

    public void adiciona(CustosDePercurso custoPercurso) {
        this.dataSet.add(custoPercurso);
        notifyItemInserted(getItemCount() - 1);
    }

    public void remove(CustosDePercurso custoPercurso) {
        int posicao = -1;
        posicao = this.dataSet.indexOf(custoPercurso);
        this.dataSet.remove(custoPercurso);
        notifyItemRemoved(posicao);
    }

}

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
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaCustosDePercursoFragment;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class CustosDePercursoAdapter extends RecyclerView.Adapter<CustosDePercursoAdapter.ViewHolder> {
    private final List<CustosDePercurso> lista;
    private final AreaMotoristaCustosDePercursoFragment context;
    private OnItemClickListener onItemClickListener;

    public CustosDePercursoAdapter(AreaMotoristaCustosDePercursoFragment context, List<CustosDePercurso> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

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

    @NonNull
    @Override
    public CustosDePercursoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_despesa, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CustosDePercursoAdapter.ViewHolder holder, int position) {
        CustosDePercurso despesa = lista.get(position);
        vincula(holder, despesa);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(despesa.getId()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<CustosDePercurso> listaTodos) {
        this.lista.clear();
        this.lista.addAll(listaTodos);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, CustosDePercurso despesa) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(despesa.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(despesa.getValorCusto()));
        holder.descricaoTxtView.setText(despesa.getDescricao());
        switch (despesa.getTipo()) {
            case NAO_REEMBOLSAVEL:
                holder.reembolsoTxtView.setText("Não");
                holder.jaFoiReembolsadoImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "nao_precisa_reembolso"));
                break;
            case REEMBOLSAVEL_EM_ABERTO:
                holder.reembolsoTxtView.setText("Sim");
                holder.jaFoiReembolsadoImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "undone"));
                break;
            case REEMBOLSAVEL_JA_PAGO:
                holder.reembolsoTxtView.setText("Sim");
                holder.jaFoiReembolsadoImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "done"));
                break;
        }
    }

}

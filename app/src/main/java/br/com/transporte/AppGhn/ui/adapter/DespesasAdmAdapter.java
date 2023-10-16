package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.DespesasAdmDiretasFragment;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.direta.domain.model.DespesaAdmDiretaObject;
import br.com.transporte.AppGhn.util.OnItemClickListener_getId;

public class DespesasAdmAdapter extends RecyclerView.Adapter<DespesasAdmAdapter.ViewHolder> {
    private final DespesasAdmDiretasFragment context;
    private final List<DespesaAdmDiretaObject> dataSet;
    private OnItemClickListener_getId onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DespesasAdmAdapter(@NonNull DespesasAdmDiretasFragment context, List<DespesaAdmDiretaObject> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView campoPlaca, campoData, campoValor, campoDescricao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campoPlaca = itemView.findViewById(R.id.rec_item_despesa_financeira_placa);
            campoValor = itemView.findViewById(R.id.rec_item_despesa_financeira_valor);
            campoData = itemView.findViewById(R.id.rec_item_despesa_financeira_data);
            campoDescricao = itemView.findViewById(R.id.rec_item_despesa_financeira_descricao);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public DespesasAdmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_despesa_financeira, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull DespesasAdmAdapter.ViewHolder holder, int position) {
        DespesaAdmDiretaObject despesaObject = dataSet.get(position);
        vincula(holder, despesaObject);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(despesaObject.getIdCertificado()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void vincula(@NonNull ViewHolder holder, @NonNull DespesaAdmDiretaObject despesa) {
        try {
            BindData.fromLocalDate(holder.campoData, despesa.getData());
            BindData.fromString(holder.campoPlaca, despesa.getPlaca());
            BindData.R$fromBigDecimal(holder.campoValor, despesa.getValor());
            BindData.fromString(holder.campoDescricao, despesa.getDescricao());

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(final List<DespesaAdmDiretaObject> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(DespesaAdmDiretaObject despesaAdm) {
        this.dataSet.add(despesaAdm);
        notifyItemInserted(getItemCount()-1);
    }

    public void remove(DespesaAdmDiretaObject despesaAdm){
        int posicao = -1;
        posicao = this.dataSet.indexOf(despesaAdm);
        this.dataSet.remove(despesaAdm);
        notifyItemRemoved(posicao);
    }

}

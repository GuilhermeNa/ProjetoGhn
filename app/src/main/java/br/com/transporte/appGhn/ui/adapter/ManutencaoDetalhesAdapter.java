package br.com.transporte.appGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.ui.fragment.manutencao.ManutencaoDetalhesFragment;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.OnItemClickListener_getId;

public class ManutencaoDetalhesAdapter extends RecyclerView.Adapter<ManutencaoDetalhesAdapter.ViewHolder> {
    private final List<CustosDeManutencao> dataSet;
    private final ManutencaoDetalhesFragment context;
    private OnItemClickListener_getId onItemClickListener;
    private int ultimaPosicaoAcessadaPeloUsuario = 0;

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ManutencaoDetalhesAdapter(ManutencaoDetalhesFragment context, List<CustosDeManutencao> lista) {
        this.dataSet = lista;
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ManutencaoDetalhesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_manutencao_detalhe, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView campoData, campoValor, campoEmpresa, campoDescricao, campoNota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campoData = itemView.findViewById(R.id.rec_item_manutencao_detalhe_data);
            campoValor = itemView.findViewById(R.id.rec_item_manutencao_detalhe_valor);
            campoEmpresa = itemView.findViewById(R.id.rec_item_manutencao_detalhe_empresa);
            campoDescricao = itemView.findViewById(R.id.rec_item_manutencao_detalhe_descricao);
            campoNota = itemView.findViewById(R.id.rec_item_manutencao_detalhe_nota);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ManutencaoDetalhesAdapter.ViewHolder holder, int position) {
        CustosDeManutencao manutencao = dataSet.get(position);
        vincula(holder, manutencao);
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick_getId(manutencao.getId());
            ultimaPosicaoAcessadaPeloUsuario = dataSet.indexOf(manutencao);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull CustosDeManutencao manutencao) {
        holder.campoData.setText(ConverteDataUtil.dataParaString(manutencao.getData()));
        holder.campoValor.setText(FormataNumerosUtil.formataMoedaPadraoBr(manutencao.getValorCusto()));
        holder.campoEmpresa.setText(manutencao.getEmpresa());
        holder.campoDescricao.setText(manutencao.getDescricao());
        holder.campoNota.setText(manutencao.getNNota());
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(final List<CustosDeManutencao> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void adiciona(final CustosDeManutencao custoManutencao) {
        if (dataSet.isEmpty()) {
            dataSet.add(custoManutencao);
            notifyDataSetChanged();
        } else {
            dataSet.add(0, custoManutencao);
            notifyItemInserted(0);
        }
    }

    public void notificaQueItemFoiRemovido() {
        this.dataSet.remove(ultimaPosicaoAcessadaPeloUsuario);
        notifyItemRemoved(ultimaPosicaoAcessadaPeloUsuario);
    }

    public void notificaQueItemFoiEditado(final CustosDeManutencao custoManutencao) {
        dataSet.set(ultimaPosicaoAcessadaPeloUsuario, custoManutencao);
        notifyItemChanged(ultimaPosicaoAcessadaPeloUsuario);
    }

}

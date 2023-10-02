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
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.ui.fragment.manutencao.ManutencaoDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.OnItemClickListener_getId;

public class ManutencaoDetalhesAdapter extends RecyclerView.Adapter<ManutencaoDetalhesAdapter.ViewHolder> {
    private final List<CustosDeManutencao> dataSet;
    private final ManutencaoDetalhesFragment context;
    private OnItemClickListener_getId onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ManutencaoDetalhesAdapter(ManutencaoDetalhesFragment context, List<CustosDeManutencao> lista) {
        this.dataSet = lista;
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxtView, valorTxtView, empresaTxtView, descricaoTxtView, numeroNotaTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_data);
            valorTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_valor);
            empresaTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_empresa);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_descricao);
            numeroNotaTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_nota);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ManutencaoDetalhesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_manutencao_detalhe, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ManutencaoDetalhesAdapter.ViewHolder holder, int position) {
        CustosDeManutencao manutencao = dataSet.get(position);
        vincula(holder, manutencao);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(manutencao.getId()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull CustosDeManutencao manutencao) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(manutencao.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(manutencao.getValorCusto()));
        holder.empresaTxtView.setText(manutencao.getEmpresa());
        holder.descricaoTxtView.setText(manutencao.getDescricao());
        holder.numeroNotaTxtView.setText(manutencao.getNNota());
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<CustosDeManutencao> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(CustosDeManutencao custoManutencao){
        this.dataSet.add(custoManutencao);
        notifyItemInserted(getItemCount()-1);
    }

    public void remove(CustosDeManutencao custoManutencao){
        int posicao = -1;
        posicao = this.dataSet.indexOf(custoManutencao);
        this.dataSet.remove(custoManutencao);
        notifyItemRemoved(posicao);
    }

}

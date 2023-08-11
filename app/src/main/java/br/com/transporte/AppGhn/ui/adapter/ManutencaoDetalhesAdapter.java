package br.com.transporte.AppGhn.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.ManutencaoDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;

public class ManutencaoDetalhesAdapter extends RecyclerView.Adapter<ManutencaoDetalhesAdapter.ViewHolder> {
    private final List<CustosDeManutencao> lista;
    private final ManutencaoDetalhesFragment context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ManutencaoDetalhesAdapter(ManutencaoDetalhesFragment context, List<CustosDeManutencao> lista) {
        this.lista = lista;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView datatxtView, valorTxtView, empresaTxtView, descricaoTxtView, numeroNotaTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datatxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_data);
            valorTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_valor);
            empresaTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_empresa);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_descricao);
            numeroNotaTxtView = itemView.findViewById(R.id.rec_item_manutencao_detalhe_nota);
        }
    }

    @NonNull
    @Override
    public ManutencaoDetalhesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_manutencao_detalhe, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ManutencaoDetalhesAdapter.ViewHolder holder, int position) {
        CustosDeManutencao manutencao = lista.get(position);
        vincula(holder, manutencao);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(manutencao));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<CustosDeManutencao> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, CustosDeManutencao manutencao) {
        holder.datatxtView.setText(FormataDataUtil.dataParaString(manutencao.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(manutencao.getValorCusto()));
        holder.empresaTxtView.setText(manutencao.getEmpresa());
        holder.descricaoTxtView.setText(manutencao.getDescricao());
        holder.numeroNotaTxtView.setText(manutencao.getnNota());
    }
}

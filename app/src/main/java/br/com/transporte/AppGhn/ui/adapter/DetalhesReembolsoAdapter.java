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
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DetalhesReembolsoAdapter extends RecyclerView.Adapter <DetalhesReembolsoAdapter.ViewHolder> {
    private final List<CustosDePercurso> lista;
    private final ComissoesDetalhesFragment context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public DetalhesReembolsoAdapter(ComissoesDetalhesFragment context, List<CustosDePercurso> lista) {
        this.lista = lista;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView dataEditText, valorEditText, descricaoEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataEditText = itemView.findViewById(R.id.rec_salario_detalhes_item_reembolso_data);
            valorEditText = itemView.findViewById(R.id.rec_salario_detalhes_item_reembolso_valor);
            descricaoEditText = itemView.findViewById(R.id.rec_salario_detalhes_item_reembolso_descricao);
        }
    }

    @NonNull
    @Override
    public DetalhesReembolsoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_comissoes_detalhes_item_reembolso, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DetalhesReembolsoAdapter.ViewHolder holder, int position) {
        CustosDePercurso custosDePercurso = lista.get(position);
        vincula(holder, custosDePercurso);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(custosDePercurso));
    }

    public void atualiza(List<CustosDePercurso> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void vincula(ViewHolder holder, CustosDePercurso custosDePercurso){
        holder.dataEditText.setText(FormataDataUtil.dataParaString(custosDePercurso.getData()));
        holder.valorEditText.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosDePercurso.getValorCusto()));
        holder.descricaoEditText.setText(custosDePercurso.getDescricao());
    }

}

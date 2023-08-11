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
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.DespesasAdmIndiretasFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DespesasAdmIndiretasAdapter extends RecyclerView.Adapter <DespesasAdmIndiretasAdapter.ViewHolder>{
    private final DespesasAdmIndiretasFragment context;
    private final List<DespesaAdm> lista;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public DespesasAdmIndiretasAdapter(DespesasAdmIndiretasFragment context, List<DespesaAdm> lista) {
        this.context = context;
        this.lista = lista;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView placaTxtView, valorTxtView, dataTxtView, descricaoTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_placa);
            valorTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_valor);
            dataTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_data);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_despesa_financeira_descricao);
        }
    }

    @NonNull
    @Override
    public DespesasAdmIndiretasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_despesa_financeira, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DespesasAdmIndiretasAdapter.ViewHolder holder, int position) {
    DespesaAdm despesa = lista.get(position);
    vincula(holder, despesa);
    holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(despesa));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, DespesaAdm despesa) {
        holder.placaTxtView.setVisibility(View.GONE);
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(despesa.getValorDespesa()));
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(despesa.getData()));
        holder.descricaoTxtView.setText(despesa.getDescricao());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<DespesaAdm> lista){
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

}

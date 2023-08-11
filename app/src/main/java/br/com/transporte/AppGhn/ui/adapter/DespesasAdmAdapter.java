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
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.ui.fragment.despesasAdm.DespesasAdmDiretasFragment;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;

public class DespesasAdmAdapter extends RecyclerView.Adapter<DespesasAdmAdapter.ViewHolder> {
    private final DespesasAdmDiretasFragment context;
    private final List<DespesaAdm> lista;
    private final CavaloDAO cavaloDao;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DespesasAdmAdapter(DespesasAdmDiretasFragment context, List<DespesaAdm> lista) {
        this.context = context;
        this.lista = lista;
        cavaloDao = new CavaloDAO();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView placaTxtView, dataTxtView, valorTxtView, descricaoTxtView;

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
    public DespesasAdmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_despesa_financeira, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DespesasAdmAdapter.ViewHolder holder, int position) {
        DespesaAdm despesa = lista.get(position);
        vincula(holder, despesa);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(despesa));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<DespesaAdm> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void vincula(ViewHolder holder, DespesaAdm despesa) {
        String placa = cavaloDao.localizaPeloId(despesa.getRefCavalo()).getPlaca();
        holder.placaTxtView.setText(placa);
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(despesa.getValorDespesa()));
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(despesa.getData()));
        holder.descricaoTxtView.setText(despesa.getDescricao());
    }

}

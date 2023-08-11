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
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.ui.fragment.ImpostosFragment;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;

public class ImpostosAdapter extends RecyclerView.Adapter <ImpostosAdapter.ViewHolder> {
    private final List<DespesasDeImposto> lista;
    private final ImpostosFragment context;
    private final CavaloDAO cavaloDao;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImpostosAdapter(List<DespesasDeImposto> lista, ImpostosFragment context) {
        this.lista = lista;
        this.context = context;
        cavaloDao = new CavaloDAO();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tipoImpostoTxtView, placaTxtView, dataTxtView, valorTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tipoImpostoTxtView = itemView.findViewById(R.id.rec_item_impostos_tipo);
            placaTxtView = itemView.findViewById(R.id.rec_item_impostos_placa);
            dataTxtView = itemView.findViewById(R.id.rec_item_impostos_data);
            valorTxtView = itemView.findViewById(R.id.rec_item_impostos_valor);
        }
    }

    @NonNull
    @Override
    public ImpostosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_impostos, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ImpostosAdapter.ViewHolder holder, int position) {
        DespesasDeImposto imposto = lista.get(position);
        vincula(holder, imposto);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(imposto));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<DespesasDeImposto> lista){
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void vincula(ViewHolder holder, DespesasDeImposto imposto) {
        try{
            String placa = cavaloDao.localizaPeloId(imposto.getRefCavalo()).getPlaca();
            holder.placaTxtView.setText(placa);
        } catch (NullPointerException e){
            e.getMessage();
            holder.placaTxtView.setText(" ");
        }
        holder.tipoImpostoTxtView.setText(imposto.getNome());
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(imposto.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(imposto.getValorDespesa()));
    }

}

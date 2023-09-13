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
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.ui.fragment.ImpostosFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.OnItemClickListenerNew;

public class ImpostosAdapter extends RecyclerView.Adapter <ImpostosAdapter.ViewHolder> {
    private final List<DespesasDeImposto> dataSet;
    private final ImpostosFragment context;
    private final CavaloDAO cavaloDao;
    private OnItemClickListenerNew onItemClickListener;

    public void setOnItemClickListener(OnItemClickListenerNew onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImpostosAdapter(List<DespesasDeImposto> lista, ImpostosFragment context) {
        this.dataSet = lista;
        this.context = context;
        cavaloDao = new CavaloDAO();
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

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

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ImpostosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_impostos, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ImpostosAdapter.ViewHolder holder, int position) {
        DespesasDeImposto imposto = dataSet.get(position);
        vincula(holder, imposto);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(imposto.getId()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(ViewHolder holder, DespesasDeImposto imposto) {
        try{
            String placa = cavaloDao.localizaPeloId(imposto.getRefCavaloId()).getPlaca();
            holder.placaTxtView.setText(placa);
        } catch (NullPointerException e){
            e.printStackTrace();
            holder.placaTxtView.setText(" ");
        }
        holder.tipoImpostoTxtView.setText(imposto.getNome());
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(imposto.getData()));
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(imposto.getValorDespesa()));
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<DespesasDeImposto> lista){
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(DespesasDeImposto despesaImposto){
        this.dataSet.add(despesaImposto);
        notifyItemInserted(getItemCount()-1);
    }

    public void remove(DespesasDeImposto despesaImposto){
        int posicao = -1;
        posicao = this.dataSet.indexOf(despesaImposto);
        this.dataSet.remove(despesaImposto);
        notifyItemRemoved(posicao);
    }

}

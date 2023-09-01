package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnLongClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DetalhesFreteAdapter extends RecyclerView.Adapter<DetalhesFreteAdapter.ViewHolder> {
    private final List<Frete> dataSet;
    private int posicao;
    private final ComissoesDetalhesFragment context;
    private OnLongClickListener onLongClickListener;

    public DetalhesFreteAdapter(ComissoesDetalhesFragment context, List<Frete> lista) {
        this.dataSet = lista;
        this.context = context;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView origemTxtView, destinoTxtView, freteTxtView, comissaoTxtView, dataTxtView, comissaoPercentualTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_frete_data);
            origemTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_frete_origem);
            destinoTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_frete_destino);
            freteTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_frete_frete_bruto_valor);
            comissaoTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_frete_comissao_valor);
            comissaoPercentualTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_frete_comissao_percentual);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.editarComissao, Menu.NONE, R.string.editar_comissao);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public DetalhesFreteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_comissoes_detalhes_item_frete, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull DetalhesFreteAdapter.ViewHolder holder, int position) {
        Frete frete = dataSet.get(position);
        vincula(holder, frete);
        holder.itemView.setOnLongClickListener(v -> {
            onLongClickListener.onLongClick(ComissoesDetalhesFragment.TipoDeAdapterPressionado.FRETE);
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public void vincula(@NonNull ViewHolder holder, @NonNull Frete frete) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.destinoTxtView.setText(frete.getDestino());
        //holder.freteTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getFreteBruto()));
        //holder.comissaoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getComissaoAoMotorista()));

        //String comissaoPercentual = R.string.comissao +" "+ frete.getAdmFrete().getComissaoPercentualAplicada() + " %";
        //holder.comissaoPercentualTxtView.setText(comissaoPercentual);
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    public int getPosicao() {
        return posicao;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Frete> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void atualizaItem(int posicao) {
        notifyItemChanged(posicao);
    }

}

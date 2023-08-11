package br.com.transporte.AppGhn.ui.adapter;

import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnLongClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DetalhesFreteAdapter extends RecyclerView.Adapter<DetalhesFreteAdapter.ViewHolder> {
    private final List<Frete> lista;
    private int posicao;
    private final ComissoesDetalhesFragment context;
    private OnLongClickListener onLongClickListener;

    public DetalhesFreteAdapter(ComissoesDetalhesFragment context, List<Frete> lista) {
        this.lista = lista;
        this.context = context;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView origemTxtView, destinoTxtView, freteTxtView, comissaoTxtView, dataTxtView, comissaoPercentualTxtView;

        @RequiresApi(api = Build.VERSION_CODES.N)
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
            menu.add(Menu.NONE, R.id.editarComissao, Menu.NONE, "Editar comissão");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public DetalhesFreteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_comissoes_detalhes_item_frete, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DetalhesFreteAdapter.ViewHolder holder, int position) {
        Frete frete = lista.get(position);
        vincula(holder, frete);
        holder.itemView.setOnLongClickListener(v -> {
            onLongClickListener.onLongClick(ComissoesDetalhesFragment.TipoDeAdapterPressionado.FRETE);
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    public void atualiza(List<Frete> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    public void atualizaItem(int posicao) {
        notifyItemChanged(posicao);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void vincula(ViewHolder holder, Frete frete) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.destinoTxtView.setText(frete.getDestino());
        holder.freteTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getFreteBruto()));
        holder.comissaoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(frete.getAdmFrete().getComissaoAoMotorista()));
        holder.comissaoPercentualTxtView.setText("Comissão " + frete.getAdmFrete().getComissaoPercentualAplicada() + " %");
    }

}

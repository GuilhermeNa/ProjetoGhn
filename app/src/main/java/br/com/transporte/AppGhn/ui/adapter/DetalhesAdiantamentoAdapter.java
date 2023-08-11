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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.adapter.listener.OnLongClickListener;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;

public class DetalhesAdiantamentoAdapter extends RecyclerView.Adapter<DetalhesAdiantamentoAdapter.ViewHolder> {
    private final List<Adiantamento> lista;
    private final ComissoesDetalhesFragment context;
    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;
    private final Map<Integer, BigDecimal> map;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    public DetalhesAdiantamentoAdapter(List<Adiantamento> lista, ComissoesDetalhesFragment context, Map<Integer, BigDecimal> map) {
        this.lista = lista;
        this.context = context;
        this.map = map;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private final TextView dataTxtView, valorTotalTxtView, saldoRestituidoTxtView, emAbertoTxtView, descontarTxtView;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_data);
            valorTotalTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_valor);
            saldoRestituidoTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_descontado_valor);
            emAbertoTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_em_aberto_valor);
            descontarTxtView = itemView.findViewById(R.id.rec_salario_detalhes_item_adiantamento_descontar_valor);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.editarDesconto, Menu.NONE, "Editar desconto");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public DetalhesAdiantamentoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_comissoes_detalhes_item_adiantamento, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DetalhesAdiantamentoAdapter.ViewHolder holder, int position) {
        Adiantamento adiantamento = lista.get(position);
        vincula(holder, adiantamento);
        configuraListeners(holder, adiantamento);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configuraListeners(@NonNull ViewHolder holder, Adiantamento adiantamento) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(adiantamento));
        holder.itemView.setOnLongClickListener(v -> {
            onLongClickListener.onLongClick(ComissoesDetalhesFragment.TipoDeAdapterPressionado.ADIANTAMENTO);
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    private int posicao;

    public void atualiza(List<Adiantamento> lista){
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public void atualizaMap() {
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void vincula(ViewHolder holder, Adiantamento adiantamento) {
        BigDecimal valorDescontar = map.get(adiantamento.getId());
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(adiantamento.getData()));
        holder.valorTotalTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.getValorTotal()));
        holder.saldoRestituidoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.getSaldoRestituido()));
        holder.emAbertoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamento.restaReembolsar()));
        holder.descontarTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorDescontar));
    }


}

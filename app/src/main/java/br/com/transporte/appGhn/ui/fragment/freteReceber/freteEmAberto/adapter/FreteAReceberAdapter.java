package br.com.transporte.appGhn.ui.fragment.freteReceber.freteEmAberto.adapter;

import static br.com.transporte.appGhn.model.enums.TipoRecebimentoFrete.ADIANTAMENTO;
import static br.com.transporte.appGhn.model.enums.TipoRecebimentoFrete.SALDO;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.filtros.FiltraRecebimentoFrete;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.ui.fragment.freteReceber.freteEmAberto.FreteAReceberEmAbertoFragment;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.ImagemUtil;
import br.com.transporte.appGhn.util.OnItemClickListener_getId;

public class FreteAReceberAdapter extends RecyclerView.Adapter<FreteAReceberAdapter.ViewHolder> {
    public static final String DRAWABLE_DONE = "done";
    public static final String DRAWABLE_UNDONE = "undone";
    private final FreteAReceberEmAbertoFragment context;
    private final List<Frete> dataSetFrete;
    private final List<Cavalo> dataSetCavalo;
    private final List<RecebimentoDeFrete> dataSetRecebimento;
    private OnItemClickListener_getId onItemClickListener;
    private int posicao;

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FreteAReceberAdapter(
            @NonNull FreteAReceberEmAbertoFragment context,
            List<Frete> listaFrete,
            List<Cavalo> listaCavalo,
            List<RecebimentoDeFrete> listaRecebimento
    ) {
        this.context = context;
        this.dataSetFrete = listaFrete;
        this.dataSetCavalo = listaCavalo;
        this.dataSetRecebimento = listaRecebimento;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final ImageView statusImg;
        private final TextView dataTxtView, placaTxtView, totalReceberTxtView, origemTxtView,
                destinoTxtView, adiantamentoTxtView, saldoTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_data);
            placaTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_placa);
            totalReceberTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_total_receber);
            origemTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_origem);
            destinoTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_destino);
            adiantamentoTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_adiantamento);
            saldoTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_saldo);
            statusImg = itemView.findViewById(R.id.rec_item_frete_receber_completo_img_status);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(@NonNull ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.FechaFrete, menu.NONE, R.string.fechar_frete);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public FreteAReceberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_frete_receber_completo, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull FreteAReceberAdapter.ViewHolder holder, int position) {
        Frete frete = dataSetFrete.get(position);
        vincula(holder, frete);
        configuraListeners(holder, frete);
    }

    private void configuraListeners(@NonNull ViewHolder holder, Frete frete) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(frete.getId()));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return dataSetFrete.size();
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    //------------------------------------------------
    // -> Vincula                                   ||
    //------------------------------------------------

    private void vincula(@NonNull ViewHolder holder, @NonNull Frete frete) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.destinoTxtView.setText(frete.getDestino());

        String placa = Objects.requireNonNull(FiltraCavalo.localizaPeloId(dataSetCavalo, frete.getRefCavaloId())).getPlaca();
        holder.placaTxtView.setText(placa);

        BigDecimal valorAReceber = frete.getFreteLiquidoAReceber();
        holder.totalReceberTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAReceber));

        BigDecimal valorAdiantamento = vinculaAdiantamento(holder, frete);
        BigDecimal valorSaldo = vinculaSaldo(holder, frete);
        configuraImgDeStatusDoRecebimento(holder, valorAReceber, valorAdiantamento, valorSaldo);
    }

    private void configuraImgDeStatusDoRecebimento(ViewHolder holder, @NonNull BigDecimal valorAReceber, @NonNull BigDecimal valorAdiantamento, BigDecimal valorSaldo) {
        BigDecimal valorRecebido = valorAdiantamento.add(valorSaldo);
        int compare = valorAReceber.compareTo(valorRecebido);
        if (compare == 0) {
            holder.statusImg.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_DONE));
        } else {
            holder.statusImg.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_UNDONE));
        }
    }

    private BigDecimal vinculaSaldo(ViewHolder holder, @NonNull Frete frete) {
        List<RecebimentoDeFrete> dataSet = FiltraRecebimentoFrete.listaPeloIdDoFrete(dataSetRecebimento, frete.getId());
        BigDecimal valorSaldo;
        try {
            RecebimentoDeFrete saldo = FiltraRecebimentoFrete.localizaPorTipo(dataSet, SALDO);
            valorSaldo = saldo.getValor();
        } catch (NullPointerException | ObjetoNaoEncontrado e) {
            e.printStackTrace();
            valorSaldo = BigDecimal.ZERO;
        }
        holder.saldoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorSaldo));
        return valorSaldo;
    }

    private BigDecimal vinculaAdiantamento(ViewHolder holder, @NonNull Frete frete) {
        List<RecebimentoDeFrete> dataSet = FiltraRecebimentoFrete.listaPeloIdDoFrete(dataSetRecebimento, frete.getId());
        BigDecimal valorAdiantamento;
        try {
            RecebimentoDeFrete adiantamento = FiltraRecebimentoFrete.localizaPorTipo(dataSet, ADIANTAMENTO);
            valorAdiantamento = adiantamento.getValor();
        } catch (NullPointerException | ObjetoNaoEncontrado e) {
            e.printStackTrace();
            valorAdiantamento = BigDecimal.ZERO;
        }
        holder.adiantamentoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAdiantamento));
        return valorAdiantamento;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Frete> listaFrete, List<RecebimentoDeFrete> listaRecebimento) {
        this.dataSetFrete.clear();
        this.dataSetFrete.addAll(listaFrete);
        this.dataSetRecebimento.clear();
        this.dataSetRecebimento.addAll(listaRecebimento);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void exibeSearch(List<Frete> listaFrete) {
        this.dataSetFrete.clear();
        this.dataSetFrete.addAll(listaFrete);
        notifyDataSetChanged();
    }

    public int getPosicao() {
        return posicao;
    }

}

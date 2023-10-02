package br.com.transporte.AppGhn.ui.adapter;

import static br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete.ADIANTAMENTO;
import static br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete.SALDO;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
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
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.filtros.FiltraRecebimentoFrete;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberPagosFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;
import br.com.transporte.AppGhn.util.OnItemClickListener_getId;

public class FreteAReceberPagoAdapter extends RecyclerView.Adapter<FreteAReceberPagoAdapter.ViewHolder> {
    public static final String DRAWABLE_DONE = "done";
    private final FreteAReceberPagosFragment context;
    private final List<Frete> dataSetFrete;
    private final List<RecebimentoDeFrete> dataSetRecebimento;
    private final List<Cavalo> dataSetCavalo;
    private OnItemClickListener_getId onItemClickListener;
    private RoomRecebimentoFreteDao recebimentoDao;

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FreteAReceberPagoAdapter(
            @NonNull FreteAReceberPagosFragment context,
            List<Frete> listaFrete,
            List<Cavalo> listaCavalo,
            List<RecebimentoDeFrete> dataSetRecebimento) {
        this.context = context;
        this.dataSetFrete = listaFrete;
        this.dataSetRecebimento = dataSetRecebimento;
        this.dataSetCavalo = listaCavalo;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxtView, placaTxtView, totalReceberTxtView, origemTxtView, destinoTxtView, adiantamentoTxtView, saldoTxtView;
        private final ImageView statusImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_data);
            placaTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_placa);
            totalReceberTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_total_receber);
            origemTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_origem);
            destinoTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_destino);
            adiantamentoTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_adiantamento);
            saldoTxtView = itemView.findViewById(R.id.rec_item_frete_receber_completo_saldo);
            statusImgView = itemView.findViewById(R.id.rec_item_frete_receber_completo_img_status);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public FreteAReceberPagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_frete_receber_completo, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull FreteAReceberPagoAdapter.ViewHolder holder, int position) {
        Frete frete = dataSetFrete.get(position);
        vincula(holder, frete);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(frete.getId()));
    }

    @Override
    public int getItemCount() {
        return dataSetFrete.size();
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
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_DONE));
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
        List<RecebimentoDeFrete> listaRecebimentos = FiltraRecebimentoFrete.listaPeloIdDoFrete(dataSetRecebimento, frete.getId());

        BigDecimal valorAdiantamento;
        try {
            RecebimentoDeFrete adiantamento = FiltraRecebimentoFrete.localizaPorTipo(listaRecebimentos, ADIANTAMENTO);
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
    public void atualiza(List<Frete> lista, List<RecebimentoDeFrete> dataSetRecebimento) {
        this.dataSetFrete.clear();
        this.dataSetFrete.addAll(lista);
        this.dataSetRecebimento.clear();
        this.dataSetRecebimento.addAll(dataSetRecebimento);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void exibeSearch(List<Frete> dataSetSearch) {
        this.dataSetFrete.clear();
        this.dataSetFrete.addAll(dataSetSearch);
        notifyDataSetChanged();
    }

}

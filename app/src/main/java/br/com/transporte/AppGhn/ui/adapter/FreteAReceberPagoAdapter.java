package br.com.transporte.AppGhn.ui.adapter;

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

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.RecebimentoFreteDAO;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberPagosFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class FreteAReceberPagoAdapter extends RecyclerView.Adapter<FreteAReceberPagoAdapter.ViewHolder> {
    public static final String DRAWABLE_DONE = "done";
    private final FreteAReceberPagosFragment context;
    private final List<Frete> dataSet;
    private OnItemClickListener onItemClickListener;
    private final RecebimentoFreteDAO recebimentoDao;
    private final CavaloDAO cavaloDao;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FreteAReceberPagoAdapter(FreteAReceberPagosFragment context, List<Frete> lista) {
        this.context = context;
        this.dataSet = lista;
        recebimentoDao = new RecebimentoFreteDAO();
        cavaloDao = new CavaloDAO();
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
        Frete frete = dataSet.get(position);
        vincula(holder, frete);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(frete));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //------------------------------------------------
    // -> Vincula                                   ||
    //------------------------------------------------

    private void vincula(@NonNull ViewHolder holder, @NonNull Frete frete) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.destinoTxtView.setText(frete.getDestino());

        String placa = cavaloDao.localizaPeloId(frete.getRefCavaloId()).getPlaca();
        holder.placaTxtView.setText(placa);

        BigDecimal valorAReceber = frete.getAdmFrete().getFreteLiquidoAReceber();
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
        RecebimentoDeFrete saldo = recebimentoDao.retornaSaldo(frete.getId());
        BigDecimal valorSaldo;
        try {
            valorSaldo = saldo.getValor();
        } catch (NullPointerException e) {
            e.printStackTrace();
            valorSaldo = BigDecimal.ZERO;
        }
        holder.saldoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorSaldo));
        return valorSaldo;
    }

    private BigDecimal vinculaAdiantamento(ViewHolder holder, @NonNull Frete frete) {
        RecebimentoDeFrete adiantamento = recebimentoDao.retornaAdiantamento(frete.getId());
        BigDecimal valorAdiantamento;
        try {
            valorAdiantamento = adiantamento.getValor();
        } catch (NullPointerException e) {
            e.printStackTrace();
            valorAdiantamento = BigDecimal.ZERO;
        }
        holder.adiantamentoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAdiantamento));
        return valorAdiantamento;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Frete> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

}

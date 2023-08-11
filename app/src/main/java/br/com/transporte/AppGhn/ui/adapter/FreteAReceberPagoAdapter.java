package br.com.transporte.AppGhn.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.RecebimentoFreteDAO;
import br.com.transporte.AppGhn.ui.fragment.freteReceber.FreteAReceberPagosFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class FreteAReceberPagoAdapter extends RecyclerView.Adapter<FreteAReceberPagoAdapter.ViewHolder> {
    private final FreteAReceberPagosFragment context;
    private final List<Frete> lista;
    private OnItemClickListener onItemClickListener;
    private final RecebimentoFreteDAO recebimentoDao;
    private final CavaloDAO cavaloDao;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FreteAReceberPagoAdapter(FreteAReceberPagosFragment context, List<Frete> lista) {
        this.context = context;
        this.lista = lista;
        recebimentoDao = new RecebimentoFreteDAO();
        cavaloDao = new CavaloDAO();
    }

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

    @NonNull
    @Override
    public FreteAReceberPagoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_frete_receber_completo, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull FreteAReceberPagoAdapter.ViewHolder holder, int position) {
        Frete frete = lista.get(position);
        vincula(holder, frete);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(frete));
    }

    public void atualiza(List<Frete> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, Frete frete) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(frete.getData()));
        holder.origemTxtView.setText(frete.getOrigem());
        holder.destinoTxtView.setText(frete.getDestino());

        String placa = cavaloDao.localizaPeloId(frete.getRefCavalo()).getPlaca();
        holder.placaTxtView.setText(placa);

        BigDecimal valorAReceber = frete.getAdmFrete().getFreteLiquidoAReceber();
        holder.totalReceberTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAReceber));

        BigDecimal valorAdiantamento = vinculaAdiantamento(holder, frete);
        BigDecimal valorSaldo = vinculaSaldo(holder, frete);
        configuraImgDeStatusDoRecebimento(holder, valorAReceber, valorAdiantamento, valorSaldo);
    }

    private void configuraImgDeStatusDoRecebimento(ViewHolder holder, BigDecimal valorAReceber, BigDecimal valorAdiantamento, BigDecimal valorSaldo) {
        BigDecimal valorRecebido = valorAdiantamento.add(valorSaldo);
        int compare = valorAReceber.compareTo(valorRecebido);

        if (compare == 0) {
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "done"));
        }
    }

    private BigDecimal vinculaSaldo(ViewHolder holder, Frete frete) {
        RecebimentoDeFrete saldo = recebimentoDao.retornaSaldo(frete.getId());
        BigDecimal valorSaldo;
        try {
            valorSaldo = saldo.getValor();
            holder.saldoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorSaldo));
        } catch (NullPointerException e) {
            e.printStackTrace();
            valorSaldo = BigDecimal.ZERO;
            holder.saldoTxtView.setText("R$ 0.00");
        }
        return valorSaldo;
    }

    private BigDecimal vinculaAdiantamento(ViewHolder holder, Frete frete) {
        RecebimentoDeFrete adiantamento = recebimentoDao.retornaAdiantamento(frete.getId());
        BigDecimal valorAdiantamento;
        try {
            valorAdiantamento = adiantamento.getValor();
            holder.adiantamentoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAdiantamento));
        } catch (NullPointerException e) {
            e.printStackTrace();
            valorAdiantamento = BigDecimal.ZERO;
            holder.adiantamentoTxtView.setText("R$ 0.00");
        }
        return valorAdiantamento;
    }

}

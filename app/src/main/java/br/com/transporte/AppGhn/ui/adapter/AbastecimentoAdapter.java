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

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaAbastecimentoFragment;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.enums.TipoAbastecimento;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class AbastecimentoAdapter extends RecyclerView.Adapter<AbastecimentoAdapter.ViewHolder> {
    private final List<CustosDeAbastecimento> lista;
    private final AreaMotoristaAbastecimentoFragment context;
    private OnItemClickListener onItemClickListener;

    public AbastecimentoAdapter(AreaMotoristaAbastecimentoFragment context, List<CustosDeAbastecimento> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxtView, postoTxtView, totalAbastecimentoTxtView, marcacaoKmTxtView, qntLitrosTxtView, valorLitroTxtView;
        private final ImageView xvImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTxtView = itemView.findViewById(R.id.rec_item_abastecimento_data);
            postoTxtView = itemView.findViewById(R.id.rec_item_abastecimento_posto);
            totalAbastecimentoTxtView = itemView.findViewById(R.id.rec_item_abastecimento_valorTotal);
            marcacaoKmTxtView = itemView.findViewById(R.id.rec_item_abastecimento_marcacaoKm);
            qntLitrosTxtView = itemView.findViewById(R.id.rec_item_abastecimento_qntLitros);
            valorLitroTxtView = itemView.findViewById(R.id.rec_item_abastecimento_precoLitro);
            xvImgView = itemView.findViewById(R.id.rec_item_abastecimentoXV);
        }
    }

    @NonNull
    @Override
    public AbastecimentoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_abastecimento, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustosDeAbastecimento abastecimento = lista.get(position);
        vincula(holder, abastecimento);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(abastecimento.getId()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualiza(List<CustosDeAbastecimento> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, CustosDeAbastecimento abastecimento) {
        holder.dataTxtView.setText(FormataDataUtil.dataParaString(abastecimento.getData()));
        holder.postoTxtView.setText(abastecimento.getPosto());
        holder.totalAbastecimentoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(abastecimento.getValorCusto()));
        holder.marcacaoKmTxtView.setText(FormataNumerosUtil.formataNumero(abastecimento.getMarcacaoKm()));
        holder.qntLitrosTxtView.setText(abastecimento.getQuantidadeLitros().toPlainString());
        holder.valorLitroTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(abastecimento.getValorLitro()));

        if (abastecimento.getTipo() == TipoAbastecimento.TOTAL) {
            holder.xvImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "done"));
        } else {
            holder.xvImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "undone"));
        }
    }

}

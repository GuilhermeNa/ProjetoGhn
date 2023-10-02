package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.enums.TipoAbastecimento;
import br.com.transporte.AppGhn.ui.fragment.areaMotorista.AreaMotoristaAbastecimentoFragment;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;
import br.com.transporte.AppGhn.util.OnItemClickListener_getId;

public class AbastecimentoAdapter extends RecyclerView.Adapter<AbastecimentoAdapter.ViewHolder> {
    private final List<CustosDeAbastecimento> dataSet;
    private final AreaMotoristaAbastecimentoFragment context;
    private OnItemClickListener_getId onItemClickListener;

    public AbastecimentoAdapter(AreaMotoristaAbastecimentoFragment context, List<CustosDeAbastecimento> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dataTxtView, postoTxtView, totalAbastecimentoTxtView,
                marcacaoKmTxtView, qntLitrosTxtView, valorLitroTxtView;
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

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AbastecimentoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_abastecimento, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustosDeAbastecimento abastecimento = dataSet.get(position);
        vincula(holder, abastecimento);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(abastecimento.getId()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull CustosDeAbastecimento abastecimento) {
        holder.dataTxtView.setText(ConverteDataUtil.dataParaString(abastecimento.getData()));
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



    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<CustosDeAbastecimento> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void insere(CustosDeAbastecimento custosDeAbastecimento){
        this.dataSet.add(custosDeAbastecimento);
        notifyItemInserted(getItemCount()-1);
    }

    public void remove(CustosDeAbastecimento custosDeAbastecimento){
        int posicao = -1;
        posicao = this.dataSet.indexOf(custosDeAbastecimento);
        this.dataSet.remove(custosDeAbastecimento);
        notifyItemRemoved(posicao);

    }

}

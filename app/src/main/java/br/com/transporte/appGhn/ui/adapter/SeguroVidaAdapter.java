package br.com.transporte.appGhn.ui.adapter;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.SeguroVidaFragment;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.ImagemUtil;

public class SeguroVidaAdapter extends RecyclerView.Adapter<SeguroVidaAdapter.ViewHolder> {
    public static final int VENCIMENTO_MES = 1;
    public static final int VENCIMENTO_SEMANA = 2;
    public static final int VENCIMENTO_OK = 0;
    public static final String SITUACAO_OK = "situacao_ok";
    public static final String SITUACAO_ATENCAO = "situacao_atencao";
    public static final String SITUACAO_AVISO = "situacao_aviso";
    private int situacaoDoCavalo;
    private final SeguroVidaFragment context;
    private final List<DespesaComSeguroDeVida> dataSet;
    private OnItemClickListener<DespesaComSeguro> onItemClickListener;
    private int posicao;

    public void setOnItemClickListener(OnItemClickListener<DespesaComSeguro> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SeguroVidaAdapter(SeguroVidaFragment context, List<DespesaComSeguroDeVida> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView placaTxtView;
        private final TextView valorTxtView;
        private final TextView dataInicialTxtView;
        private final TextView dataFinalTxtView;
        private final ImageView statusImgView;
        private final LinearLayout descricaoLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_placa);
            valorTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_valor);
            dataInicialTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_data_inicial);
            dataFinalTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_data_final);
            descricaoLayout = itemView.findViewById(R.id.rec_item_despesa_seguros_layout_descricao);
            statusImgView = itemView.findViewById(R.id.rec_item_seguro_status);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(@NonNull ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.visualizaParcelas, Menu.NONE, R.string.visualizar_parcelas);
            menu.add(Menu.NONE, R.id.renovarSeguro, Menu.NONE, R.string.renovar_seguro);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public SeguroVidaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_seguros, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull SeguroVidaAdapter.ViewHolder holder, int position) {
        DespesaComSeguro seguro = dataSet.get(position);
        vincula(holder, seguro);
        configuraListeners(holder, seguro);
    }

    private void configuraListeners(@NonNull ViewHolder holder, DespesaComSeguro seguro) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(seguro));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    private void setPosicao(int posicao){
        this.posicao = posicao;
    }

    //--------------------------------------------
    // -> Vincula                               ||
    //--------------------------------------------

    private void vincula(ViewHolder holder, DespesaComSeguro seguro) {
        if (seguro instanceof DespesaComSeguroDeVida) {
            holder.placaTxtView.setText(R.string.vida_grupo);
        }
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguro.getValorDespesa()));
        holder.dataInicialTxtView.setText(ConverteDataUtil.dataParaString(seguro.getDataInicial()));
        holder.dataFinalTxtView.setText(ConverteDataUtil.dataParaString(seguro.getDataFinal()));
        holder.descricaoLayout.setVisibility(GONE);
        exibeImgDeStatusDoCertificadoParaCadaCavalo(holder, seguro);
    }

    private void exibeImgDeStatusDoCertificadoParaCadaCavalo(ViewHolder holder, DespesaComSeguro seguros) {
        situacaoDoCavalo = VENCIMENTO_OK;
        situacaoDoCavalo = verificaVencimentoDosCertificados(seguros);

        if (situacaoDoCavalo == VENCIMENTO_OK) {
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), SITUACAO_OK));
        } else if (situacaoDoCavalo == VENCIMENTO_MES) {
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), SITUACAO_ATENCAO));
        } else {
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), SITUACAO_AVISO));
        }

    }

    private int verificaVencimentoDosCertificados(@NonNull DespesaComSeguro seguros) {
        LocalDate dataDeHoje = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.todayInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();

        Period periodo = Period.between(dataDeHoje, seguros.getDataFinal());
        int anosEmDias = (periodo.getYears() * 360);
        int mesesEmDias = (periodo.getMonths() * 30);
        int diasAteVencimento = (periodo.getDays() + mesesEmDias + anosEmDias);

        if (diasAteVencimento <= 7) {
            situacaoDoCavalo = VENCIMENTO_SEMANA;
        } else if (diasAteVencimento <= 30 && situacaoDoCavalo == VENCIMENTO_OK) {
            situacaoDoCavalo = VENCIMENTO_MES;
        }
        return situacaoDoCavalo;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<DespesaComSeguroDeVida> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public int getPosicao(){
        return posicao;
    }

}

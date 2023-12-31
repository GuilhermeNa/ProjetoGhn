package br.com.transporte.appGhn.ui.adapter;

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

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.ui.activity.despesaAdm.extensions.BindData;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.SeguroFrotaFragment;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.domain.model.DespesaSeguroFrotaObject;
import br.com.transporte.appGhn.util.ImagemUtil;
import br.com.transporte.appGhn.util.OnItemClickListener_getId;

public class SeguroFrotaAdapter extends RecyclerView.Adapter<SeguroFrotaAdapter.ViewHolder> {
    public static final int SITUACAO_ATENCAO = 1;
    public static final int SITUACAO_AVISO = 2;
    public static final int SITUACAO_OK = 0;
    public static final String DRAWABLE_SITUACAO_OK = "situacao_ok";
    public static final String DRAWABLE_SITUACAO_ATENCAO = "situacao_atencao";
    public static final String DRAWABLE_SITUACAO_AVISO = "situacao_aviso";
    private int situacaoDoCavalo;
    private final List<DespesaSeguroFrotaObject> dataSet;
    private final SeguroFrotaFragment context;
    private OnItemClickListener_getId onItemClickListener;
    private int posicao;

    public SeguroFrotaAdapter(@NonNull SeguroFrotaFragment context, List<DespesaSeguroFrotaObject> lista) {
        this.dataSet = lista;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView campoPlaca, campoValor, campoDataInicial, campoDataFinal, descricaoTxtView;
        private final ImageView statusImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campoPlaca = itemView.findViewById(R.id.rec_item_despesa_seguros_placa);
            campoValor = itemView.findViewById(R.id.rec_item_despesa_seguros_valor);
            campoDataInicial = itemView.findViewById(R.id.rec_item_despesa_seguros_data_inicial);
            campoDataFinal = itemView.findViewById(R.id.rec_item_despesa_seguros_data_final);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_descricao);
            statusImageView = itemView.findViewById(R.id.rec_item_seguro_status);
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
    public SeguroFrotaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_seguros, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull SeguroFrotaAdapter.ViewHolder holder, int position) {
        final DespesaSeguroFrotaObject seguros = dataSet.get(position);
        vincula(holder, seguros);
        configuraListeners(holder, seguros);
    }

    private void configuraListeners(@NonNull ViewHolder holder, DespesaSeguroFrotaObject seguros) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(seguros.getIdSeguro()));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    private void setPosicao(int posicao){
        this.posicao = posicao;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //------------------------------------------------
    // -> Vincula                                   ||
    //------------------------------------------------

    private void vincula(@NonNull ViewHolder holder, @NonNull DespesaSeguroFrotaObject seguros) {
        try {

            BindData.fromString(holder.campoPlaca, seguros.getPlaca());
            BindData.R$fromBigDecimal(holder.campoValor, seguros.getValor());
            BindData.fromLocalDate(holder.campoDataInicial, seguros.getDataInicial());
            BindData.fromLocalDate(holder.campoDataFinal, seguros.getDataFinal());
            holder.descricaoTxtView.setText(R.string.seguro_auto);

        } catch (ObjetoNaoEncontrado e) {
            throw new RuntimeException(e);
        }

        exibeImgDeStatusDoCertificadoParaCadaCavalo(holder, seguros);

    }

    private void exibeImgDeStatusDoCertificadoParaCadaCavalo(ViewHolder holder, DespesaSeguroFrotaObject seguros) {
        situacaoDoCavalo = SITUACAO_OK;
        situacaoDoCavalo = verificaVencimentoDosCertificados(seguros);

        if (situacaoDoCavalo == SITUACAO_OK) {
            holder.statusImageView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_SITUACAO_OK));
        } else if (situacaoDoCavalo == SITUACAO_ATENCAO) {
            holder.statusImageView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_SITUACAO_ATENCAO));
        } else {
            holder.statusImageView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), DRAWABLE_SITUACAO_AVISO));
        }
    }

    private int verificaVencimentoDosCertificados(@NonNull DespesaSeguroFrotaObject seguros) {
        LocalDate dataDeHoje = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.todayInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();

        Period periodo = Period.between(dataDeHoje, seguros.getDataFinal());
        int anosEmDias = (periodo.getYears() * 360);
        int mesesEmDias = (periodo.getMonths() * 30);
        int diasAteVencimento = (periodo.getDays() + mesesEmDias + anosEmDias);

        if (diasAteVencimento <= 7) {
            situacaoDoCavalo = SITUACAO_AVISO;
        } else if (diasAteVencimento <= 30 && situacaoDoCavalo == SITUACAO_OK) {
            situacaoDoCavalo = SITUACAO_ATENCAO;
        }
        return situacaoDoCavalo;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    public int getPosicao(){
        return this.posicao;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<DespesaSeguroFrotaObject> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(DespesaSeguroFrotaObject seguroFrota){
        this.dataSet.add(seguroFrota);
        notifyItemInserted(getItemCount()-1);
    }

    /** @noinspection UnusedAssignment*/
    public void remove(DespesaSeguroFrotaObject seguroFrota) {
        int posicao = -1;
        posicao = this.dataSet.indexOf(seguroFrota);
        this.dataSet.remove(seguroFrota);
        notifyItemRemoved(posicao);
    }

}

package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.abstracts.DespesaComSeguro;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.seguros.seguroVida.SeguroVidaFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class SeguroVidaAdapter extends RecyclerView.Adapter<SeguroVidaAdapter.ViewHolder> {
    public static final int SITUACAO_ATENCAO = 1;
    public static final int SITUACAO_AVISO = 2;
    public static final int SITUACAO_OK = 0;
    private int situacaoDoCavalo;
    private final SeguroVidaFragment context;
    private final List<DespesaComSeguroDeVida> dataSet;
    private OnItemClickListener onItemClickListener;
    private int posicao;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
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
        private final TextView placaTxtView, valorTxtView, dataInicialTxtView, dataFinalTxtView, descricaoTxtView;
        private final ImageView statusImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_placa);
            valorTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_valor);
            dataInicialTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_data_inicial);
            dataFinalTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_data_final);
            descricaoTxtView = itemView.findViewById(R.id.rec_item_despesa_seguros_descricao);
            statusImgView = itemView.findViewById(R.id.rec_item_seguro_status);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.visualizaParcelas, Menu.NONE, "Visualizar Parcelas");
            menu.add(Menu.NONE, R.id.renovarSeguro, Menu.NONE, "Renovar Seguro");
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

    @SuppressLint("NewApi")
    private void vincula(ViewHolder holder, DespesaComSeguro seguro) {
        if (seguro instanceof DespesaComSeguroDeVida) {
            holder.placaTxtView.setText("Vida Grupo");
        }
        holder.valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(seguro.getValorDespesa()));
        holder.dataInicialTxtView.setText(FormataDataUtil.dataParaString(seguro.getDataInicial()));
        holder.dataFinalTxtView.setText(FormataDataUtil.dataParaString(seguro.getDataFinal()));
        holder.descricaoTxtView.setText("Seguro Auto");
        exibeImgDeStatusDoCertificadoParaCadaCavalo(holder, seguro);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void exibeImgDeStatusDoCertificadoParaCadaCavalo(ViewHolder holder, DespesaComSeguro seguros) {
        situacaoDoCavalo = SITUACAO_OK;
        situacaoDoCavalo = verificaVencimentoDosCertificados(seguros);

        if (situacaoDoCavalo == SITUACAO_OK) {
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "situacao_ok"));
        } else if (situacaoDoCavalo == SITUACAO_ATENCAO) {
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "situacao_atencao"));
        } else {
            holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), "situacao_aviso"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int verificaVencimentoDosCertificados(DespesaComSeguro seguros) {
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

    private void setPosicao(int posicao){
        this.posicao = posicao;
    }
    //------------------------------------- Metodos Publicos ---------------------------------------

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

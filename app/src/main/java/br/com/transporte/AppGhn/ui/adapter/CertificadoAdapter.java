package br.com.transporte.AppGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
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
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.ui.fragment.certificados.CertificadosDiretosFragment;
import br.com.transporte.AppGhn.util.ImagemUtil;
import br.com.transporte.AppGhn.util.OnItemClickListener_getId;

public class CertificadoAdapter extends RecyclerView.Adapter<CertificadoAdapter.ViewHolder> {
    public static final int DIAS_MES = 30;
    public static final int DIAS_ANO = 365;
    public static final int DIAS_SEMANA = 7;
    private final CertificadosDiretosFragment context;
    private OnItemClickListener_getId onItemClickListener;
    private final List<Cavalo> dataSet;
    private static final int SITUACAO_OK = 0;
    private static final int SITUACAO_AVISO = 2;
    private static final int SITUACAO_ATENCAO = 1;
    private int situacaoDoCavalo;

    public CertificadoAdapter(List<Cavalo> lista, CertificadosDiretosFragment context) {
        this.dataSet = lista;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView statusImgView, icCaminhaoImgView;
        private final TextView placaTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icCaminhaoImgView = itemView.findViewById(R.id.rec_item_certificados_ic_camimhao);
            statusImgView = itemView.findViewById(R.id.rec_item_certificados_status);
            placaTxtView = itemView.findViewById(R.id.rec_item_certificados_placa);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public CertificadoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_certificados, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull CertificadoAdapter.ViewHolder holder, int position) {
        Cavalo cavalo = dataSet.get(position);
        configuraUi(holder);
        vincula(holder, cavalo);
        configuraListeners(holder, cavalo);
    }

    private void configuraListeners(@NonNull ViewHolder holder, Cavalo cavalo) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(cavalo.getId()));
    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.icCaminhaoImgView.setColorFilter(Color.parseColor("#FFFFFFFF"));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //----------------------------------------------------------------------------
    // -> Vincula                                                               ||
    //----------------------------------------------------------------------------

    private void vincula(@NonNull ViewHolder holder, @NonNull Cavalo cavalo) {
        holder.placaTxtView.setText(cavalo.getPlaca());
        exibeImgDeStatusDoCertificadoParaCadaCavalo(holder, cavalo);
    }

    private void exibeImgDeStatusDoCertificadoParaCadaCavalo(ViewHolder holder, Cavalo cavalo) {
        situacaoDoCavalo = SITUACAO_OK;
        situacaoDoCavalo = verificaVencimentoDosCertificados(cavalo);

        switch (situacaoDoCavalo){
            case SITUACAO_OK:
                setStatusImgView(holder, "situacao_ok");
                break;

            case SITUACAO_ATENCAO:
                setStatusImgView(holder, "situacao_atencao");
                break;

            case SITUACAO_AVISO:
                setStatusImgView(holder, "situacao_aviso");
                break;
        }

    }

    private void setStatusImgView(@NonNull ViewHolder holder, String situacao) {
        holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), situacao));
    }

    private int verificaVencimentoDosCertificados(@NonNull Cavalo cavalo) {
        RoomDespesaCertificadoDao certificadoDao = GhnDataBase.getInstance(context.requireContext()).getRoomDespesaCertificadoDao();
        List<DespesaCertificado> listaTodosCertificadosDoCavalo = certificadoDao.listaPorCavaloId(cavalo.getId());
        int diasAteVencimento = 0;

        List<DespesaCertificado> listaDeCertificadosAtivos = listaTodosCertificadosDoCavalo.stream()
                .filter(DespesaCertificado::isValido)
                .collect(Collectors.toList());

        LocalDate dataDeHoje = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.todayInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();

        for (DespesaCertificado c : listaDeCertificadosAtivos) {
            Period periodo = Period.between(dataDeHoje, c.getDataDeVencimento());
            int anosEmDias = (periodo.getYears() * DIAS_ANO);
            int mesesEmDias = (periodo.getMonths() * DIAS_MES);
            diasAteVencimento = (periodo.getDays() + mesesEmDias + anosEmDias);

            if (diasAteVencimento <= DIAS_SEMANA) {
                situacaoDoCavalo = SITUACAO_AVISO;
            } else if (diasAteVencimento <= DIAS_MES && situacaoDoCavalo == SITUACAO_OK) {
                situacaoDoCavalo = SITUACAO_ATENCAO;
            }

        }

        return situacaoDoCavalo;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Cavalo> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

}

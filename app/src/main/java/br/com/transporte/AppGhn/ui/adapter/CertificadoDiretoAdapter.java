package br.com.transporte.AppGhn.ui.adapter;

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

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.fragment.certificados.CertificadoDiretosDetalhesFragment;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.ImagemUtil;

public class CertificadoDiretoAdapter extends RecyclerView.Adapter<CertificadoDiretoAdapter.ViewHolder> {
    private final CertificadoDiretosDetalhesFragment context;
    private OnItemClickListener onItemClickListener;
    private final List<DespesaCertificado> lista;
    public static final int DIAS_SEMANA = 7;
    public static final int DIAS_MES = 30;

    public CertificadoDiretoAdapter(CertificadoDiretosDetalhesFragment context, List<DespesaCertificado> lista) {
        this.lista = lista;
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView nomeEDataTxtView, dataExpedicaoTxtView, nCertificadoTxtView, valorDocumentoTxtView, dataVencimentoTxtView;
        private final ImageView statusImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeEDataTxtView = itemView.findViewById(R.id.rec_item_certificado_detalhes_nome_e_ano);
            statusImgView = itemView.findViewById(R.id.rec_item_certificado_detalhes_status);
            dataExpedicaoTxtView = itemView.findViewById(R.id.rec_item_certificado_detalhes_data_expedicao);
            nCertificadoTxtView = itemView.findViewById(R.id.rec_item_certificado_detalhes_numero);
            valorDocumentoTxtView = itemView.findViewById(R.id.rec_item_certificado_detalhes_valor);
            dataVencimentoTxtView = itemView.findViewById(R.id.rec_item_certificado_detalhes_data_vencimento);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.renovarCertificado, Menu.NONE, "Renovar Certificado");
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public CertificadoDiretoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_certificado_detalhes, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CertificadoDiretoAdapter.ViewHolder holder, int position) {
        DespesaCertificado certificado = lista.get(position);
        vincula(holder, certificado);
        configuraListeners(holder, certificado);
    }

    private void configuraListeners(@NonNull ViewHolder holder, DespesaCertificado certificado) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(certificado));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vincula(ViewHolder holder, DespesaCertificado certificado) {
        String nomeEData = certificado.getTipoCertificado().getDescricao() + " " + certificado.getAno();

        holder.nomeEDataTxtView.setText(nomeEData);
        holder.dataExpedicaoTxtView.setText(FormataDataUtil.dataParaString(certificado.getDataDeEmissao()));
        holder.nCertificadoTxtView.setText(String.valueOf(certificado.getNumeroDoDocumento()));
        holder.valorDocumentoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(certificado.getValorDespesa()));
        holder.dataVencimentoTxtView.setText(FormataDataUtil.dataParaString(certificado.getDataDeVencimento()));

        configuraImgDeStatusDoCertificado(holder, certificado);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraImgDeStatusDoCertificado(ViewHolder holder, DespesaCertificado certificado) {
        int diasAteVencimento = VencimentoUtil.verificaQuantosDiasFaltam(certificado.getDataDeVencimento());

        if (!certificado.isValido()) {
            setStatusImgView(holder, "nao_precisa_reembolso");
        } else if (diasAteVencimento > DIAS_MES) {
            setStatusImgView(holder, "situacao_ok");
        } else if (diasAteVencimento > DIAS_SEMANA) {
            setStatusImgView(holder, "situacao_atencao");
        } else {
            setStatusImgView(holder, "situacao_aviso");
        }
    }

    private void setStatusImgView(ViewHolder holder, String situacao) {
        holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), situacao));
    }

    private int posicao;

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void atualiza(List<DespesaCertificado> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public int getPosicao() {
        return posicao;
    }

}

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

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.ui.adapter.adaptersUtil.VencimentoUtil;
import br.com.transporte.appGhn.ui.fragment.certificados.CertificadosIndiretosFragment;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.ImagemUtil;
import br.com.transporte.appGhn.util.OnItemClickListener_getId;

public class CertificadoIndiretoAdapter extends RecyclerView.Adapter<CertificadoIndiretoAdapter.ViewHolder> {
    private final CertificadosIndiretosFragment context;
    public OnItemClickListener_getId onItemClickListener;
    private final List<DespesaCertificado> dataSet;
    public static final int DIAS_SEMANA = 7;
    public static final int DIAS_MES = 30;
    private int posicao;

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CertificadoIndiretoAdapter(CertificadosIndiretosFragment context, List<DespesaCertificado> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private final TextView nomeEDataTxtView, dataExpedicaoTxtView, nCertificadoTxtView,
                valorDocumentoTxtView, dataVencimentoTxtView;
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
    public CertificadoIndiretoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_certificado_detalhes, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull CertificadoIndiretoAdapter.ViewHolder holder, int position) {
        DespesaCertificado certificado = dataSet.get(position);
        vincula(holder, certificado);
        configuraListeners(holder, certificado);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void configuraListeners(@NonNull ViewHolder holder, DespesaCertificado certificado) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(certificado.getId()));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    //----------------------------------------------------------------------------
    // -> Vincula                                                               ||
    //----------------------------------------------------------------------------

    private void vincula(@NonNull ViewHolder holder, @NonNull DespesaCertificado certificado) {
        String nomeEData = certificado.getTipoCertificado().getDescricao() + " " + certificado.getAno();
        holder.nomeEDataTxtView.setText(nomeEData);
        holder.dataExpedicaoTxtView.setText(ConverteDataUtil.dataParaString(certificado.getDataDeEmissao()));
        holder.nCertificadoTxtView.setText(String.valueOf(certificado.getNumeroDoDocumento()));
        holder.valorDocumentoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(certificado.getValorDespesa()));
        holder.dataVencimentoTxtView.setText(ConverteDataUtil.dataParaString(certificado.getDataDeVencimento()));

        configuraImgDeStatusDoCertificado(holder, certificado);
    }

    private void configuraImgDeStatusDoCertificado(ViewHolder holder, @NonNull DespesaCertificado certificado) {
        int diasAteVencimento = VencimentoUtil.verificaQuantosDiasFaltam(certificado.getDataDeVencimento());

        if(!certificado.isValido()){
            setStatusImgView(holder, "nao_precisa_reembolso");
        } else if (diasAteVencimento > DIAS_MES) {
            setStatusImgView(holder, "situacao_ok");
        } else if (diasAteVencimento > DIAS_SEMANA) {
            setStatusImgView(holder, "situacao_atencao");
        } else {
            setStatusImgView(holder, "situacao_aviso");
        }
    }

    private void setStatusImgView(@NonNull ViewHolder holder, String situacao) {
        holder.statusImgView.setImageDrawable(ImagemUtil.pegaDrawable(context.requireActivity(), situacao));
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<DespesaCertificado> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public int getPosicao() {
        return posicao;
    }

    public void adiciona(DespesaCertificado certificado){
        this.dataSet.add(certificado);
        notifyItemInserted(getItemCount()-1);
    }

    /** @noinspection UnusedAssignment*/
    public void remove(DespesaCertificado certificado){
        int posicaoRemovida = -1;
        posicaoRemovida = this.dataSet.indexOf(certificado);
        this.dataSet.remove(certificado);
        notifyItemRemoved(posicaoRemovida);
    }

}

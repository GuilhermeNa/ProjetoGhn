package br.com.transporte.AppGhn.ui.fragment.home.frota.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.fragment.home.frota.dialog.AlteraSemiReboqueDoCavalo;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class SemiReboqueAdapter extends RecyclerView.Adapter<SemiReboqueAdapter.ViewHolder> implements AlteraSemiReboqueDoCavalo.DialogAlteraSrCallBack {
    private final Context context;
    private final List<SemiReboque> dataSet;
    private OnItemClickListener onItemClickListener;
    private ViewHolder holder;

    public SemiReboqueAdapter(Context context, List<SemiReboque> lista) {
        this.context = context;
        this.dataSet = lista;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView alteraRefCavaloImgIc;
        private final TextView placaEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alteraRefCavaloImgIc = itemView.findViewById(R.id.rec_item_semireboque_altera);
            placaEdit = itemView.findViewById(R.id.rec_item_semireboque_placa);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public SemiReboqueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_semireboque, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull SemiReboqueAdapter.ViewHolder holder, int position) {
        this.holder = holder;
        SemiReboque sr = dataSet.get(position);
        configuraUi(holder);
        vincula(holder, sr);
        configuraClickListeners(holder, sr);
    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.alteraRefCavaloImgIc.setColorFilter(Color.parseColor("#FFFFFFFF"));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull SemiReboque sr) {
        holder.placaEdit.setText(sr.getPlaca());
    }

    private void configuraClickListeners(@NonNull ViewHolder holder, SemiReboque sr) {
        holder.placaEdit.setOnClickListener(v -> onItemClickListener.onEditaSrClick(sr.getId()));
        holder.alteraRefCavaloImgIc.setOnClickListener(v -> {
            AlteraSemiReboqueDoCavalo dialog = new AlteraSemiReboqueDoCavalo(context, sr);
            dialog.dialogAlteraSrCavalo();
            dialog.setDialogAlteraSrCallBack(this);
        });
    }

    //---------------------------------------- Metodos publicos ------------------------------------

    public void atualiza() {

    }


    //----------------------------------------------------------------------------------------------
    //               Callback retorna resultado do Dialog que altera o Reboque                    ||
    //----------------------------------------------------------------------------------------------

    public void quandoFalhaEmAlterarSr(String txt) {
        MensagemUtil.snackBar(holder.itemView, txt);
    }

    @Override
    public void quandoSucessoEmAlterarSr(SemiReboque reboque) {
        removeItem(reboque);
        onItemClickListener.onAlteraReferenciaDeCavalo(reboque.getId());
    }

    private void removeItem(SemiReboque reboque) {
        int posicao = -1;
        posicao = dataSet.indexOf(reboque);
        dataSet.remove(posicao);
        notifyItemRemoved(posicao);
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface OnItemClickListener {
        // Id do semireboque que foi clicado para realizar alterações
        // será enviado de volta ao Fragment para abrir Formulario modo Edição
        void onEditaSrClick(int idSr);

        // Id do semireboque que teve seu cavalo alterado,
        // será enviado :
        // -> Inner Adapter para aplicar comportamento de remoção da Recycler
        // -> Adapter Pai para atualizar o novo cavalo que está recebendo o reboque
        void onAlteraReferenciaDeCavalo(int reboqueAlteradoId);
    }


}
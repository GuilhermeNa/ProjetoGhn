package br.com.transporte.AppGhn.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.ui.dialog.AlteraSemiReboqueDoCavalo;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class SemiReboqueAdapter extends RecyclerView.Adapter<SemiReboqueAdapter.ViewHolder> {
    private final Context context;
    private final List<SemiReboque> lista;
    private OnItemClickListener onItemClickListener;

    public SemiReboqueAdapter(Context context, List<SemiReboque> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder  {
        private final ImageView alteraRefCavaloImgIc;
        private final TextView placaEdit;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alteraRefCavaloImgIc = itemView.findViewById(R.id.rec_item_semireboque_altera);
            placaEdit = itemView.findViewById(R.id.rec_item_semireboque_placa);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public SemiReboqueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_semireboque, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SemiReboqueAdapter.ViewHolder holder, int position) {
        SemiReboque sr = lista.get(position);
        configuraUi(holder);
        vincula(holder, sr);
        configuraClickListeners(holder, sr);

    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.alteraRefCavaloImgIc.setColorFilter(Color.parseColor("#FFFFFFFF"));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void itemRemovido(int posicaoSrRemovido) {
        notifyItemRemoved(posicaoSrRemovido);
    }

    private void vincula(ViewHolder holder, SemiReboque sr) {
        holder.placaEdit.setText(sr.getPlaca());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraClickListeners(ViewHolder holder, SemiReboque sr) {
        holder.placaEdit.setOnClickListener(v -> onItemClickListener.onItemClick(sr.getId()));

        holder.alteraRefCavaloImgIc.setOnClickListener(v -> {
            AlteraSemiReboqueDoCavalo dialog = new AlteraSemiReboqueDoCavalo(context, sr);
            dialog.dialogAlteraSrCavalo();
            dialog.setDialogAlteraSrCallBack(new AlteraSemiReboqueDoCavalo.DialogAlteraSrCallBack() {
                @Override
                public void quandoFalhaEmAlterarSr(String txt) {
                    MensagemUtil.snackBar(holder.itemView, txt);
                }

                @Override
                public void quandoSucessoEmAlterarSr(int posicaoSrRemovido) {
                    itemRemovido(posicaoSrRemovido);
                }
            });
        });
    }

}

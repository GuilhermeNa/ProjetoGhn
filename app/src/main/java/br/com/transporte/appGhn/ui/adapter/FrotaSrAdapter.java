package br.com.transporte.appGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomCavaloDao;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.tasks.cavalo.LocalizaPeloIdTask;
import br.com.transporte.appGhn.ui.fragment.home.frota.FrotaFragment;

public class FrotaSrAdapter extends RecyclerView.Adapter<FrotaSrAdapter.ViewHolder> {
    private final FrotaFragment context;
    private final List<SemiReboque> dataSet;
    private final RoomCavaloDao cavaloDao;
    private final Handler handler;
    private final ExecutorService executor;

    public FrotaSrAdapter(@NonNull FrotaFragment context, List<SemiReboque> lista) {
        this.context = context;
        this.dataSet = lista;
        cavaloDao = GhnDataBase.getInstance(context.requireContext()).getRoomCavaloDao();
        GhnApplication application = new GhnApplication();
        handler = application.getMainThreadHandler();
        executor = application.getExecutorService();
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView campoPlacaReboque, campoPlacaCavalo;
        private final ImageView campoReboqueImg, campoCavaloImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campoPlacaReboque = itemView.findViewById(R.id.rec_item_sr_txt);
            campoPlacaCavalo = itemView.findViewById(R.id.rec_item_sr_ref_cavalo_txt);
            campoReboqueImg = itemView.findViewById(R.id.rec_item_sr_img);
            campoCavaloImg = itemView.findViewById(R.id.rec_item_sr_ref_cavalo_img);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public FrotaSrAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_sr, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull FrotaSrAdapter.ViewHolder holder, int position) {
        final SemiReboque sr = dataSet.get(position);
        configuraUi(holder);
        vincula(holder, sr);
    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.campoReboqueImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
        holder.campoCavaloImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull SemiReboque sr) {
        LocalizaPeloIdTask localizaCavaloTask = new LocalizaPeloIdTask(executor, handler);
        localizaCavaloTask.solicitaBusca(cavaloDao, sr.getRefCavaloId(),
                cavalo -> {
                    if(cavalo != null){
                        holder.campoPlacaCavalo.setText(cavalo.getPlaca());
                        holder.campoPlacaReboque.setText(sr.getPlaca());
                    }
                });
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<SemiReboque> listaFiltrada) {
        this.dataSet.clear();
        this.dataSet.addAll(listaFiltrada);
        notifyDataSetChanged();
    }

}

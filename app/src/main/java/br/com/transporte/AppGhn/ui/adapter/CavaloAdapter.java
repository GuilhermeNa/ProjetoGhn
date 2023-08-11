package br.com.transporte.AppGhn.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.fragment.home.FrotaFragment;

public class CavaloAdapter extends RecyclerView.Adapter<CavaloAdapter.ViewHolder> {
    private final List<Cavalo> lista;
    private final FrotaFragment context;
    private OnItemClickListener onItemClickListener;
    private boolean janelaFechada = true;

    public CavaloAdapter(FrotaFragment context, List<Cavalo> lista) {
        this.lista = lista;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView placaTxt, motorista;
        private final LinearLayout subMenu;
        private final ImageView srImg, cavaloImg, motoristaImg, seta;
        private final Button btnNovoSr;
        private final RecyclerView recyclerFilha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxt = itemView.findViewById(R.id.rec_item_frota_placa);
            subMenu = itemView.findViewById(R.id.rec_item_frota_submenu);
            seta = itemView.findViewById(R.id.rec_item_frota_seta);
            motorista = itemView.findViewById(R.id.rec_item_frota_motorista_txt);
            srImg = itemView.findViewById(R.id.rec_item_frota_sr_img);
            cavaloImg = itemView.findViewById(R.id.rec_item_frota_cavalo_img);
            motoristaImg = itemView.findViewById(R.id.rec_item_frota_motorista_img);
            btnNovoSr = itemView.findViewById(R.id.rec_item_frota_btn);
            recyclerFilha = itemView.findViewById(R.id.rec_item_frota_recycler);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.defineMotorista, Menu.NONE, "Definir Motorista");
        }
    }

    @NonNull
    @Override
    public CavaloAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_frota, parent, false);
        return new ViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull CavaloAdapter.ViewHolder holder, int position) {
        Cavalo cavalo = lista.get(position);
        configuraUi(holder);
        configuraAnimacaoSubListaSr(holder);
        vincula(holder, cavalo);
        configuraListeners(holder, cavalo);
        configuraRecycler(holder, cavalo);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public int posicao;

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }

    public void atualiza(List<Cavalo> lista) {
        this.lista.clear();
        this.lista.addAll(lista);
        notifyDataSetChanged();
    }

    private void vincula(ViewHolder holder, Cavalo cavalo) {
        holder.placaTxt.setText(cavalo.getPlaca());

        try {
            holder.motorista.setText(cavalo.getMotorista().getNome());
        } catch (NullPointerException e) {
            e.printStackTrace();
            e.getMessage();
            holder.motorista.setText("Não há motorista vinculado a este Cavalo");
        }
    }

    private void configuraUi(ViewHolder holder) {
        holder.srImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
        holder.cavaloImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
        holder.motoristaImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
    }

    private void configuraListeners(ViewHolder holder, Cavalo cavalo) {
        holder.btnNovoSr.setOnClickListener(v -> onItemClickListener.onNovoSrClick(cavalo.getId()));
        holder.placaTxt.setOnClickListener(v -> onItemClickListener.onEditaCavaloClick(cavalo.getId()));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    private void configuraAnimacaoSubListaSr(ViewHolder holder) {
        Animation animationAbertura = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.seta_abertura);
        Animation animationFechamento = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.seta_fechamento);

        holder.seta.setOnClickListener(v -> {
            if (janelaFechada) {
                holder.seta.startAnimation(animationAbertura);
                holder.subMenu.setVisibility(View.VISIBLE);
                janelaFechada = false;
            } else {
                holder.seta.startAnimation(animationFechamento);
                holder.subMenu.setVisibility(View.GONE);
                janelaFechada = true;
            }
        });

        if (holder.subMenu.getVisibility() == View.VISIBLE) {
            holder.seta.startAnimation(animationAbertura);
        }
    }

    public interface OnItemClickListener {
        void onEditaCavaloClick(int idCavalo);

        void onNovoSrClick(int idCavalo);

        void onEditaSrClick(int idSr, int idCavalo);
    }

    private void configuraRecycler(ViewHolder holder, Cavalo cavalo) {
        SemiReboqueAdapter adapter = new SemiReboqueAdapter(context.getContext(), cavalo.getSemiReboque());
        holder.recyclerFilha.setAdapter(adapter);

        Drawable divider = ContextCompat.getDrawable(context.requireContext(), R.drawable.divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context.requireContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(divider);
        holder.recyclerFilha.addItemDecoration(itemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getContext());
        holder.recyclerFilha.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener((idSr) -> onItemClickListener.onEditaSrClick((Integer) idSr, cavalo.getId()));
    }

}



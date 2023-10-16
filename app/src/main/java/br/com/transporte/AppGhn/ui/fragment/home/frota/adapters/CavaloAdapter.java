package br.com.transporte.AppGhn.ui.fragment.home.frota.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.filtros.FiltraMotorista;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.ui.fragment.home.frota.FrotaFragment;

public class CavaloAdapter extends RecyclerView.Adapter<CavaloAdapter.ViewHolder> {
    public static final String MOTORISTA_NULO_MSG = "Não há motorista vinculado a este Cavalo";
    private final List<Cavalo> copiaDataSet_cavalo;
    private final List<Motorista> copiaDataSet_motorista;
    private final List<SemiReboque> copiaDataSet_reboque;
    private final FrotaFragment context;
    private OnItemClickListener onItemClickListener;
    private boolean janelaFechada = true;
    private int posicao;

    public CavaloAdapter(@NonNull FrotaFragment context) {
        this.context = context;
        copiaDataSet_cavalo = new ArrayList<>();
        copiaDataSet_motorista = new ArrayList<>();
        copiaDataSet_reboque = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onCLickEditaCavalo(Long cavaloId);

        void onClickAdicionaReboque(Long cavaloId);

        // Id do semireboque que foi clicado para realizar alterações
        // será enviado de volta ao Fragment para abrir Formulario modo Edição
        void onClickEditaReboque(Long reboqueId, Long cavaloId);
    }

    public void atualizaDataSet_motorista(List<Motorista> lista){
       this.copiaDataSet_motorista.clear();
       this.copiaDataSet_motorista.addAll(lista);
    }

    public void atualizaDataSet_reboque(List<SemiReboque> lista) {
        this.copiaDataSet_reboque.clear();
        this.copiaDataSet_reboque.addAll(lista);
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView placaTxt, motorista;
        private final LinearLayout subMenu;
        private final ImageView srImg, cavaloImg, motoristaImg, seta;
        private final Button btnNovoSr;
        protected final RecyclerView recyclerFilha;

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
        public void onCreateContextMenu(@NonNull ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.defineMotorista, Menu.NONE, "Definir Motorista");
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public CavaloAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_frota, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull CavaloAdapter.ViewHolder holder, int position) {
        Cavalo cavalo = copiaDataSet_cavalo.get(position);
        configuraUi(holder);
        configuraAnimacaoSubListaSr(holder);
        vincula(holder, cavalo);
        configuraListeners(holder, cavalo);
        configuraInnerAdapter(holder, cavalo);
    }

    @Override
    public int getItemCount() {
        return copiaDataSet_cavalo.size();
    }

    private void configuraAnimacaoSubListaSr(@NonNull ViewHolder holder) {
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

    private void configuraListeners(@NonNull ViewHolder holder, Cavalo cavalo) {
        holder.btnNovoSr.setOnClickListener(v -> onItemClickListener.onClickAdicionaReboque(cavalo.getId()));
        holder.placaTxt.setOnClickListener(v -> onItemClickListener.onCLickEditaCavalo(cavalo.getId()));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.srImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
        holder.cavaloImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
        holder.motoristaImg.setColorFilter(Color.parseColor("#FFFFFFFF"));
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    //----------------------------------------------
    // -> Vincula                                 ||
    //----------------------------------------------
    private void vincula(@NonNull ViewHolder holder, @NonNull Cavalo cavalo) {
        String motoristaString = tentaPegarMotorista(cavalo);
        holder.motorista.setText(motoristaString);
        holder.placaTxt.setText(cavalo.getPlaca());
    }

    private String tentaPegarMotorista(@NonNull Cavalo cavalo) {
        String motoristaString;
        try {
            motoristaString = FiltraMotorista.localizaPeloId(copiaDataSet_motorista, cavalo.getRefMotoristaId()).getNome();
        } catch (NullPointerException e) {
            e.printStackTrace();
            motoristaString = MOTORISTA_NULO_MSG;
        }
        return motoristaString;
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    public int getPosicao() {
        return posicao;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Cavalo> lista) {
        this.copiaDataSet_cavalo.clear();
        this.copiaDataSet_cavalo.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(Cavalo cavalo) {
        this.copiaDataSet_cavalo.add(cavalo);
        notifyItemInserted(getItemCount() - 1);
    }

    public void remove(Cavalo cavalo) {
        int posicao = -1;
        posicao = this.copiaDataSet_cavalo.indexOf(cavalo);
        this.copiaDataSet_cavalo.remove(cavalo);
        notifyItemRemoved(posicao);
    }

    //----------------------------------------------------------------------------------------------
    //                                           InnerAdapter                                     ||
    //----------------------------------------------------------------------------------------------

    private void configuraInnerAdapter(ViewHolder holder, Cavalo cavalo) {
        CavaloAdapterInnerAdapterHelper innerAdapter = new CavaloAdapterInnerAdapterHelper(context, copiaDataSet_reboque, copiaDataSet_cavalo);
        innerAdapter.configuraRecycler(holder, cavalo);
        innerAdapter.setCallbackCavaloInnerAdapter(new CavaloAdapterInnerAdapterHelper.InterfaceCavaloInnerAdapter() {
            @Override
            public void solicitaAlteracao_clickEmEditarReboque(Long reboqueId, Long cavaloId) {
                onItemClickListener.onClickEditaReboque(reboqueId, cavaloId);
            }

            @Override
            public void solicitaAlteracao_mudaReferenciaDeCavalo(Long reboqueId) {
                notifyDataSetChanged();
            }
        });
    }

}




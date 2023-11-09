package br.com.transporte.appGhn.ui.fragment.home.frota.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.filtros.FiltraMotorista;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.ui.fragment.home.frota.FrotaFragment;

public class CavaloAdapter extends RecyclerView.Adapter<CavaloAdapter.ViewHolder> {
    public static final String MOTORISTA_NULO_MSG = "Não há motorista vinculado a este Cavalo";
    private final List<Cavalo> copiaDataSet_cavalo;
    private List<Motorista> copiaDataSet_motorista;
    private final List<SemiReboque> copiaDataSet_reboque;
    private final FrotaFragment context;
    private OnItemClickListener onItemClickListener;
    private int posicao;
    private final List<Integer> holdersSolicitados = new ArrayList<>();
    private CavaloAdapterInnerAdapterHelper innerAdapter;

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

    public void atualizaDataSet_motorista(final List<Motorista> lista) {
        this.copiaDataSet_motorista = lista;
    }

    public void atualizaDataSet_reboque(List<SemiReboque> lista) {
        this.copiaDataSet_reboque.clear();
        this.copiaDataSet_reboque.addAll(lista);
    }

    public int getPosicao() {
        return posicao;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(final List<Cavalo> lista) {
        this.copiaDataSet_cavalo.clear();
        this.copiaDataSet_cavalo.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(Cavalo cavalo) {
        this.copiaDataSet_cavalo.add(cavalo);
        notifyItemInserted(getItemCount() - 1);
    }

    /** @noinspection UnusedAssignment*/
    public void remove(Cavalo cavalo) {
        int posicao = -1;
        posicao = this.copiaDataSet_cavalo.indexOf(cavalo);
        this.copiaDataSet_cavalo.remove(cavalo);
        notifyItemRemoved(posicao);
    }

    public void atualizaInner() {
        if (innerAdapter != null) {
            innerAdapter.atualizaAdapter(copiaDataSet_reboque);
            notifyItemChanged(posicao);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public CavaloAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_frota, parent, false);
        return new ViewHolder(viewCriada);
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
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull CavaloAdapter.ViewHolder holder, int position) {
        final Cavalo cavalo = copiaDataSet_cavalo.get(position);
        configuraUi(holder);
        vincula(holder, cavalo);
        configuraListeners(holder, cavalo);
        configuraInnerAdapter(holder, cavalo);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return copiaDataSet_cavalo.size();
    }

    private void configuraListeners(@NonNull ViewHolder holder, Cavalo cavalo) {
        holder.btnNovoSr.setOnClickListener(v -> {
            onItemClickListener.onClickAdicionaReboque(cavalo.getId());
            setPosicao(holder.getAdapterPosition());
        });
        holder.placaTxt.setOnClickListener(v -> {
            onItemClickListener.onCLickEditaCavalo(cavalo.getId());
            setPosicao(holder.getAdapterPosition());
        });
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

    private void vincula(@NonNull ViewHolder holder, @NonNull Cavalo cavalo) {
        final String motoristaString = tentaPegarMotorista(cavalo);
        holder.motorista.setText(motoristaString);
        holder.placaTxt.setText(cavalo.getPlaca());
    }

    private String tentaPegarMotorista(@NonNull Cavalo cavalo) {
        String motoristaString;
        try {
            motoristaString = FiltraMotorista.localizaPeloId(copiaDataSet_motorista, cavalo.getRefMotoristaId()).toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
            motoristaString = MOTORISTA_NULO_MSG;
        }
        return motoristaString;
    }

    //----------------------------------------------------------------------------------------------
    //                                           InnerAdapter                                     ||
    //----------------------------------------------------------------------------------------------

    private void configuraInnerAdapter(ViewHolder holder, Cavalo cavalo) {
        innerAdapter = new CavaloAdapterInnerAdapterHelper(context, copiaDataSet_reboque, copiaDataSet_cavalo);
        innerAdapter.configuraRecycler(holder, cavalo);
        innerAdapter.setCallbackCavaloInnerAdapter(
                new CavaloAdapterInnerAdapterHelper.InterfaceCavaloInnerAdapter() {
                    @Override
                    public void solicitaAlteracao_clickEmEditarReboque(Long reboqueId, Long cavaloId) {
                        onItemClickListener.onClickEditaReboque(reboqueId, cavaloId);
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void solicitaAlteracao_mudaReferenciaDeCavalo(Long reboqueId) {
                        notifyDataSetChanged();
                    }
                });
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        final Animation animationAbertura = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.seta_abertura);
        configuraComportamentoQueMantemARecyclerQueFoiAbertaPeloUsuarioDeFatoVisivel(holder);
        configuraSetaParaQueAcompanheOSubMenuCorretamenteAoSofrerReAtach(holder, animationAbertura);
        configuraListenerDeAberturaEFechamento(holder, animationAbertura);
    }

    private void configuraComportamentoQueMantemARecyclerQueFoiAbertaPeloUsuarioDeFatoVisivel(@NonNull final ViewHolder holder) {
        // Método concerta o bug que fazia com que a recycler abrisse
        // outros itens que não foram efetivamente selecionados pelo usuario
        if (holdersSolicitados.contains(holder.getAdapterPosition())) {
            if (holder.subMenu.getVisibility() == GONE) {
                holder.subMenu.setVisibility(VISIBLE);
            }
        } else {
            if (holder.subMenu.getVisibility() == VISIBLE) {
                holder.subMenu.setVisibility(GONE);
            }
        }
    }

    private void configuraSetaParaQueAcompanheOSubMenuCorretamenteAoSofrerReAtach(
            @NonNull final ViewHolder holder,
            final Animation animationAbertura
    ) {
        if (holder.subMenu.getVisibility() == VISIBLE) {
            holder.seta.startAnimation(animationAbertura);
        }
    }

    private void configuraListenerDeAberturaEFechamento(
            @NonNull final ViewHolder holder,
            final Animation animationAbertura
    ) {
        final Animation animationFechamento =
                AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.seta_fechamento);

        holder.seta.setOnClickListener(v -> {
            if (holder.subMenu.getVisibility() == GONE) {
                abreSubMenu(holder, animationAbertura);
                holdersSolicitados.add(holder.getAdapterPosition());
            } else {
                fechaSubMenu(holder, animationFechamento);
                removeHolderDaListaDeSolicitadosPeloUsuario(holder);
            }
        });
    }

    private void removeHolderDaListaDeSolicitadosPeloUsuario(@NonNull ViewHolder holder) {
        if (holdersSolicitados.contains(holder.getAdapterPosition())) {
            int posicao = holdersSolicitados.indexOf(holder.getAdapterPosition());
            holdersSolicitados.remove(posicao);
        }
    }

    private static void fechaSubMenu(
            @NonNull final ViewHolder holder,
            final Animation animationFechamento
    ) {
        holder.seta.startAnimation(animationFechamento);
        holder.subMenu.setVisibility(GONE);
    }

    private static void abreSubMenu(
            @NonNull final ViewHolder holder,
            final Animation animationAbertura
    ) {
        holder.seta.startAnimation(animationAbertura);
        holder.subMenu.setVisibility(VISIBLE);
    }


}




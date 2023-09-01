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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.ui.fragment.home.frota.FrotaFragment;

public class CavaloAdapter extends RecyclerView.Adapter<CavaloAdapter.ViewHolder> implements CavaloAdapterInnerAdapterHelper.InterfaceCavaloInnerAdapter {
    public static final String MOTORISTA_NULO_MSG = "Não há motorista vinculado a este Cavalo";
    private final List<Cavalo> dataSet;
    private final FrotaFragment context;
    private OnItemClickListener onItemClickListener;
    private RoomMotoristaDao motoristaDao;
    private RoomSemiReboqueDao reboqueDao;
    private boolean janelaFechada = true;
    private int posicao;
    private CavaloAdapterInnerAdapterHelper innerAdapter;

    public CavaloAdapter(@NonNull FrotaFragment context, List<Cavalo> lista) {
        this.dataSet = lista;
        this.context = context;
        motoristaDao = GhnDataBase.getInstance(context.requireContext()).getRoomMotoristaDao();
        reboqueDao = GhnDataBase.getInstance(context.requireContext()).getRoomReboqueDao();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        Cavalo cavalo = dataSet.get(position);
        configuraUi(holder);
        configuraAnimacaoSubListaSr(holder);
        vincula(holder, cavalo);
        configuraListeners(holder, cavalo);
        configuraInnerAdapter(holder, cavalo);
    }

    private void configuraInnerAdapter(ViewHolder holder, Cavalo cavalo) {
        innerAdapter = new CavaloAdapterInnerAdapterHelper(context);
        innerAdapter.configuraRecycler(holder, cavalo);
        innerAdapter.setCallbackCavaloInnerAdapter(this);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
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
            motoristaString = motoristaDao.localizaPeloId(cavalo.getRefMotoristaId()).getNome();
        } catch (NullPointerException e) {
            e.printStackTrace();
            motoristaString = MOTORISTA_NULO_MSG;
        }
        return motoristaString;
    }

    //--------------------------------------------------
    // -> Configura Recycler SemiReboque              ||
    //--------------------------------------------------

    /*private void configuraRecycler(@NonNull ViewHolder holder, @NonNull Cavalo cavalo) {
        SemiReboqueAdapter adapter = configuraAdapter(holder, cavalo);
        configuraItemDecoration(holder);
        adapter.setOnItemClickListener((idSr) -> onItemClickListener.onEditaSrClick((Integer) idSr, cavalo.getId()));
    }

    private void configuraItemDecoration(@NonNull ViewHolder holder) {
        Drawable divider = ContextCompat.getDrawable(context.requireContext(), R.drawable.divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context.requireContext(), DividerItemDecoration.VERTICAL);
        if (divider != null) itemDecoration.setDrawable(divider);
        holder.recyclerFilha.addItemDecoration(itemDecoration);
    }
    @NonNull
    private SemiReboqueAdapter configuraAdapter(@NonNull ViewHolder holder, @NonNull Cavalo cavalo) {
        List<SemiReboque> listaSrPorCavalo = semiReboqueDao.listaPorCavaloId(cavalo.getId());
        SemiReboqueAdapter adapter = new SemiReboqueAdapter(context.getContext(), listaSrPorCavalo);
        holder.recyclerFilha.setAdapter(adapter);
        return adapter;
    }*/

    //------------------------------------- Metodos Publicos ---------------------------------------

    public int getPosicao() {
        return posicao;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Cavalo> lista) {
        this.dataSet.clear();
        this.dataSet.addAll(lista);
        notifyDataSetChanged();
    }

    public void adiciona(Cavalo cavalo) {
        this.dataSet.add(cavalo);
        notifyItemInserted(getItemCount() - 1);
    }

    public void remove(Cavalo cavalo) {
        int posicao = -1;
        posicao = this.dataSet.indexOf(cavalo);
        this.dataSet.remove(cavalo);
        notifyItemRemoved(posicao);
    }

    //----------------------------------------------------------------------------------------------
    //                       Callback -> CavaloAdapterInnerAdapterHelper                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void solicitaAlteracao_clickEmEditarReboque(int reboqueId, int cavaloId) {
        onItemClickListener.onClickEditaReboque(reboqueId, cavaloId);
    }

    @Override
    public void solicitaAlteracao_mudaReferenciaDeCavalo(int reboqueId) {
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                        Interface                                           ||
    //----------------------------------------------------------------------------------------------

    public interface OnItemClickListener {
        void onCLickEditaCavalo(Integer cavaloId);

        void onClickAdicionaReboque(Integer cavaloId);

        // Id do semireboque que foi clicado para realizar alterações
        // será enviado de volta ao Fragment para abrir Formulario modo Edição
        void onClickEditaReboque(Integer reboqueId, Integer cavaloId);
    }

}




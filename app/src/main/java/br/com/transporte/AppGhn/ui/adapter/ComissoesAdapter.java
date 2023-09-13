package br.com.transporte.AppGhn.ui.adapter;

import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.filtros.FiltraAdiantamento;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesFragment;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.OnItemClickListenerNew;

public class ComissoesAdapter extends RecyclerView.Adapter<ComissoesAdapter.ViewHolder> {
    private BigDecimal comissaoEmAberto, adiantamentosADescontar,
            reembolsoDevidoAoMotorista, comissaoJaPaga;
    private final List<Cavalo> lista;
    private final ComissoesFragment context;
    private OnItemClickListenerNew onItemClickListener;
    private LocalDate dataInicial, dataFinal;
    public int posicao;

    public ComissoesAdapter(List<Cavalo> lista, ComissoesFragment context, LocalDate dataInicial, LocalDate dataFinal) {
        this.lista = lista;
        this.context = context;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public void setOnItemClickListener(OnItemClickListenerNew onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView placaTxtView, nomeTxtView, comissaoTotalDevidaAoMotoristaTxtView, adiantamentoADescontarTxtView,
                reembolsoDevidoAoMotoristaTxtView, comissaoPagaTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placaTxtView = itemView.findViewById(R.id.rec_item_salarios_placa);
            nomeTxtView = itemView.findViewById(R.id.rec_item_salarios_nome);
            comissaoTotalDevidaAoMotoristaTxtView = itemView.findViewById(R.id.rec_item_salarios_total);
            adiantamentoADescontarTxtView = itemView.findViewById(R.id.rec_item_salarios_descontos);
            reembolsoDevidoAoMotoristaTxtView = itemView.findViewById(R.id.rec_item_salarios_reembolso);
            comissaoPagaTxtView = itemView.findViewById(R.id.rec_item_salarios_pago);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(@NonNull ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.concedeAdiantamento, Menu.NONE, "Conceder Adiantamento");
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ComissoesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_comissoes, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull ComissoesAdapter.ViewHolder holder, int position) {
        Cavalo cavalo = lista.get(position);
        calculaValoresParaExibir(cavalo);
        vincula(holder, cavalo);
        configuraListeners(holder, cavalo);
    }

    private void configuraListeners(@NonNull ViewHolder holder, Cavalo cavalo) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(cavalo.getId()));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    private void calculaValoresParaExibir(@NonNull Cavalo cavalo) {
        CalculaValores.dataInicial = this.dataInicial;
        CalculaValores.dataFinal = this.dataFinal;

        comissaoEmAberto = CalculaValores.getComissaoEmAberto(context.requireContext(), cavalo.getId());
        comissaoJaPaga = CalculaValores.getComissaoJaPaga(context.requireContext(), cavalo.getId());
        adiantamentosADescontar = CalculaValores.getAdiantamentosADescontar(context.requireContext(), cavalo.getId());
        reembolsoDevidoAoMotorista = CalculaValores.reembolsoDevidoAoMotorista(context.requireContext(), cavalo.getId());
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull Cavalo cavalo) {
        holder.placaTxtView.setText(cavalo.getPlaca());
        holder.comissaoTotalDevidaAoMotoristaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoEmAberto));
        holder.adiantamentoADescontarTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamentosADescontar));
        holder.reembolsoDevidoAoMotoristaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(reembolsoDevidoAoMotorista));
        holder.comissaoPagaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoJaPaga));

        try {
            RoomMotoristaDao motoristaDao = GhnDataBase.getInstance(context.requireContext()).getRoomMotoristaDao();
            String nome = motoristaDao.localizaPeloId(cavalo.getRefMotoristaId()).getNome();
            holder.nomeTxtView.setText(nome);
        } catch (NullPointerException e) {
            e.printStackTrace();
            holder.nomeTxtView.setText("-");
        }
    }

    //------------------------------------- Metodos Publicos ---------------------------------------

    public int getPosicao() {
        return posicao;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(List<Cavalo> lista, LocalDate dataInicial, LocalDate dataFinal) {
        this.lista.clear();
        this.lista.addAll(lista);
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListaCavalos(List<Cavalo> listaFiltrada) {
        this.lista.clear();
        this.lista.addAll(listaFiltrada);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                          Inner Class                                       ||
    //----------------------------------------------------------------------------------------------

    private static class CalculaValores {
        private static LocalDate dataInicial;
        private static LocalDate dataFinal;
        private static GhnDataBase database;
        private static RoomFreteDao freteDao;

        private static BigDecimal getComissaoEmAberto(Context context, Long cavaloId) {
            database = GhnDataBase.getInstance(context);
            freteDao = database.getRoomFreteDao();

            List<Frete> dataSet = FiltraFrete.listaPorCavaloId(freteDao.todos(), cavaloId);
            dataSet = FiltraFrete.listaPorData(dataSet, dataInicial, dataFinal);
            dataSet = FiltraFrete.listaPorStatusDePagamentoDaComissao(dataSet, false);
            return CalculoUtil.somaComissao(dataSet);
        }

        private static BigDecimal getComissaoJaPaga(Context context, Long cavaloId) {
            database = GhnDataBase.getInstance(context);
            freteDao = database.getRoomFreteDao();

            List<Frete> dataSet = FiltraFrete.listaPorCavaloId(freteDao.todos(), cavaloId);
            dataSet = FiltraFrete.listaPorData(dataSet, dataInicial, dataFinal);
            dataSet = FiltraFrete.listaPorStatusDePagamentoDaComissao(dataSet, true);
            return CalculoUtil.somaComissao(dataSet);
        }

        private static BigDecimal getAdiantamentosADescontar(Context context, Long cavaloId) {
            database = GhnDataBase.getInstance(context);
            RoomAdiantamentoDao adiantamentoDao = database.getRoomAdiantamentoDao();

            List<Adiantamento> dataSet = FiltraAdiantamento.listaPorCavaloId(adiantamentoDao.todos(), cavaloId);
            return CalculoUtil.somaAdiantamentoPorStatus(dataSet, false);
        }

        private static BigDecimal reembolsoDevidoAoMotorista(Context context, Long cavaloId) {
            database = GhnDataBase.getInstance(context);
            RoomCustosPercursoDao custosPercursoDao = database.getRoomCustosPercursoDao();

            List<CustosDePercurso> dataSet = FiltraCustosPercurso.listaPorCavaloId(custosPercursoDao.todos(), cavaloId);
            dataSet = FiltraCustosPercurso.listaPorTipo(dataSet, REEMBOLSAVEL_EM_ABERTO);
            return CalculoUtil.somaCustosDePercurso(dataSet);
        }
    }

}

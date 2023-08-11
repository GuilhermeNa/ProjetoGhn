package br.com.transporte.AppGhn.ui.adapter;

import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.AppGhn.ui.adapter.listener.OnItemClickListener;
import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesFragment;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class ComissoesAdapter extends RecyclerView.Adapter<ComissoesAdapter.ViewHolder> {
    private final List<Cavalo> lista;
    private final ComissoesFragment context;
    private final AdiantamentoDAO adiantamentoDao;
    private final CustosDePercursoDAO custosDePercursoDao;
    private OnItemClickListener onItemClickListener;
    private LocalDate dataInicial, dataFinal;
    private final FreteDAO freteDao;
    private BigDecimal comissaoDevidaAoMotorista, adiantamentosADescontar, reembolsoDevidoAoMotorista, comissaoJaPagaAoMotorista;

    public ComissoesAdapter(List<Cavalo> lista, ComissoesFragment context, LocalDate dataInicial, LocalDate dataFinal) {
        this.lista = lista;
        this.context = context;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        freteDao = new FreteDAO();
        adiantamentoDao = new AdiantamentoDAO();
        custosDePercursoDao = new CustosDePercursoDAO();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView placaTxtView, nomeTxtView, comissaoTotalDevidaAoMotoristaTxtView, adiantamentoADescontarTxtView,
                reembolsoDevidoAoMotoristaTxtView, comissaoPagaTxtView;

        @RequiresApi(api = Build.VERSION_CODES.N)
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
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.concedeAdiantamento, Menu.NONE, "Conceder Adiantamento");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public ComissoesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context.getContext()).inflate(R.layout.recycler_item_comissoes, parent, false);
        return new ViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ComissoesAdapter.ViewHolder holder, int position) {
        Cavalo cavalo = lista.get(position);
        calculaValoresParaExibir(cavalo);
        vincula(holder, cavalo);
        configuraListeners(holder, cavalo);
    }

    private void configuraListeners(@NonNull ViewHolder holder, Cavalo cavalo) {
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(cavalo));
        holder.itemView.setOnLongClickListener(v -> {
            setPosicao(holder.getAdapterPosition());
            return false;
        });
    }

    public int posicao;

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }

    public void atualiza(List<Cavalo> lista, LocalDate dataInicial, LocalDate dataFinal) {
        this.lista.clear();
        this.lista.addAll(lista);
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setListaCavalos(List<Cavalo> listaFiltrada) {
        this.lista.clear();
        this.lista.addAll(listaFiltrada);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calculaValoresParaExibir(Cavalo cavalo) {
        List<Frete> listaFretePorPlacaEData = freteDao.listaFiltradaPorCavaloEData(cavalo.getId(), dataInicial, dataFinal);
        List<Adiantamento> listaAdiantamentoPorPlaca = adiantamentoDao.listaPorCavalo(cavalo.getId());
        List<CustosDePercurso> listaCustosPorPlaca = custosDePercursoDao.listaPorCavalo(cavalo.getId());

        comissaoDevidaAoMotorista = listaFretePorPlacaEData.stream()
                .map(Frete::getAdmFrete)
                .filter(admFrete -> !admFrete.isComissaoJaFoiPaga())
                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        adiantamentosADescontar = listaAdiantamentoPorPlaca.stream()
                .filter(adiantamento -> !adiantamento.isAdiantamentoJaFoiPago())
                .map(Adiantamento::restaReembolsar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        reembolsoDevidoAoMotorista = listaCustosPorPlaca.stream()
                .filter(custosDePercurso -> custosDePercurso.getTipo() == TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO)
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        comissaoJaPagaAoMotorista = listaFretePorPlacaEData.stream()
                .map(Frete::getAdmFrete)
                .filter(Frete.AdmFinanceiroFrete::isComissaoJaFoiPaga)
                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void vincula(ViewHolder holder, Cavalo cavalo) {
        holder.placaTxtView.setText(cavalo.getPlaca());
        holder.comissaoTotalDevidaAoMotoristaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoDevidaAoMotorista));
        holder.adiantamentoADescontarTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamentosADescontar));
        holder.reembolsoDevidoAoMotoristaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(reembolsoDevidoAoMotorista));
        holder.comissaoPagaTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoJaPagaAoMotorista));

        try {
            holder.nomeTxtView.setText(cavalo.getMotorista().getNome());
        } catch (NullPointerException e) {
            e.printStackTrace();
            holder.nomeTxtView.setText("-");
        }
    }
}

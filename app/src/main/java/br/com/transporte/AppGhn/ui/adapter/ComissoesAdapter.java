package br.com.transporte.AppGhn.ui.adapter;

import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.filtros.FiltraAdiantamento;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.filtros.FiltraMotorista;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesEmAbertoFragment;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.OnItemClickListener_getId;

public class ComissoesAdapter extends RecyclerView.Adapter<ComissoesAdapter.ViewHolder> {
    private final List<Cavalo> dataSet_cavalo;
    private final List<Motorista> dataSet_motorista;
    private final List<Frete> dataSet_frete;
    private final List<Adiantamento> dataSet_adiantamento;
    private final List<CustosDePercurso> dataSet_custoPercurso;
    private final ComissoesEmAbertoFragment context;
    private OnItemClickListener_getId onItemClickListener;
    public int posicao;

    public ComissoesAdapter(
            ComissoesEmAbertoFragment context,
            List<Cavalo> lista,
            List<Motorista> listaMotorista,
            List<Frete> listaFrete,
            List<Adiantamento> listaAdiantamento,
            List<CustosDePercurso> listaCustoPercurso
    ) {
        this.context = context;
        this.dataSet_cavalo = lista;
        this.dataSet_motorista = listaMotorista;
        this.dataSet_frete = listaFrete;
        this.dataSet_adiantamento = listaAdiantamento;
        this.dataSet_custoPercurso = listaCustoPercurso;
    }

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(
            final List<Frete> listaFrete,
            final List<Adiantamento> listaAdiantamento,
            final List<CustosDePercurso> listaCustoPercurso
    ){
        if(listaFrete != null){
            this.dataSet_frete.clear();
            this.dataSet_frete.addAll(listaFrete);
        }
        if(listaAdiantamento != null){
            this.dataSet_adiantamento.clear();
            this.dataSet_adiantamento.addAll(listaAdiantamento);
        }
        if(listaCustoPercurso != null){
            this.dataSet_custoPercurso.clear();
            this.dataSet_custoPercurso.addAll(listaCustoPercurso);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualizaDataSetCavalo(List<Cavalo> lista) {
        this.dataSet_cavalo.clear();
        this.dataSet_cavalo.addAll(lista);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView campoPlaca, campoNome, campoComissaoEmAberto, campoAdiantamento,
                campoReembolso, campoComissaoPaga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campoPlaca = itemView.findViewById(R.id.rec_item_salarios_placa);
            campoNome = itemView.findViewById(R.id.rec_item_salarios_nome);
            campoComissaoEmAberto = itemView.findViewById(R.id.rec_item_salarios_total);
            campoAdiantamento = itemView.findViewById(R.id.rec_item_salarios_descontos);
            campoReembolso = itemView.findViewById(R.id.rec_item_salarios_reembolso);
            campoComissaoPaga = itemView.findViewById(R.id.rec_item_salarios_pago);
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
        Cavalo cavalo = dataSet_cavalo.get(position);
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
        return dataSet_cavalo.size();
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    private void vincula(@NonNull ViewHolder holder, @NonNull Cavalo cavalo) {
        final GeraValoresParaUi getValores = new GeraValoresParaUi(dataSet_frete, dataSet_adiantamento, dataSet_custoPercurso, dataSet_motorista);
        final Resource resource = getValores.run(cavalo);

        holder.campoPlaca.setText(cavalo.getPlaca());

        String nome = resource.getNome();
        holder.campoNome.setText(nome);

        String comissaoEmAberto = resource.getComissaoEmAberto();
        holder.campoComissaoEmAberto.setText(comissaoEmAberto);

        String adiantamento = resource.getAdiantamento();
        holder.campoAdiantamento.setText(adiantamento);

        String reembolso = resource.getReembolso();
        holder.campoReembolso.setText(reembolso);

        String comissaoPaga = resource.getComissaoPaga();
        holder.campoComissaoPaga.setText(comissaoPaga);
    }

    public int getPosicao() {
        return posicao;
    }

}

class GeraValoresParaUi {
    private final List<Frete> dataSet_frete;
    private final List<Adiantamento> dataSet_adiantamento;
    private final List<CustosDePercurso> dataSet_custoPercurso;
    private final List<Motorista> dataSet_motorista;

    GeraValoresParaUi(
            List<Frete> dataSetFrete,
            List<Adiantamento> dataSetAdiantamento,
            List<CustosDePercurso> dataSetCustoPercurso,
            List<Motorista> dataSetMotorista
    ) {
        dataSet_frete = dataSetFrete;
        dataSet_adiantamento = dataSetAdiantamento;
        dataSet_custoPercurso = dataSetCustoPercurso;
        dataSet_motorista = dataSetMotorista;
    }

    public Resource run(@NonNull final Cavalo cavalo) {
        final Resource resource = new Resource();
        final List<Frete> dataSet = FiltraFrete.listaPorCavaloId(dataSet_frete, cavalo.getId());

        BigDecimal comissaoEmAberto = getComissaEmAberto(dataSet);
        String comissaoEmAberto_string = FormataNumerosUtil.formataMoedaPadraoBr(comissaoEmAberto);
        resource.setComissaoEmAberto(comissaoEmAberto_string);

        BigDecimal comissaoPaga = getComissaoJaPaga(dataSet);
        String comissaoPaga_string = FormataNumerosUtil.formataMoedaPadraoBr(comissaoPaga);
        resource.setComissaoPaga(comissaoPaga_string);

        BigDecimal adiantamento = getAdiantamentoADescontar(cavalo.getId());
        String adiantamento_string = FormataNumerosUtil.formataMoedaPadraoBr(adiantamento);
        resource.setAdiantamento(adiantamento_string);

        BigDecimal reembolso = getReembolsoDevidoAoMotorista(cavalo.getId());
        String reembolso_string = FormataNumerosUtil.formataMoedaPadraoBr(reembolso);
        resource.setReembolso(reembolso_string);

        String nome = getNomeMotorista(cavalo.getRefMotoristaId());
        resource.setNome(nome);

        return resource;
    }

    private BigDecimal getReembolsoDevidoAoMotorista(final Long cavaloId) {
        List<CustosDePercurso> dataSet = FiltraCustosPercurso.listaPorCavaloId(dataSet_custoPercurso, cavaloId);
        dataSet = FiltraCustosPercurso.listaPorTipo(dataSet, REEMBOLSAVEL_EM_ABERTO);
        return CalculoUtil.somaCustosDePercurso(dataSet);
    }

    private BigDecimal getAdiantamentoADescontar(final Long cavaloId) {
        List<Adiantamento> dataSet = FiltraAdiantamento.listaPorCavaloId(dataSet_adiantamento, cavaloId);
        return CalculoUtil.somaAdiantamentoPorStatus(dataSet, false);
    }

    private BigDecimal getComissaoJaPaga(final List<Frete> dataSet_recebido) {
        List<Frete> dataSet = FiltraFrete.listaPorStatusDePagamentoDaComissao(dataSet_recebido, true);
        return CalculoUtil.somaComissao(dataSet);
    }

    private BigDecimal getComissaEmAberto(final List<Frete> dataSet_recebido) {
        List<Frete> dataSet = FiltraFrete.listaPorStatusDePagamentoDaComissao(dataSet_recebido, false);
        return CalculoUtil.somaComissao(dataSet);
    }

    private String getNomeMotorista(final Long motoristaId) {
        String nome;
        try {
            Motorista motorista = FiltraMotorista.localizaPeloId(dataSet_motorista, motoristaId);
            nome = motorista.getNome();
        } catch (NullPointerException | NoSuchElementException e) {
            nome = "...";
        }
        return nome;
    }

}

class Resource {
    private String reembolso;
    private String adiantamento;
    private String comissaoPaga;
    private String comissaoEmAberto;
    private String nome;

    public String getReembolso() {
        return reembolso;
    }

    public void setReembolso(String reembolso) {
        this.reembolso = reembolso;
    }

    public String getAdiantamento() {
        return adiantamento;
    }

    public void setAdiantamento(String adiantamento) {
        this.adiantamento = adiantamento;
    }

    public String getComissaoPaga() {
        return comissaoPaga;
    }

    public void setComissaoPaga(String comissaoPaga) {
        this.comissaoPaga = comissaoPaga;
    }

    public String getComissaoEmAberto() {
        return comissaoEmAberto;
    }

    public void setComissaoEmAberto(String comissaoEmAberto) {
        this.comissaoEmAberto = comissaoEmAberto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}


package br.com.transporte.appGhn.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.appGhn.filtros.FiltraAdiantamento;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.filtros.FiltraMotorista;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.abstracts.Frota;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.model.custos.CustosDeSalario;
import br.com.transporte.appGhn.ui.fragment.pagamentoComissoes.comissoesPagas.ComissoesPagasFragment;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.OnItemClickListener_getId;

public class SalariosAdapter extends RecyclerView.Adapter<SalariosAdapter.ViewHolder> {
    private final ComissoesPagasFragment context;
    private final List<Frete> dataSet_frete;
    private final List<CustosDeSalario> dataSet_salario;
    private final List<Cavalo> dataSet_cavalo;
    private final List<Motorista> dataSet_motorista;
    private final List<Adiantamento> dataSet_adiantamento;
    private final List<CustosDePercurso> dataSet_percurso;
    private OnItemClickListener_getId onItemClickListener;

    public SalariosAdapter(
            @NonNull ComissoesPagasFragment context,
            List<Frete> dataSetFrete,
            List<CustosDeSalario> listaSalario,
            List<Cavalo> dataSetCavalo,
            List<Motorista> dataSetMotorista,
            List<Adiantamento> dataSetAdiantamento,
            List<CustosDePercurso> dataSetPercurso
    ) {
        this.context = context;
        this.dataSet_frete = dataSetFrete;
        this.dataSet_salario = listaSalario;
        this.dataSet_cavalo = dataSetCavalo;
        this.dataSet_motorista = dataSetMotorista;
        this.dataSet_adiantamento = dataSetAdiantamento;
        this.dataSet_percurso = dataSetPercurso;
    }

    public void setOnItemClickListener(OnItemClickListener_getId onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void exibeResultadoSearch(List<CustosDeSalario> listaSearch) {
        this.dataSet_salario.clear();
        this.dataSet_salario.addAll(listaSearch);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualiza(
            final List<CustosDeSalario> listaDeSalariosPagos,
            final List<Frete> listaFrete
    ) {
        this.dataSet_salario.clear();
        this.dataSet_salario.addAll(listaDeSalariosPagos);

        this.dataSet_frete.clear();
        this.dataSet_frete.addAll(listaFrete);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateViewHolder                                ||
    //----------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public SalariosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View viewCriada = LayoutInflater.from(context.requireContext()).inflate(R.layout.recycler_item_comissoes, parent, false);
        return new ViewHolder(viewCriada);
    }

    //----------------------------------------------------------------------------------------------
    //                                          ViewHolder                                        ||
    //----------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView campoPlaca, campoNome, comissaoPagaTituloTxtView, campoTotalPago,
                campoAdiantamento, campoData, campoReembolso, campoComissao, totalPagoTituloTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campoData = itemView.findViewById(R.id.rec_item_salarios_data);
            campoPlaca = itemView.findViewById(R.id.rec_item_salarios_placa);
            campoNome = itemView.findViewById(R.id.rec_item_salarios_nome);
            comissaoPagaTituloTxtView = itemView.findViewById(R.id.rec_item_salarios_comissao_devida_titulo);
            totalPagoTituloTxtView = itemView.findViewById(R.id.rec_item_salarios_pago_titulo);
            campoTotalPago = itemView.findViewById(R.id.rec_item_salarios_total);
            campoAdiantamento = itemView.findViewById(R.id.rec_item_salarios_descontos);
            campoReembolso = itemView.findViewById(R.id.rec_item_salarios_reembolso);
            campoComissao = itemView.findViewById(R.id.rec_item_salarios_pago);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnBindViewHolder                                  ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull SalariosAdapter.ViewHolder holder, int position) {
        final CustosDeSalario salario = dataSet_salario.get(position);
        configuraUi(holder);
        vincula(holder, salario);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick_getId(salario.getId()));
    }

    @Override
    public int getItemCount() {
        return dataSet_salario.size();
    }

    private void configuraUi(@NonNull ViewHolder holder) {
        holder.campoData.setVisibility(View.VISIBLE);
        holder.totalPagoTituloTxtView.setText(R.string.total_pago);
        holder.comissaoPagaTituloTxtView.setText(R.string.comissao_paga);
    }

    private void vincula(@NonNull ViewHolder holder, CustosDeSalario salario) {
        final CalculaValoresParaUi calculaValores = new CalculaValoresParaUi(dataSet_frete, dataSet_cavalo, dataSet_motorista, dataSet_adiantamento, dataSet_percurso);
        final ResourceSalarios resource = calculaValores.run(salario);

        final String placa = resource.getPlaca();
        holder.campoPlaca.setText(placa);

        final String motorista = resource.getMotorista();
        holder.campoNome.setText(motorista);

        final String data = ConverteDataUtil.dataParaString(salario.getData());
        holder.campoData.setText(data);

        final String adiantamento = resource.getAdiantamento();
        holder.campoAdiantamento.setText(adiantamento);

        final String reembolso = resource.getReembolso();
        holder.campoReembolso.setText(reembolso);

        final String frete = resource.getFrete();
        holder.campoComissao.setText(frete);

        holder.campoTotalPago.setText(FormataNumerosUtil.formataMoedaPadraoBr(salario.getValorCusto()));

    }

}

class CalculaValoresParaUi {
    private final List<Frete> dataSet_frete;
    private final List<Cavalo> dataSet_cavalo;
    private final List<Motorista> dataSet_motorista;
    private final List<Adiantamento> dataSet_adiantamento;
    private final List<CustosDePercurso> dataSet_custoPercurso;

    CalculaValoresParaUi(
            List<Frete> dataSet_frete,
            List<Cavalo> dataSetCavalo,
            List<Motorista> dataSetMotorista,
            List<Adiantamento> dataSetAdiantamento,
            List<CustosDePercurso> dataSetCustoPercurso
    ) {
        this.dataSet_frete = dataSet_frete;
        this.dataSet_cavalo = dataSetCavalo;
        this.dataSet_motorista = dataSetMotorista;
        this.dataSet_adiantamento = dataSetAdiantamento;
        this.dataSet_custoPercurso = dataSetCustoPercurso;
    }

    public ResourceSalarios run(@NonNull final CustosDeSalario salario) {
        final ResourceSalarios resource = new ResourceSalarios();

        final BigDecimal valorAdiantamento = getValorAdiantamento(salario);
        final String adiantamento_string = FormataNumerosUtil.formataMoedaPadraoBr(valorAdiantamento);
        resource.setAdiantamento(adiantamento_string);

        final BigDecimal valorReembolso = getValorReembolso(salario);
        final String reembolso_string = FormataNumerosUtil.formataMoedaPadraoBr(valorReembolso);
        resource.setReembolso(reembolso_string);

        final BigDecimal valorFrete = getValorFrete(salario);
        final String frete_string = FormataNumerosUtil.formataMoedaPadraoBr(valorFrete);
        resource.setFrete(frete_string);

        final Frota cavalo = FiltraCavalo.localizaPeloId(dataSet_cavalo, salario.getRefCavaloId());
        String placa = "";
        if(cavalo != null){
            placa = cavalo.getPlaca();
        }
        resource.setPlaca(placa);

        final String motorista =
                FiltraMotorista.localizaPeloId(dataSet_motorista, salario.getRefMotoristaId()).toString();
        resource.setMotorista(motorista);

        return resource;
    }

    private BigDecimal getValorFrete(@NonNull CustosDeSalario salario) {
        List<BigDecimal> listaFretes = new ArrayList<>();
        for (Long i : salario.getRefFretes()) {
            final BigDecimal valorEncontrado =
                    FiltraFrete.localizaPeloId(dataSet_frete, i).getComissaoAoMotorista();
            if (valorEncontrado != null) listaFretes.add(valorEncontrado);
        }
        return listaFretes.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getValorReembolso(@NonNull CustosDeSalario salario) {
        List<BigDecimal> listaReembolsos = new ArrayList<>();
        for (Long i : salario.getRefReembolsos()) {
            BigDecimal valorEncontrado = FiltraCustosPercurso.localizaPeloId(dataSet_custoPercurso, i).getValorCusto();
            if (valorEncontrado != null) listaReembolsos.add(valorEncontrado);
        }
        return listaReembolsos.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getValorAdiantamento(@NonNull CustosDeSalario salario) {
        List<BigDecimal> listaAdiantamentos = new ArrayList<>();
        for (Long i : salario.getRefAdiantamentos()) {
            BigDecimal valorEncontrado = FiltraAdiantamento.localizaPeloId(dataSet_adiantamento, i).getUltimoValorAbatido();
            if (valorEncontrado != null) listaAdiantamentos.add(valorEncontrado);
        }
        return listaAdiantamentos.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

class ResourceSalarios {
    private String adiantamento;
    private String reembolso;
    private String frete;
    private String motorista;
    private String placa;

    public String getAdiantamento() {
        return adiantamento;
    }

    public void setAdiantamento(String adiantamento) {
        this.adiantamento = adiantamento;
    }

    public String getReembolso() {
        return reembolso;
    }

    public void setReembolso(String reembolso) {
        this.reembolso = reembolso;
    }

    public String getFrete() {
        return frete;
    }

    public void setFrete(String frete) {
        this.frete = frete;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}


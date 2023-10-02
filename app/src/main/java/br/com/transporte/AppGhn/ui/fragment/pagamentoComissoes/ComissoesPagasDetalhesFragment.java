package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes;

import static android.view.View.GONE;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentComissoesPagasDetalhesBinding;
import br.com.transporte.AppGhn.filtros.FiltraAdiantamento;
import br.com.transporte.AppGhn.filtros.FiltraCavalo;
import br.com.transporte.AppGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.filtros.FiltraMotorista;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.ui.adapter.AdiantamentoPagoAdapter;
import br.com.transporte.AppGhn.ui.adapter.FretePagoAdapter;
import br.com.transporte.AppGhn.ui.adapter.ReembolsoPagoAdapter;
import br.com.transporte.AppGhn.ui.viewmodel.ComissaoActViewModel;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class ComissoesPagasDetalhesFragment extends Fragment {
    private FragmentComissoesPagasDetalhesBinding binding;
    private ComissaoActViewModel viewModel;
    private List<Adiantamento> dataSetAdiantamentoRelacionadoAEstePagamento;
    private List<CustosDePercurso> dataSetReembolsoRelacionadoAEstePagamento;
    private List<Frete> dataSetFreteRelacionadoAEstePagamento;
    private TextView campoMotorista, campoPlaca, campoData,
            campoAdiantamento, campoReembolso, campoFrete;
    private AdiantamentoPagoAdapter adapterAdiantamento;
    private ReembolsoPagoAdapter adapterReembolso;
    private FretePagoAdapter adapterFrete;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Long salarioId = ComissoesPagasDetalhesFragmentArgs.fromBundle(getArguments()).getSalarioId();
        viewModel = new ViewModelProvider(requireActivity()).get(ComissaoActViewModel.class);
        viewModel.localizaSalario(salarioId).observe(this,
                salarioRecebido -> {
                    if (salarioRecebido != null) {
                        getListaDeAdiantamentoRelacionadaAEstePagamentoDeSalario(salarioRecebido);
                        getListaDeReembolsoRelacionadaAEstePagamentoDeSalario(salarioRecebido);
                        getListaDeFreteRelacinadaAEstePagamentoDeSalario(salarioRecebido);
                        configuraLayoutsQueDevemSerExibidos(salarioRecebido);
                        enviaDataParaRecyclers();
                        vincula(salarioRecebido);
                    }
                });
    }

    private void configuraLayoutsQueDevemSerExibidos(@NonNull final CustosDeSalario salarioRecebido) {
        if(salarioRecebido.getRefAdiantamentos().size() == 0){
            binding.layoutAdiantamento.setVisibility(GONE);
        }
        if(salarioRecebido.getRefReembolsos().size() == 0) {
            binding.layoutReembolso.setVisibility(GONE);
        }
        if(salarioRecebido.getRefFretes().size() == 0) {
            binding.layoutFrete.setVisibility(GONE);
        }
    }

    private void enviaDataParaRecyclers() {
        adapterAdiantamento.atualizaData(new ArrayList<>(dataSetAdiantamentoRelacionadoAEstePagamento));
        adapterReembolso.atualizaData(new ArrayList<>(dataSetReembolsoRelacionadoAEstePagamento));
        adapterFrete.atualizaData(new ArrayList<>(dataSetFreteRelacionadoAEstePagamento));
    }

    private void getListaDeFreteRelacinadaAEstePagamentoDeSalario(CustosDeSalario salarioRecebido) {
        if (dataSetFreteRelacionadoAEstePagamento == null)
            dataSetFreteRelacionadoAEstePagamento = new ArrayList<>();

        if (salarioRecebido.getRefFretes().size() > 0) {
            for (Long i : salarioRecebido.getRefFretes()) {
                final Frete frete =
                        FiltraFrete.localizaPeloId(viewModel.getDataSetBaseFrete(), i);
                dataSetFreteRelacionadoAEstePagamento.add(frete);
            }
        }
    }

    private void getListaDeReembolsoRelacionadaAEstePagamentoDeSalario(CustosDeSalario salarioRecebido) {
        if (dataSetReembolsoRelacionadoAEstePagamento == null)
            dataSetReembolsoRelacionadoAEstePagamento = new ArrayList<>();

        if (salarioRecebido.getRefReembolsos().size() > 0) {
            for (Long i : salarioRecebido.getRefReembolsos()) {
                final CustosDePercurso custo =
                        FiltraCustosPercurso.localizaPeloId(viewModel.getDataSetReembolso(), i);
                dataSetReembolsoRelacionadoAEstePagamento.add(custo);
            }
        }
    }

    private void getListaDeAdiantamentoRelacionadaAEstePagamentoDeSalario(CustosDeSalario salarioRecebido) {
        if (dataSetAdiantamentoRelacionadoAEstePagamento == null)
            dataSetAdiantamentoRelacionadoAEstePagamento = new ArrayList<>();

        if (salarioRecebido.getRefAdiantamentos().size() > 0) {
            for (Long i : salarioRecebido.getRefAdiantamentos()) {
                final Adiantamento adiantamento =
                        FiltraAdiantamento.localizaPeloId(viewModel.getDataSetAdiantamento(), i);
                dataSetAdiantamentoRelacionadoAEstePagamento.add(adiantamento);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesPagasDetalhesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaViewCompartilhadosPorMetodos();
        configuraRecyclerAdiantamentos();
        configuraRecyclerReembolsos();
        configuraRecyclerFretes();
    }

    private void inicializaCamposDaViewCompartilhadosPorMetodos() {
        campoMotorista = binding.motorista;
        campoPlaca = binding.placa;
        campoData = binding.dataPagamento;
        campoAdiantamento = binding.totalAdiantamento;
        campoReembolso = binding.totalReembolso;
        campoFrete = binding.totalFrete;
    }

    private void vincula(final CustosDeSalario salarioRecebido) {
        final DetalhesDePagamentosGeraValoresParaUi calculaValores = new DetalhesDePagamentosGeraValoresParaUi(
                viewModel.getDataSet_cavalo(),
                viewModel.getDataSet_motorista(),
                dataSetFreteRelacionadoAEstePagamento,
                dataSetAdiantamentoRelacionadoAEstePagamento,
                dataSetReembolsoRelacionadoAEstePagamento
        );
        final ResourceDetalhesDePagamentos resource = calculaValores.run(salarioRecebido);

        final String data = resource.getData();
        campoData.setText(data);

        final String motorista = resource.getMotorista();
        campoMotorista.setText(motorista);

        final String placa = resource.getPlaca();
        campoPlaca.setText(placa);

        final String frete = resource.getFrete();
        campoFrete.setText(frete);

        final String reembolso = resource.getReembolso();
        campoReembolso.setText(reembolso);

        final String adiantamento = resource.getAdiantamento();
        campoAdiantamento.setText(adiantamento);
    }

    private void configuraRecyclerFretes() {
        final RecyclerView recycler = binding.recyclerFretes;
        adapterFrete = new FretePagoAdapter(this, new ArrayList<>());
        recycler.setAdapter(adapterFrete);
        recyclerLinhaVerticalDecoration(recycler);
    }

    private void recyclerLinhaVerticalDecoration(@NonNull RecyclerView recycler) {
        final Drawable divider = ContextCompat.getDrawable(requireContext(), R.drawable.divider);
        final DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(divider));
        recycler.addItemDecoration(itemDecoration);
    }

    private void configuraRecyclerReembolsos() {
        final RecyclerView recycler = binding.recyclerReembolsos;
        adapterReembolso = new ReembolsoPagoAdapter(this, new ArrayList<>());
        recycler.setAdapter(adapterReembolso);
        recyclerLinhaVerticalDecoration(recycler);
    }

    private void configuraRecyclerAdiantamentos() {
        final RecyclerView recycler = binding.recyclerAdiantamentos;
        adapterAdiantamento = new AdiantamentoPagoAdapter(this, new ArrayList<>());
        recycler.setAdapter(adapterAdiantamento);
        recyclerLinhaVerticalDecoration(recycler);
    }

    public void actNotificaAtualizacao_frete() {

    }

    public void actSolicitaAtt_adiantamento() {

    }

    public void actSolicitaAtt_custoPercurso() {

    }

}

class DetalhesDePagamentosGeraValoresParaUi {
    public static final String VALOR_NEGATIVO = "(-) ";
    public static final String VALOR_POSITIVO = "(+) ";
    private final List<Frete> dataSet_frete;
    private final List<Cavalo> dataSet_cavalo;
    private final List<Motorista> dataSet_motorista;
    private final List<Adiantamento> dataSet_adiantamento;
    private final List<CustosDePercurso> dataSet_custoPercurso;

    DetalhesDePagamentosGeraValoresParaUi(
            List<Cavalo> dataSetCavalo,
            List<Motorista> dataSetMotorista,
            List<Frete> dataSet_frete,
            List<Adiantamento> dataSetAdiantamento,
            List<CustosDePercurso> dataSetCustoPercurso
    ) {
        this.dataSet_frete = dataSet_frete;
        this.dataSet_cavalo = dataSetCavalo;
        this.dataSet_motorista = dataSetMotorista;
        this.dataSet_adiantamento = dataSetAdiantamento;
        this.dataSet_custoPercurso = dataSetCustoPercurso;
    }

    //----------------------------------------------------------------------------------------------

    public ResourceDetalhesDePagamentos run(final CustosDeSalario salario) {
        final ResourceDetalhesDePagamentos resource = new ResourceDetalhesDePagamentos();

        final String placa = getPlaca(salario);
        resource.setPlaca(placa);

        final String data = ConverteDataUtil.dataParaString(salario.getData());
        resource.setData(data);

        final String motorista = getMotorista(salario);
        resource.setMotorista(motorista);

        final String adiantamento_string = getAdiantamento();
        resource.setAdiantamento(adiantamento_string);

        final String reembolso_string = getReembolso();
        resource.setReembolso(reembolso_string);

        final String frete_string = getFrete();
        resource.setFrete(frete_string);

        return resource;
    }

    private String getMotorista(@NonNull final CustosDeSalario salario) {
        final Motorista motorista = FiltraMotorista.localizaPeloId(dataSet_motorista, salario.getRefMotoristaId());
        return motorista.getNome();
    }

    @NonNull
    private String getFrete() {
        final BigDecimal somaFrete = CalculoUtil.somaComissao(dataSet_frete);
        return FormataNumerosUtil.formataMoedaPadraoBr(somaFrete);
    }

    @NonNull
    private String getReembolso() {
        final BigDecimal somaReembolso = CalculoUtil.somaCustosDePercurso(dataSet_custoPercurso);
        return VALOR_POSITIVO + FormataNumerosUtil.formataMoedaPadraoBr(somaReembolso);
    }

    @NonNull
    private String getAdiantamento() {
        final BigDecimal somaAdiantamento = CalculoUtil.somaAdiantamentoPorUltimoValorAbatido(dataSet_adiantamento);
        return VALOR_NEGATIVO + FormataNumerosUtil.formataMoedaPadraoBr(somaAdiantamento);
    }

    private String getPlaca(@NonNull final CustosDeSalario salario) {
        String placa = null;
        final Cavalo cavalo = (Cavalo) FiltraCavalo.localizaPeloId(dataSet_cavalo, salario.getRefCavaloId());
        if (cavalo != null) {
            placa = cavalo.getPlaca();
        }
        return placa;
    }

}

class ResourceDetalhesDePagamentos {
    private String placa;
    private String data;
    private String motorista;
    private String adiantamento;
    private String reembolso;
    private String frete;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

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

}
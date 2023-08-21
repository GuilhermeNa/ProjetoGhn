package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.NAO_REEMBOLSAVEL;
import static br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso.REEMBOLSAVEL_EM_ABERTO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaResumoBinding;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.dao.AdiantamentoDAO;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class AreaMotoristaResumoFragment extends Fragment {
    private FragmentAreaMotoristaResumoBinding binding;
    private TextView totalAReceberTxt, valorAcumuladoTxt, jaRecebidoTxt, emAbertoTxt, reembolsoDespesasTxt, adiantamentoTxt,
            dataInicialTxt, dataFinalTxt, freteBrutoTxt, descontosFreteTxt, freteLiquidoTxt, abastecimentoTotalTxt,
            litragemTotalTxt, kmPercorridoTxt, despesaTotalTxt, despesaReembolsavelTxt, despesaNaoReembolsavelTxt;
    private CustosDeAbastecimentoDAO abastecimentoDao;
    private CustosDePercursoDAO custoDao;
    private AdiantamentoDAO adiantamentoDao;
    private FreteDAO freteDao;
    private List<CustosDeAbastecimento> listaDeAbastecimentos;
    private List<Adiantamento> listaAdiantamentos;
    private List<CustosDePercurso> listaDeCustos;
    private List<Frete> listaDeFretes;
    private LocalDate dataInicial, dataFinal;
    private LinearLayout dataLayout;
    private Cavalo cavalo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abastecimentoDao = new CustosDeAbastecimentoDAO();
        adiantamentoDao = new AdiantamentoDAO();
        custoDao = new CustosDePercursoDAO();
        freteDao = new FreteDAO();
        CavaloDAO cavaloDao = new CavaloDAO();

        int cavaloId = getArguments().getInt(CHAVE_ID_CAVALO);
        cavalo = cavaloDao.localizaPeloId(cavaloId);

        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoinicial();

        listaDeFretes = getListaDeFretes(cavalo);
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
        listaDeCustos = getListaDeCustos(cavalo);
        listaAdiantamentos = getListaAdiantamentos(cavalo);

    }

    private List<Adiantamento> getListaAdiantamentos(Cavalo cavalo) {
        return adiantamentoDao.listaPorCavaloEAberto(cavalo.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<CustosDePercurso> getListaDeCustos(Cavalo cavalo) {
        return custoDao.listaFiltradaPorPlacaEData(cavalo.getId(), dataInicial, dataFinal);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<CustosDeAbastecimento> getListaDeAbastecimentos(Cavalo cavalo) {
        return abastecimentoDao.listaFiltradaPorCavaloEData(cavalo.getId(), dataInicial, dataFinal);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Frete> getListaDeFretes(Cavalo cavalo) {
        return freteDao.listaFiltradaPorCavaloEData(cavalo.getId(), dataInicial, dataFinal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAreaMotoristaResumoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraDateRangePicker();

    }

    @Override
    public void onResume() {
        super.onResume();
        configuraUiMutavel();
    }

    private void configuraUiMutavel() {
        listaDeFretes = getListaDeFretes(cavalo);
        listaDeAbastecimentos = getListaDeAbastecimentos(cavalo);
        listaDeCustos = getListaDeCustos(cavalo);
        listaAdiantamentos = getListaAdiantamentos(cavalo);

        dataInicialTxt.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(FormataDataUtil.dataParaString(dataFinal));


        //----------------------------- calculos de comissao --------------------------------------

        BigDecimal somaComissaoTotalAoMotorista = listaDeFretes.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal somaComissaoJaPaga = listaDeFretes.stream()
                .map(Frete::getAdmFrete)
                .filter(Frete.AdmFinanceiroFrete::isComissaoJaFoiPaga)
                .map(Frete.AdmFinanceiroFrete::getComissaoAoMotorista)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal comissaAReceber = somaComissaoTotalAoMotorista.subtract(somaComissaoJaPaga);

        BigDecimal adiantamentoAReembolsar = listaAdiantamentos.stream()
                .filter(a -> !a.isAdiantamentoJaFoiPago())
                .map(Adiantamento::restaReembolsar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal custosDePercursoEmAberto = listaDeCustos.stream()
                .filter(c -> c.getTipo() == REEMBOLSAVEL_EM_ABERTO)
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorAReceber = comissaAReceber.add(custosDePercursoEmAberto).subtract(adiantamentoAReembolsar);


        // ------------------------------ calculos de frete -------------------------------------------

        BigDecimal somaFreteBruto = listaDeFretes.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getFreteBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal somaDescontosNoFrete = listaDeFretes.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getDescontos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal somaFreteLiquidoAReceber = listaDeFretes.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getFreteLiquidoAReceber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // ------------------------------ calculos de abastecimento ------------------------------

        BigDecimal somaAbastecimentoTotal = listaDeAbastecimentos.stream()
                .map(CustosDeAbastecimento::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal somaLitragemTotal = listaDeAbastecimentos.stream()
                .map(CustosDeAbastecimento::getQuantidadeLitros)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // ------------------------------ calculos de custos de percurso --------------------------

        BigDecimal custoDePercursoTotal = listaDeCustos.stream()
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal custosDePercursoSemReembolso = listaDeCustos.stream()
                .filter(c -> c.getTipo() == NAO_REEMBOLSAVEL)
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        //------------------------------ binding comissao

        valorAcumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaComissaoTotalAoMotorista));
        jaRecebidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaComissaoJaPaga));
        emAbertoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaAReceber));
        reembolsoDespesasTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosDePercursoEmAberto));
        adiantamentoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(adiantamentoAReembolsar));
        totalAReceberTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAReceber));


        //------------------------------ binding frete

        freteBrutoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteBruto));
        descontosFreteTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaDescontosNoFrete));
        freteLiquidoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteLiquidoAReceber));


        //------------------------------ binding abastecimento

        abastecimentoTotalTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaAbastecimentoTotal));
        if (somaLitragemTotal.compareTo(BigDecimal.ZERO) == 0) {
            litragemTotalTxt.setText("0.00");
        } else {
            litragemTotalTxt.setText(FormataNumerosUtil.formataNumero(somaLitragemTotal));
        }


        try {
            BigDecimal kmPercorrido = abastecimentoDao.calculaKmPercorrido(cavalo.getId(), dataInicial, dataFinal);
            if (kmPercorrido.compareTo(BigDecimal.ZERO) == 0) {
                kmPercorridoTxt.setText("0.00");
            } else {
                kmPercorridoTxt.setText(FormataNumerosUtil.formataNumero(kmPercorrido));
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            e.getMessage();
            kmPercorridoTxt.setText("0.00");
        }


        //------------------------------ binding custos de percurso

        despesaTotalTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custoDePercursoTotal));
        despesaReembolsavelTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosDePercursoEmAberto));
        despesaNaoReembolsavelTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(custosDePercursoSemReembolso));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraDateRangePicker() {
        MaterialDatePicker dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Selecione o periodo")
                .setSelection(
                        new Pair(
                                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                                MaterialDatePicker.todayInUtcMilliseconds()
                        )
                )
                .build();

        dataLayout.setOnClickListener(v -> {
            dateRangePicker.show(getParentFragmentManager(), "DataRange");

            dateRangePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
                LocalDate dataInicialAtualizada = Instant.ofEpochMilli(Long.parseLong(String.valueOf(selection.first))).atZone(ZoneId.of("America/Sao_Paulo"))
                        .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                        .toLocalDate();

                LocalDate dataFinalAtualizada = Instant.ofEpochMilli(Long.parseLong(String.valueOf(selection.second))).atZone(ZoneId.of("America/Sao_Paulo"))
                        .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                        .toLocalDate();

                dataInicialAtualizada = DataUtil.formataDataParaPadraoPtBr(dataInicialAtualizada);
                dataFinalAtualizada = DataUtil.formataDataParaPadraoPtBr(dataFinalAtualizada);

                this.dataInicial = dataInicialAtualizada;
                this.dataFinal = dataFinalAtualizada;

                configuraUiMutavel();
            });
        });

    }

    private void inicializaCampos() {
        totalAReceberTxt = binding.fragResumoValorTotal;
        valorAcumuladoTxt = binding.fragResumoValorAcumulado;
        jaRecebidoTxt = binding.fragResumoValorJaRecebido;
        emAbertoTxt = binding.fragResumoValorEmAberto;
        reembolsoDespesasTxt = binding.fragResumoValorReembolsoDespesas;
        adiantamentoTxt = binding.fragResumoValorAdiantamento;
        dataLayout = binding.fragResumoMes;
        dataInicialTxt = binding.fragResumoMesDtInicial;
        dataFinalTxt = binding.fragResumoMesDtFinal;

        freteBrutoTxt = binding.fragResumoFreteBrutoVlr;
        descontosFreteTxt = binding.fragResumoFreteDescontosVlr;
        freteLiquidoTxt = binding.fragResumoFreteLiquidoVlr;
        abastecimentoTotalTxt = binding.fragResumoAbastecimentoTotalVlr;
        litragemTotalTxt = binding.fragResumoAbastecimentoLitrosVlr;
        kmPercorridoTxt = binding.fragResumoAbastecimentoKmVlr;
        despesaTotalTxt = binding.fragResumoDespesaTotalVlr;
        despesaReembolsavelTxt = binding.fragResumoDespesasReembolsaveisVlr;
        despesaNaoReembolsavelTxt = binding.fragResumoDespesasNaoReembolsaveisVlr;
    }


}

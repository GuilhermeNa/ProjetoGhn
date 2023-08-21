package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_CUSTO_PERCURSO;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaCustosDePercursoBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.CustosDePercursoAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class AreaMotoristaCustosDePercursoFragment extends Fragment {
    private FragmentAreaMotoristaCustosDePercursoBinding binding;
    private CustosDePercursoAdapter adapter;
    private CustosDePercursoDAO custosDao;
    private LinearLayout dataLayout, buscaVazia;
    private TextView dataInicialTxt, dataFinalTxt, despesaAcumuladaTxt;
    private LocalDate dataInicial, dataFinal;
    private Cavalo cavalo;
    private List<CustosDePercurso> listaDeCustos;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        custosDao = new CustosDePercursoDAO();
        CavaloDAO cavaloDao = new CavaloDAO();

        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoinicial();

        int cavaloId = getArguments().getInt(CHAVE_ID_CAVALO);
        cavalo = cavaloDao.localizaPeloId(cavaloId);
        listaDeCustos = getListaDeCustos();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<CustosDePercurso> getListaDeCustos() {
        List<CustosDePercurso> lista = custosDao.listaFiltradaPorPlacaEData(cavalo.getId(), dataInicial, dataFinal);
        Collections.sort(lista, Comparator.comparing(CustosDePercurso::getData));
        Collections.reverse(lista);
        return lista;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAreaMotoristaCustosDePercursoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();

        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        listaDeCustos = getListaDeCustos();
        adapter.atualiza(listaDeCustos);
        configuraUiMutavel();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUi() {
        configuraUiMutavel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUiMutavel() {
        dataInicialTxt.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(FormataDataUtil.dataParaString(dataFinal));

        BigDecimal somaDespesaTotal = listaDeCustos.stream()
                .map(CustosDePercurso::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        despesaAcumuladaTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaDespesaTotal));

        if (listaDeCustos.isEmpty()) {
            buscaVazia.setVisibility(View.VISIBLE);
        } else if (!listaDeCustos.isEmpty() && buscaVazia.getVisibility() == View.VISIBLE) {
            buscaVazia.setVisibility(View.INVISIBLE);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraRecycler() {
        RecyclerView recycler = binding.fragAreaMotoristaDespesaRecycler;

        adapter = new CustosDePercursoAdapter(this, listaDeCustos);
        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);

        adapter.setOnItemClickListener((idDespesa) -> {
            Intent intent = new Intent(getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_ID, (Integer) idDespesa);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_CUSTO_PERCURSO);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraDateRangePicker() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy", new Locale("pt-br"));

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


                configuraMudancasAposSelecaoDeData();
            });
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraMudancasAposSelecaoDeData() {
        listaDeCustos = getListaDeCustos();
        adapter.atualiza(listaDeCustos);
        configuraUiMutavel();
    }

    private void inicializaCampos() {
        despesaAcumuladaTxt = binding.fragDespesaValorTotal;
        dataLayout = binding.fragDespesaMes;
        dataInicialTxt = binding.fragDespesaMesDtInicial;
        dataFinalTxt = binding.fragDespesaMesDtFinal;
        buscaVazia = binding.fragDespesaVazio;
    }
}

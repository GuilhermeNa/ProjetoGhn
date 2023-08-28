/*
package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.SELECIONE_O_PERIODO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_FRETE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.databinding.FragmentAreaMotoristaFreteBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.FreteAdapter;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class AreaMotoristaFreteFragment extends Fragment {
    private FragmentAreaMotoristaFreteBinding binding;
    private FreteAdapter adapter;
    private FreteDAO freteDao;
    private LocalDate dataInicial, dataFinal;
    private TextView dataInicialTxt, dataFinalTxt, freteAcumuladoTxt;
    private LinearLayout dataLayout, buscaVazia;
    private List<Frete> listaDeFretes;
    private Cavalo cavalo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        freteDao = new FreteDAO();
        CavaloDAO cavaloDao = new CavaloDAO();

        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();

        int cavaloId = requireArguments().getInt(CHAVE_ID_CAVALO);
        cavalo = cavaloDao.localizaPeloId(cavaloId);
        listaDeFretes = getListaDeFretes(cavalo);

    }

    @NonNull
    private List<Frete> getListaDeFretes(@NonNull Cavalo cavalo) {
        List<Frete> lista = freteDao.listaFiltradaPorCavaloEData(cavalo.getId(), dataInicial, dataFinal);
        lista.sort(Comparator.comparing(Frete::getData));
        Collections.reverse(lista);
        return lista;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAreaMotoristaFreteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraRecycler();
        configuraDateRangePicker();

        configuraUiMutavel();

    }

    @Override
    public void onResume() {
        super.onResume();

        listaDeFretes = getListaDeFretes(cavalo);
        adapter.atualiza(listaDeFretes);
        configuraUiMutavel();

    }

    private void configuraUiMutavel() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(dataFinal));

        BigDecimal somaFreteLiquidoAReceber = listaDeFretes.stream()
                .map(Frete::getAdmFrete)
                .map(Frete.AdmFinanceiroFrete::getFreteLiquidoAReceber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        freteAcumuladoTxt.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaFreteLiquidoAReceber));

        if (listaDeFretes.isEmpty()) {
            buscaVazia.setVisibility(View.VISIBLE);
        } else if (!listaDeFretes.isEmpty() && buscaVazia.getVisibility() == View.VISIBLE) {
            buscaVazia.setVisibility(View.INVISIBLE);
        }
    }

    private void configuraRecycler() {
        RecyclerView recycler = binding.fragAreaMotoristaFreteRecycler;

        adapter = new FreteAdapter(this, listaDeFretes);
        recycler.setAdapter(adapter);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(LayoutManager);

        adapter.setOnItemClickListener((idFrete) -> {
            Intent intent = new Intent(getActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_FRETE);
            intent.putExtra(CHAVE_ID, (Integer) idFrete);
            startActivity(intent);
        });
    }

    private void configuraDateRangePicker() {
        DateRangePickerUtil dateRange = new DateRangePickerUtil(dataLayout, getParentFragmentManager());
        dateRange.build();
        dateRange.getDataInicialEmLocalDate()
       */
/* MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(SELECIONE_O_PERIODO)
                .setSelection(
                        new Pair<>(
                                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                                MaterialDatePicker.todayInUtcMilliseconds()
                        )
                )
                .build();

        dataLayout.setOnClickListener(v -> {
            dateRangePicker.show(getParentFragmentManager(), "DataRange");

            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
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
        });*//*


    }

    private void configuraMudancasAposSelecaoDeData() {
        listaDeFretes = getListaDeFretes(cavalo);
        adapter.atualiza(listaDeFretes);
        configuraUiMutavel();
    }

    private void inicializaCampos() {
        freteAcumuladoTxt = binding.fragFreteValorTotal;
        dataLayout = binding.fragFreteMes;
        dataInicialTxt = binding.fragFreteMesDtInicial;
        dataFinalTxt = binding.fragFreteMesDtFinal;
        buscaVazia = binding.fragFreteVazio;
    }

}
*/

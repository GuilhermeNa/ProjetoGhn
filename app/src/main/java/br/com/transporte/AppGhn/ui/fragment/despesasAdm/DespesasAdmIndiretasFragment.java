package br.com.transporte.AppGhn.ui.fragment.despesasAdm;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.SELECIONE_O_PERIODO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentDespesasAdmIndiretasBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.DespesasAdmIndiretasAdapter;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DespesasAdmIndiretasFragment extends Fragment {
    public static final String DESPESAS_INDIRETAS = "Despesas indiretas";
    private FragmentDespesasAdmIndiretasBinding binding;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private DespesasAdmDAO despesaDao;
    private List<DespesaAdm> listaDeDespesasIndiretas;
    private TextView dataFinalTxtView, dataInicialTxtView, valorPagoTxtView;
    private LinearLayout dataLayout;
    private LinearLayout vazioLayout;
    private RecyclerView recyclerView;
    private DespesasAdmIndiretasAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        despesaDao = new DespesasAdmDAO();
        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
        listaDeDespesasIndiretas = getListaDespesasIndiretasFiltradaPorData(dataInicial, dataFinal);
    }

    private List<DespesaAdm> getListaDespesasIndiretasFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        return despesaDao.listaFiltradaPorTipoEData(TipoDespesa.INDIRETA, dataInicial, dataFinal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDespesasAdmIndiretasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraValoresMutaveisDaUi();
        configuraDateRangePicker();
        configuraToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        listaDeDespesasIndiretas = getListaDespesasIndiretasFiltradaPorData(dataInicial, dataFinal);
        configuraValoresMutaveisDaUi();
        configuraAdapterAposAtualizarData();
    }

    private void configuraValoresMutaveisDaUi() {
        BigDecimal somaTotalValorPago = listaDeDespesasIndiretas.stream()
                .map(DespesaAdm::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaTotalValorPago));

        dataInicialTxtView.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    private void configuraRecycler() {
        adapter = new DespesasAdmIndiretasAdapter(this, listaDeDespesasIndiretas);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(despesa -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_DESPESA_ADM);
            intent.putExtra(CHAVE_ID, ((DespesaAdm) despesa).getId());
            startActivity(intent);
        });
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.fragItemDespesasFinanceirasRecycler;
        vazioLayout = binding.fragCertificadoVazio;
        valorPagoTxtView = binding.fragDespesasFinanceirasTotalPagoValor;
        dataLayout = binding.fragDespesasFinanceirasDataLayout;
        dataInicialTxtView = binding.fragDespesasFinanceirasMesDtInicial;
        dataFinalTxtView = binding.fragDespesasFinanceirasMesDtFinal;
    }

    private void configuraDateRangePicker() {
        MaterialDatePicker dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(SELECIONE_O_PERIODO)
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

                this.dataInicial = dataInicialAtualizada;
                this.dataFinal = dataFinalAtualizada;

                configuraAdapterAposAtualizarData();
            });
        });
    }

    private void configuraAdapterAposAtualizarData() {
        listaDeDespesasIndiretas = getListaDespesasIndiretasFiltradaPorData(dataInicial, dataFinal);
        configuraValoresMutaveisDaUi();
        adapter.atualiza(listaDeDespesasIndiretas);

        if (listaDeDespesasIndiretas.isEmpty()) {
            vazioLayout.setVisibility(View.VISIBLE);
        } else if (!listaDeDespesasIndiretas.isEmpty() && vazioLayout.getVisibility() == View.VISIBLE) {
            vazioLayout.setVisibility(View.INVISIBLE);
        }

    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(DESPESAS_INDIRETAS);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
                menu.removeItem(R.id.menu_padrao_editar);
                menu.removeItem(R.id.menu_padrao_search);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


}
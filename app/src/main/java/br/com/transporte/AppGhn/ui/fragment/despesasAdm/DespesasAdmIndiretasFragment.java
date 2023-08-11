package br.com.transporte.AppGhn.ui.fragment.despesasAdm;

import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;

import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
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

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentDespesasAdmIndiretasBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.DespesasAdmIndiretasAdapter;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.util.DatePickerUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DespesasAdmIndiretasFragment extends Fragment {
    private FragmentDespesasAdmIndiretasBinding binding;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private DespesasAdmDAO despesaDao;
    private List<DespesaAdm> listaDeDespesasIndiretas;
    private TextView dataFinalTxtView, datainicialTxtView, valorPagoTxtView;
    private LinearLayout dataLayout;
    private LinearLayout vaziolayout;
    private RecyclerView recyclerView;
    private DespesasAdmIndiretasAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        despesaDao = new DespesasAdmDAO();
        dataInicial = DatePickerUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DatePickerUtil.capturaDataDeHojeParaConfiguracaoinicial();
        listaDeDespesasIndiretas = getListaDespesasIndiretasFiltradaPorData(dataInicial, dataFinal);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<DespesaAdm> getListaDespesasIndiretasFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        return despesaDao.listaFiltradaPorTipoEData(TipoDespesa.INDIRETA, dataInicial, dataFinal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDespesasAdmIndiretasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraValoresMutaveisDaUi();
        configuraDateRangePicker();
        configuraToolbar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        listaDeDespesasIndiretas = getListaDespesasIndiretasFiltradaPorData(dataInicial, dataFinal);
        configuraValoresMutaveisDaUi();
        configuraAdapterAposAtualizarData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraValoresMutaveisDaUi() {
        BigDecimal somaTotalValorPago = listaDeDespesasIndiretas.stream()
                .map(DespesaAdm::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaTotalValorPago));

        datainicialTxtView.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(FormataDataUtil.dataParaString(dataFinal));
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
        vaziolayout = binding.fragCertificadoVazio;
        valorPagoTxtView = binding.fragDespesasFinanceirasTotalPagoValor;
        dataLayout = binding.fragDespesasFinanceirasDataLayout;
        datainicialTxtView = binding.fragDespesasFinanceirasMesDtInicial;
        dataFinalTxtView = binding.fragDespesasFinanceirasMesDtFinal;
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

                this.dataInicial = dataInicialAtualizada;
                this.dataFinal = dataFinalAtualizada;

                configuraAdapterAposAtualizarData();
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraAdapterAposAtualizarData() {
        listaDeDespesasIndiretas = getListaDespesasIndiretasFiltradaPorData(dataInicial, dataFinal);
        configuraValoresMutaveisDaUi();
        adapter.atualiza(listaDeDespesasIndiretas);

        if (listaDeDespesasIndiretas.isEmpty()) {
            vaziolayout.setVisibility(View.VISIBLE);
        } else if (!listaDeDespesasIndiretas.isEmpty() && vaziolayout.getVisibility() == View.VISIBLE) {
            vaziolayout.setVisibility(View.INVISIBLE);
        }

    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Despesas indiretas");
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
                menu.removeItem(R.id.menu_padrao_editar);
                menu.removeItem(R.id.menu_padrao_search);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
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
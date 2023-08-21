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
import android.widget.SearchView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentDespesasAdmBinding;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.DespesasAdmAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class DespesasAdmDiretasFragment extends Fragment {
    private FragmentDespesasAdmBinding binding;
    private List<DespesaAdm> listaDeDespesasDiretas;
    private TextView dataFinalTxtView, datainicialTxtView, valorPagoTxtView;
    private LinearLayout dataLayout;
    private DespesasAdmAdapter adapter;
    private LocalDate dataInicial, dataFinal;
    private DespesasAdmDAO despesaDao;
    private LinearLayout vaziolayout;
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        despesaDao = new DespesasAdmDAO();
        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoinicial();
        listaDeDespesasDiretas = getListaDespesasDiretasFiltradaPorData(dataInicial, dataFinal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDespesasAdmBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();

        configuraValoresMutaveisDaUi(dataInicial, dataFinal);
        configuraDateRangePicker();
        configuraToolbar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        listaDeDespesasDiretas = getListaDespesasDiretasFiltradaPorData(dataInicial, dataFinal);
        configuraValoresMutaveisDaUi(dataInicial, dataFinal);
        configuraAdapterAposAtualizarData();
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
        listaDeDespesasDiretas = getListaDespesasDiretasFiltradaPorData(dataInicial, dataFinal);
        configuraValoresMutaveisDaUi(dataInicial, dataFinal);
        adapter.atualiza(listaDeDespesasDiretas);

        if (listaDeDespesasDiretas.isEmpty()) {
            vaziolayout.setVisibility(View.VISIBLE);
        } else if (!listaDeDespesasDiretas.isEmpty() && vaziolayout.getVisibility() == View.VISIBLE) {
            vaziolayout.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraValoresMutaveisDaUi(LocalDate dataInicial, LocalDate dataFinal) {
        BigDecimal somaTotalValorPago = listaDeDespesasDiretas.stream()
                .map(DespesaAdm::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaTotalValorPago));

        datainicialTxtView.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(FormataDataUtil.dataParaString(dataFinal));
    }

    private void configuraRecycler() {
        adapter = new DespesasAdmAdapter(this, listaDeDespesasDiretas);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<DespesaAdm> getListaDespesasDiretasFiltradaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        listaDeDespesasDiretas = despesaDao.listaFiltradaPorTipoEData(TipoDespesa.DIRETA, dataInicial, dataFinal);
        return listaDeDespesasDiretas;
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.fragItemDespesasFinanceirasRecycler;
        vaziolayout = binding.fragCertificadoVazio;
        valorPagoTxtView = binding.fragDespesasFinanceirasTotalPagoValor;
        dataLayout = binding.fragDespesasFinanceirasDataLayout;
        datainicialTxtView = binding.fragDespesasFinanceirasMesDtInicial;
        dataFinalTxtView = binding.fragDespesasFinanceirasMesDtFinal;

    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Despesas diretas");
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_padrao, menu);
                menu.removeItem(R.id.menu_padrao_editar);
                MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
                MenuItem busca = menu.findItem(R.id.menu_padrao_search);
                SearchView searchView = (SearchView) busca.getActionView();

                searchView.setOnSearchClickListener(v -> {
                    logout.setVisible(false);
                    ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                });

                searchView.setOnCloseListener(() -> {
                    logout.setVisible(true);
                    ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                    return false;
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        List<DespesaAdm> listaFiltrada = new ArrayList<>();
                        CavaloDAO cavaloDao = new CavaloDAO();


                        for (DespesaAdm d : despesaDao.listaFiltradaPorTipoEData(TipoDespesa.DIRETA, dataInicial, dataFinal)) {
                            String placa = cavaloDao.localizaPeloId(d.getRefCavalo()).getPlaca();

                            if (placa.toUpperCase(Locale.ROOT).contains(newText.toUpperCase(Locale.ROOT))) {
                                listaFiltrada.add(d);
                            }
                        }

                        if (listaFiltrada.isEmpty()) {
                            vaziolayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            if (vaziolayout.getVisibility() == View.VISIBLE) {
                                vaziolayout.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            adapter.atualiza(listaFiltrada);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                });
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
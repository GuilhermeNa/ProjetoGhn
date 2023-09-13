package br.com.transporte.AppGhn.ui.fragment.despesasAdm;

import static br.com.transporte.AppGhn.model.enums.TipoDespesa.INDIRETA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.SELECIONE_O_PERIODO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_DESPESA_ADM;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.databinding.FragmentDespesasAdmIndiretasBinding;
import br.com.transporte.AppGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.DespesasAdmIndiretasAdapter;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class DespesasAdmIndiretasFragment extends Fragment {
    public static final String DESPESAS_INDIRETAS = "Despesas indiretas";
    private FragmentDespesasAdmIndiretasBinding binding;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private RoomDespesaAdmDao despesaDao;
    private List<DespesaAdm> dataSet;
    private TextView dataFinalTxtView, dataInicialTxtView, valorPagoTxtView;
    private LinearLayout dataLayout, buscaSemResultado_alerta;
    private RecyclerView recyclerView;
    private DespesasAdmIndiretasAdapter adapter;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        configuraData();
        atualizaDataSet();
    }

    private void atualizaDataSet() {
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = despesaDao.todos();
        dataSet = FiltraDespesasAdm.listaPorTipo(dataSet, INDIRETA);
        dataSet = FiltraDespesasAdm.listaPorData(dataSet, dataInicial, dataFinal);
    }

    private void configuraData() {
        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    }

    private void inicializaDataBase() {
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        despesaDao = dataBase.getRoomDespesaAdmDao();
    }

    @NonNull
    @Contract(" -> new")
    private List<DespesaAdm> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDespesasAdmIndiretasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();
        configuraToolbar();
    }

    private void configuraUi() {
        BigDecimal somaTotalValorPago = dataSet.stream()
                .map(DespesaAdm::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaTotalValorPago));

        dataInicialTxtView.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    private void configuraRecycler() {
        adapter = new DespesasAdmIndiretasAdapter(this, getDataSet());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(despesaId -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_DESPESA_ADM);
            intent.putExtra(CHAVE_ID, (despesaId));
            startActivity(intent);
        });
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.fragItemDespesasFinanceirasRecycler;
        buscaSemResultado_alerta = binding.fragCertificadoVazio;
        valorPagoTxtView = binding.fragDespesasFinanceirasTotalPagoValor;
        dataLayout = binding.fragDespesasFinanceirasDataLayout;
        dataInicialTxtView = binding.fragDespesasFinanceirasMesDtInicial;
        dataFinalTxtView = binding.fragDespesasFinanceirasMesDtFinal;
    }

    private void configuraDateRangePicker() {
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
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
        atualizaDataSet();
        configuraUi();
        adapter.atualiza(getDataSet());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet.size(), buscaSemResultado_alerta, recyclerView, VIEW_INVISIBLE);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(DESPESAS_INDIRETAS);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
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
                        MensagemUtil.toast(requireContext(), LOGOUT);
                        break;

                    case android.R.id.home:
                        requireActivity().finish();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnResume                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        configuraAdapterAposAtualizarData();
    }

}
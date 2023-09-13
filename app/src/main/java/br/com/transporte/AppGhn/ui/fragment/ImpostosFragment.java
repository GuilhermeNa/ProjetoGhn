package br.com.transporte.AppGhn.ui.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_IMPOSTOS;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.databinding.FragmentImpostosBinding;
import br.com.transporte.AppGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.ImpostosAdapter;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class ImpostosFragment extends Fragment {
    public static final String IMPOSTOS = "Impostos";
    private FragmentImpostosBinding binding;
    private TextView valorPagoTxtView, dataInicialTxtView, dataFinalTxtView;
    private LinearLayout dataLayout;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ImpostosAdapter adapter;
    private LocalDate dataInicial, dataFinal;
    private RoomDespesaImpostoDao impostoDao;
    private List<DespesasDeImposto> dataSet;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        atualizaUiAposRetornoResult(REGISTRO_CRIADO);
                        break;
                    case RESULT_DELETE:
                        atualizaUiAposRetornoResult(REGISTRO_APAGADO);
                        break;
                    case RESULT_EDIT:
                        atualizaUiAposRetornoResult(REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;
                }
            });

    private void atualizaUiAposRetornoResult(String msg) {
        atualizaDataSet();
        adapter.atualiza(getDataSet());
        configuraUi();
        MensagemUtil.toast(requireContext(), msg);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        inicializaData_inicialEFinal();
        atualizaDataSet();
    }

    private void inicializaData_inicialEFinal() {
        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    }

    private void inicializaDataBase() {
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        impostoDao = dataBase.getRoomDespesaImpostoDao();
    }

    private void atualizaDataSet() {
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = impostoDao.todos();
        dataSet = FiltraDespesasImposto.listaFiltradaPorData(dataSet, dataInicial, dataFinal);
    }

    @NonNull
    @Contract(" -> new")
    private List<DespesasDeImposto> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImpostosBinding.inflate(getLayoutInflater());
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
        configuraDateRangePicker();
        configuraFab();
        configuraUi();
        configuraToolbar();
    }

    private void inicializaCamposDaView() {
        valorPagoTxtView = binding.fragImpostosPrevisaoTotalPagoValor;
        dataLayout = binding.fragImpostosData;
        dataInicialTxtView = binding.fragImpostosMesDtInicial;
        dataFinalTxtView = binding.fragImpostosMesDtFinal;
        fab = binding.fragImpostosFab;
        recyclerView = binding.fragItemImpostosRecycler;
    }

    private void configuraRecycler() {
        adapter = new ImpostosAdapter(dataSet, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(impostoId -> {
            Intent intent = new Intent(getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_IMPOSTOS);
            intent.putExtra(CHAVE_ID, (impostoId));
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraUi() {
        BigDecimal somaValorTotalDeImpostosPagos = CalculoUtil.somaDespesaImposto(dataSet);

        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaValorTotalDeImpostosPagos));
        dataInicialTxtView.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    private void configuraFab() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.requireActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_IMPOSTOS);
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraDateRangePicker() {
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Selecione o periodo")
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
        });

    }

    private void configuraMudancasAposSelecaoDeData() {
        atualizaDataSet();
        adapter.atualiza(getDataSet());
        configuraUi();
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(IMPOSTOS);
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
                        Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        NavController controlador = Navigation.findNavController(requireView());
                        controlador.popBackStack();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

}
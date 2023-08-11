package br.com.transporte.AppGhn.ui.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_IMPOSTOS;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.DespesasImpostoDAO;
import br.com.transporte.AppGhn.databinding.FragmentImpostosBinding;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.ImpostosAdapter;
import br.com.transporte.AppGhn.util.DatePickerUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ImpostosFragment extends Fragment {
    private FragmentImpostosBinding binding;
    private TextView valorPagoTxtView, dataInicialTxtView, dataFinalTxtView;
    private LinearLayout dataLayout;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ImpostosAdapter adapter;
    private LocalDate dataInicial, dataFinal;
    private DespesasImpostoDAO impostoDao;
    private List<DespesasDeImposto> listaDeImpostos;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                switch (codigoResultado) {
                    case RESULT_OK:
                        atualizaUiAposRetornoResult("Registro criado");
                        break;
                    case RESULT_DELETE:
                        atualizaUiAposRetornoResult("Registro apagado");
                        break;
                    case RESULT_EDIT:
                        atualizaUiAposRetornoResult("Registro editado");
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                        break;
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void atualizaUiAposRetornoResult(String msg) {
        listaDeImpostos = getListaDeImpostos(dataInicial, dataFinal);
        adapter.atualiza(listaDeImpostos);
        configuraUiMutavel();
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        impostoDao = new DespesasImpostoDAO();

        dataInicial = DatePickerUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DatePickerUtil.capturaDataDeHojeParaConfiguracaoinicial();
        listaDeImpostos = getListaDeImpostos(dataInicial, dataFinal);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<DespesasDeImposto> getListaDeImpostos(LocalDate dataInicial, LocalDate dataFinal) {
        return impostoDao.listaFiltradaPorData(dataInicial, dataFinal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImpostosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraDateRangePicker();
        configuraFab();
        configuraUiMutavel();
        configuraToolbar();
    }


    private void configuraUiMutavel() {
        BigDecimal somaValorTotalDeImpostosPagos = listaDeImpostos.stream()
                .map(DespesasDeImposto::getValorDespesa)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaValorTotalDeImpostosPagos));
        dataInicialTxtView.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(FormataDataUtil.dataParaString(dataFinal));
    }

    private void configuraFab() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.requireActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_IMPOSTOS);
            activityResultLauncher.launch(intent);
        });
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

                dataInicialAtualizada = DatePickerUtil.formataDataParaPadraoPtBr(dataInicialAtualizada);
                dataFinalAtualizada = DatePickerUtil.formataDataParaPadraoPtBr(dataFinalAtualizada);

                this.dataInicial = dataInicialAtualizada;
                this.dataFinal = dataFinalAtualizada;

                configuraMudancasAposSelecaoDeData();

            });
        });

    }

    private void configuraMudancasAposSelecaoDeData() {
        listaDeImpostos = getListaDeImpostos(dataInicial, dataFinal);
        adapter.atualiza(listaDeImpostos);
        configuraUiMutavel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraRecycler() {
        adapter = new ImpostosAdapter(listaDeImpostos, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(imposto -> {
            Intent intent = new Intent(getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_IMPOSTOS);
            intent.putExtra(CHAVE_ID, ((DespesasDeImposto) imposto).getId());
            activityResultLauncher.launch(intent);
        });
    }

    private void inicializaCamposDaView() {
        valorPagoTxtView = binding.fragImpostosPrevisaoTotalPagoValor;
        dataLayout = binding.fragImpostosData;
        dataInicialTxtView = binding.fragImpostosMesDtInicial;
        dataFinalTxtView = binding.fragImpostosMesDtFinal;
        fab = binding.fragImpostosFab;
        recyclerView = binding.fragItemImpostosRecycler;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Impostos");
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

                switch (menuItem.getItemId()){
                    case R.id.menu_padrao_logout:

                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
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
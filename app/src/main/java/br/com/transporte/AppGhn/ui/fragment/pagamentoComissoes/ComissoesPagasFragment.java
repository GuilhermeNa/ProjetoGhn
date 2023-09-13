package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.SELECIONE_O_PERIODO;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.jetbrains.annotations.Contract;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.AppGhn.databinding.FragmentComissoesPagasBinding;
import br.com.transporte.AppGhn.filtros.FiltraSalario;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.ui.adapter.SalariosAdapter;
import br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.comissoesPagasHelpers.ComissoesPagasMenuProviderHelper;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class ComissoesPagasFragment extends Fragment {
    public static final String PAGAMENTOS = "Pagamentos";
    private FragmentComissoesPagasBinding binding;
    private RoomCustosDeSalarioDao salarioDao;
    private List<CustosDeSalario> dataSet;
    private LocalDate dataInicial, dataFinal;
    private TextView dataInicialTxtView, dataFinalTxtView;
    private LinearLayout dataLayout;
    private SalariosAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout buscaVazia;
    private GhnDataBase dataBase;
    private ToolbarUtil toolbarUtil;
    private ComissoesPagasMenuProviderHelper menuProviderHelper;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
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
        dataBase = GhnDataBase.getInstance(requireContext());
        salarioDao = dataBase.getRoomCustosDeSalarioDao();
    }

    private void atualizaDataSet() {
        if (dataSet == null) dataSet = new ArrayList<>();
        dataSet = FiltraSalario.listaPorData(salarioDao.todos(), dataInicial, dataFinal);
    }

    @NonNull
    @Contract(" -> new")
    private List<CustosDeSalario> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesPagasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraToolbar();
        configuraMenuProvider();
        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.recycler;
        dataLayout = binding.layoutData;
        dataInicialTxtView = binding.dataInicial;
        dataFinalTxtView = binding.dataFinal;
        buscaVazia = binding.buscaVazia;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        toolbarUtil = new ToolbarUtil(PAGAMENTOS);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
    }

    private void configuraMenuProvider() {
        menuProviderHelper = new ComissoesPagasMenuProviderHelper(getDataSet(), dataBase);
        requireActivity().addMenuProvider(menuProviderHelper, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProviderHelper.setCallbackSearch(new ComissoesPagasMenuProviderHelper.CallbackSearch() {
            @Override
            public void searchViewAtiva() {
                toolbarUtil.setTitleAtivo(false);
            }

            @Override
            public void searchViewInativa() {
                toolbarUtil.setTitleAtivo(true);
            }

            @Override
            public void realizaBusca(List<CustosDeSalario> dataSet_search) {
                adapter.atualiza(dataSet_search);
                ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet_search.size(), buscaVazia, recyclerView, VIEW_INVISIBLE);
            }

            @Override
            public void onLogoutClick() {
                MensagemUtil.toast(requireContext(), LOGOUT);
            }

            @Override
            public void onHomeClick() {
                requireActivity().finish();
            }
        });
    }

    private void configuraRecycler() {
        adapter = new SalariosAdapter(this, dataSet);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(salarioId -> {
            NavController controlador = Navigation.findNavController(this.requireView());
            NavDirections direction = ComissoesPagasFragmentDirections.actionNavComissoesPagasToNavComissoesPagasDetalhes(salarioId);
            controlador.navigate(direction);
        });
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
        configuraUi();
        atualizaAdapter();
        menuProviderHelper.atualiza(getDataSet());
    }

    private void atualizaAdapter() {
        atualizaDataSet();
        adapter.atualiza(getDataSet());
    }

    private void configuraUi() {
        dataInicialTxtView.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(ConverteDataUtil.dataParaString(dataFinal));
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(getDataSet().size(), buscaVazia, recyclerView, VIEW_INVISIBLE);
    }


/*
new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_padrao, menu);
            menu.removeItem(R.id.menu_padrao_editar);
            MenuItem logout = menu.findItem(R.id.menu_padrao_logout);
            MenuItem busca = menu.findItem(R.id.menu_padrao_search);
            SearchView searchView = (SearchView) busca.getActionView();

            Objects.requireNonNull(searchView).setOnSearchClickListener(v -> {
                logout.setVisible(false);
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
            });

            searchView.setOnCloseListener(() -> {
                logout.setVisible(true);
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
                return false;
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    List<CustosDeSalario> listaFiltrada = new ArrayList<>();
                    CavaloDAO cavaloDao = new CavaloDAO();

                    for (CustosDeSalario s : getDataSet()) {
                        String placa = cavaloDao.localizaPeloId(s.getRefCavaloId()).getPlaca();
                        if (placa.toLowerCase().contains(newText.toLowerCase())) {
                            listaFiltrada.add(s);
                        }
                    }

                    if (listaFiltrada.isEmpty()) {
                        buscaVazia.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        if (buscaVazia.getVisibility() == View.VISIBLE) {
                            buscaVazia.setVisibility(View.INVISIBLE);
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

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_padrao_logout:
                    Toast.makeText(requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
                    break;

                case android.R.id.home:
                    requireActivity().finish();


            }


            return false;
        }
    }*/

}
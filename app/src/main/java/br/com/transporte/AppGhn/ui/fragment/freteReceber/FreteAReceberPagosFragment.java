package br.com.transporte.AppGhn.ui.fragment.freteReceber;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.SELECIONE_O_PERIODO;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.databinding.FragmentFreteAReceberPagosBinding;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.FreteAReceberPagoAdapter;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;

public class FreteAReceberPagosFragment extends Fragment implements MenuProvider {
    public static final String FRETE_RECEBIDO = "Frete Recebido";
    private FragmentFreteAReceberPagosBinding binding;
    private LocalDate dataInicial, dataFinal;
    private FreteAReceberPagoAdapter adapter;
    private TextView dataInicialTxt, dataFinalTxt;
    private LinearLayout dataLayout, buscaVazia;
    private List<Frete> listaFiltrada;
    private RecyclerView recyclerView;
    private RoomFreteDao freteDao;
    private GhnDataBase dataBase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase = GhnDataBase.getInstance(requireContext());
        freteDao = dataBase.getRoomFreteDao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFreteAReceberPagosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();


        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
        listaFiltrada = getListaPagos();

        configuraToolbar();
        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();

    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(FRETE_RECEBIDO);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
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

                dataInicialAtualizada = DataUtil.formataDataParaPadraoPtBr(dataInicialAtualizada);
                dataFinalAtualizada = DataUtil.formataDataParaPadraoPtBr(dataFinalAtualizada);

                this.dataInicial = dataInicialAtualizada;
                this.dataFinal = dataFinalAtualizada;

                configuraMudancasAposSelecaoDeData();
            });
        });
    }

    private void configuraMudancasAposSelecaoDeData() {
        listaFiltrada = getListaPagos();
        configuraUi();
        adapter.atualiza(listaFiltrada);
    }

    private void configuraUi() {

        if (listaFiltrada.isEmpty()) {
            buscaVazia.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else if (buscaVazia.getVisibility() == View.VISIBLE) {
            buscaVazia.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        configuraUiMutavel();
    }

    private void configuraUiMutavel() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.recycler;
        dataLayout = binding.layoutData;
        dataInicialTxt = binding.dataInicial;
        dataFinalTxt = binding.dataFinal;
        buscaVazia = binding.buscaVazia;
    }

    private List<Frete> getListaPagos() {
        List<Frete> listaFretes = FiltraFrete.listaPorData(freteDao.todos(), dataInicial, dataFinal);
        // listaFretes = FiltraFrete.listaPorStatusDeRecebimentoDoFrete(listaFretes, true);
        return listaFretes;
    }

    private void configuraRecycler() {
        adapter = new FreteAReceberPagoAdapter(this, listaFiltrada);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        NavController controlador = Navigation.findNavController(requireView());
        adapter.setOnItemClickListener(frete -> {
            NavDirections direction = FreteAReceberPagosFragmentDirections.actionNavFreteReceberPagoToNavFreteAReceberResumo(((Frete) frete).getId());
            controlador.navigate(direction);
        });

    }

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
                List<Frete> lista = new ArrayList<>();
                RoomCavaloDao cavaloDao = dataBase.getRoomCavaloDao();
                String placa;

                List<Frete> listaFretes = FiltraFrete.listaPorData(freteDao.todos(), dataInicial, dataFinal);
               // listaFretes = FiltraFrete.listaPorStatusDeRecebimentoDoFrete(listaFretes, true);

                for (Frete f : listaFretes) {
                    placa = cavaloDao.localizaPeloId(f.getRefCavaloId()).getPlaca().toUpperCase(Locale.ROOT);
                    if (placa.contains(newText.toUpperCase(Locale.ROOT))) {
                        lista.add(f);
                    }
                }

                if (lista.isEmpty()) {
                    buscaVazia.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    if (buscaVazia.getVisibility() == View.VISIBLE) {
                        buscaVazia.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    adapter.atualiza(lista);
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
        if (menuItem.getItemId() == R.id.menu_padrao_logout) {
            Toast.makeText(this.requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == android.R.id.home) {
            requireActivity().finish();
        }

        return false;
    }
}
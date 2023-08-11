package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes;

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

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentComissoesPagasBinding;
import br.com.transporte.AppGhn.model.Salario;
import br.com.transporte.AppGhn.ui.adapter.SalariosAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.SalarioDAO;
import br.com.transporte.AppGhn.util.DatePickerUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;

public class ComissoesPagasFragment extends Fragment {
    private FragmentComissoesPagasBinding binding;
    private SalarioDAO salarioDao;
    private List<Salario> listaDeSalariosPagos;
    private LocalDate dataInicial, dataFinal;
    private TextView dataInicialTxtView;
    private TextView dataFinalTxtView;
    private LinearLayout dataLayout;
    private SalariosAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout buscaVazia;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        salarioDao = new SalarioDAO();
        dataInicial = DatePickerUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DatePickerUtil.capturaDataDeHojeParaConfiguracaoinicial();
        listaDeSalariosPagos = getListaDeSalariosPagos(dataInicial, dataFinal);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Salario> getListaDeSalariosPagos(LocalDate dataInicial, LocalDate dataFinal) {
        return salarioDao.listaFiltradaPorData(dataInicial, dataFinal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesPagasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();

        configuraRecycler();
        configuraUiMutavel();
        configuraToolbar();
        configuraDateRangePicker();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraMudancasAposSelecaoDeData() {
        configuraUiMutavel();
        atualizaAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void atualizaAdapter() {
        listaDeSalariosPagos = getListaDeSalariosPagos(dataInicial, dataFinal);
        adapter.atualiza(listaDeSalariosPagos);
    }

    private void configuraRecycler() {

        adapter = new SalariosAdapter(this, listaDeSalariosPagos);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(salario -> {
            NavController controlador = Navigation.findNavController(this.requireView());
            NavDirections direction = ComissoesPagasFragmentDirections.actionNavComissoesPagasToNavComissoesPagasDetalhes(((Salario) salario).getId());
            controlador.navigate(direction);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUiMutavel() {
        dataInicialTxtView.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxtView.setText(FormataDataUtil.dataParaString(dataFinal));

        if (listaDeSalariosPagos.isEmpty()) {
            buscaVazia.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            buscaVazia.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
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
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Pagamentos");
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

                    private LinearLayout buscaVaziaLayout;

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        List<Salario> listaFiltrada = new ArrayList<>();
                        CavaloDAO cavaloDao = new CavaloDAO();

                        for (Salario s : salarioDao.listaTodos()) {
                            String placa = cavaloDao.localizaPeloId(s.getRefCavalo()).getPlaca();
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

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        requireActivity().finish();


                }


                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


}
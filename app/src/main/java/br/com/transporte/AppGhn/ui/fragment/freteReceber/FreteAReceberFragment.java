package br.com.transporte.AppGhn.ui.fragment.freteReceber;

import android.app.AlertDialog;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentFreteAReceberBinding;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.adapter.FreteAReceberAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.RecebimentoFreteDAO;
import br.com.transporte.AppGhn.util.DatePickerUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class FreteAReceberFragment extends Fragment implements MenuProvider {
    private FragmentFreteAReceberBinding binding;
    private LinearLayout dataLayout, buscaVazia;
    private RecyclerView recyclerView;
    private TextView dataInicialTxt, dataFinalTxt;
    private LocalDate dataInicial, dataFinal;
    private FreteDAO freteDao;
    private FreteAReceberAdapter adapter;
    private NavController controlador;
    private NavDirections direction;
    private List<Frete> listaFiltrada;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFreteAReceberBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        freteDao = new FreteDAO();

        dataInicial = DatePickerUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DatePickerUtil.capturaDataDeHojeParaConfiguracaoinicial();
        listaFiltrada = getListaComPagamentoEmAberto();

        configuraToolbar();
        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();

    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Frete em Aberto");
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Frete> getListaComPagamentoEmAberto() {
        return freteDao.listaFiltradaPorStatusEmAberto(dataInicial, dataFinal);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void configuraDateRangePicker() {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUiMutavel() {
        dataInicialTxt.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(FormataDataUtil.dataParaString(dataFinal));
    }

    private void configuraRecycler() {
        adapter = new FreteAReceberAdapter(this, listaFiltrada);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        controlador = Navigation.findNavController(requireView());
        adapter.setOnItemClickListener(frete -> {
            direction = FreteAReceberFragmentDirections.actionNavFreteReceberToNavFreteAReceberResumo(((Frete) frete).getId());
            controlador.navigate(direction);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraMudancasAposSelecaoDeData() {
        listaFiltrada = getListaComPagamentoEmAberto();
        configuraUi();
        adapter.atualiza(listaFiltrada);

    }

    private void inicializaCamposDaView() {
        buscaVazia = binding.fragFreteReceberVazio;
        dataInicialTxt = binding.dataInicial;
        dataFinalTxt = binding.dataFinal;
        dataLayout = binding.layoutData;
        recyclerView = binding.recItemFreteReceberRecycler;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        Frete frete = listaFiltrada.get(posicao);
        CavaloDAO cavaloDao = new CavaloDAO();
        RecebimentoFreteDAO recebimentoDao = new RecebimentoFreteDAO();

        BigDecimal valorLiquidoAReceber = frete.getAdmFrete().getFreteLiquidoAReceber();
        BigDecimal valorTotalRecebido = recebimentoDao.valorRecebido(frete.getId());
        String placa = cavaloDao.localizaPeloId(frete.getRefCavalo()).getPlaca();

        if (item.getItemId() == R.id.FechaFrete) {
            new AlertDialog.Builder(this.requireContext())
                    .setTitle(placa + " " + frete.getDestino())
                    .setMessage("Você confirma o fechamento?")
                    .setPositiveButton("Confirmar", (dialog, which) -> {
                        if (valorLiquidoAReceber.compareTo(valorTotalRecebido) == 0) {
                            frete.getAdmFrete().setFreteJaFoiPago(true);
                            listaFiltrada = getListaComPagamentoEmAberto();
                            adapter.atualiza(listaFiltrada);
                            Toast.makeText(this.requireContext(), "Frete fechado", Toast.LENGTH_SHORT).show();
                        } else {
                            MensagemUtil.snackBar(getView(), "Valor recebido nao confere");
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }

        return super.onContextItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                List<Frete> lista = new ArrayList<>();
                CavaloDAO cavaloDao = new CavaloDAO();
                String placa;

                for (Frete f : freteDao.listaFiltradaPorData(dataInicial, dataFinal)) {
                    placa = cavaloDao.localizaPeloId(f.getRefCavalo()).getPlaca().toUpperCase(Locale.ROOT);
                    if (placa.contains(newText.toUpperCase(Locale.ROOT))) {
                        lista.add(f);
                    }
                }

                if (lista.isEmpty()) {
                    buscaVazia.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else if (buscaVazia.getVisibility() == View.VISIBLE) {
                    buscaVazia.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.atualiza(lista);
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
            Toast.makeText(this.requireContext(), "Logout", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == android.R.id.home) {
            requireActivity().finish();
        }

        return false;
    }


}
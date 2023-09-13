package br.com.transporte.AppGhn.ui.fragment.freteReceber;

import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment.CANCELAR;
import static br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes.ComissoesDetalhesFragment.CONFIRMAR;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.app.AlertDialog;
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

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.AppGhn.databinding.FragmentFreteAReceberBinding;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.filtros.FiltraRecebimentoFrete;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.ui.adapter.FreteAReceberAdapter;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.ToolbarUtil;

public class FreteAReceberFragment extends Fragment implements MenuProvider {
    public static final String VOCE_CONFIRMA_O_FECHAMENTO = "Você confirma o fechamento?";
    public static final String FRETE_FECHADO = "Frete fechado";
    public static final String VALOR_RECEBIDO_NAO_CONFERE = "Valor recebido nao confere";
    public static final String FRETE_EM_ABERTO = "Frete em Aberto";
    private FragmentFreteAReceberBinding binding;
    private LinearLayout dataLayout, buscaVazia;
    private RecyclerView recyclerView;
    private TextView dataInicialTxt, dataFinalTxt;
    private LocalDate dataInicial, dataFinal;
    private RoomFreteDao freteDao;
    private FreteAReceberAdapter adapter;
    private NavController controlador;
    private NavDirections direction;
    private List<Frete> dataSet_freteEmAberto;
    private GhnDataBase dataBase;
    private RoomCavaloDao cavaloDao;

    //----------------------------------------------------------------------------------------------
    //                                          On Create                                         ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        atualizaData_inicialEFinal();
        atualizaDataSet();
    }

    private void inicializaDataBase() {
        dataBase = GhnDataBase.getInstance(requireContext());
        freteDao = dataBase.getRoomFreteDao();
        cavaloDao = dataBase.getRoomCavaloDao();
    }

    private void atualizaData_inicialEFinal() {
        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    }

    private void atualizaDataSet() {
        List<Frete> dataSet = FiltraFrete.listaPorData(freteDao.todos(), dataInicial, dataFinal);
        dataSet_freteEmAberto = FiltraFrete.listaPorStatusDeRecebimentoDoFrete(dataSet, false);
    }

    @NonNull
    @Contract(" -> new")
    private List<Frete> getDataSet() {
        return new ArrayList<>(dataSet_freteEmAberto);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFreteAReceberBinding.inflate(getLayoutInflater());
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
        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();
    }

    private void inicializaCamposDaView() {
        buscaVazia = binding.fragFreteReceberVazio;
        dataInicialTxt = binding.dataInicial;
        dataFinalTxt = binding.dataFinal;
        dataLayout = binding.layoutData;
        recyclerView = binding.recItemFreteReceberRecycler;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ToolbarUtil toolbarUtil = new ToolbarUtil(FRETE_EM_ABERTO);
        toolbarUtil.configuraToolbar(requireActivity(), toolbar);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void configuraRecycler() {
        adapter = new FreteAReceberAdapter(this, getDataSet());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        controlador = Navigation.findNavController(requireView());
        adapter.setOnItemClickListener(freteId -> {
            direction = FreteAReceberFragmentDirections.actionNavFreteReceberToNavFreteAReceberResumo(freteId);
            controlador.navigate(direction);
        });
    }

    public void configuraDateRangePicker() {
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

    private void configuraUi() {
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet_freteEmAberto.size(), buscaVazia, recyclerView, VIEW_INVISIBLE);
        configuraUiMutavel();
    }

    private void configuraUiMutavel() {
        dataInicialTxt.setText(ConverteDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(ConverteDataUtil.dataParaString(dataFinal));
    }

    private void configuraMudancasAposSelecaoDeData() {
        atualizaDataSet();
        configuraUi();
        adapter.atualiza(getDataSet());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        Frete frete = dataSet_freteEmAberto.get(posicao);
        RoomRecebimentoFreteDao recebimentoDao = dataBase.getRoomRecebimentoFreteDao();
        BigDecimal valorLiquidoAReceber = frete.getFreteLiquidoAReceber();

        List<RecebimentoDeFrete> listaRecebimento = FiltraRecebimentoFrete.listaPeloIdDoFrete(recebimentoDao.todos(), frete.getId());
        BigDecimal valorTotalRecebido = FiltraRecebimentoFrete.valorTotalRecebido(listaRecebimento);
        String placa = cavaloDao.localizaPeloId(frete.getRefCavaloId()).getPlaca();

        if (item.getItemId() == R.id.FechaFrete) {
            new AlertDialog.Builder(this.requireContext())
                    .setTitle(placa + " " + frete.getDestino())
                    .setMessage(VOCE_CONFIRMA_O_FECHAMENTO)
                    .setPositiveButton(CONFIRMAR, (dialog, which) -> {
                        if (valorLiquidoAReceber.compareTo(valorTotalRecebido) == 0) {
                            frete.setFreteJaFoiPago(true);
                            freteDao.substitui(frete);
                            atualizaDataSet();
                            adapter.atualiza(getDataSet());
                            Toast.makeText(this.requireContext(), FRETE_FECHADO, Toast.LENGTH_SHORT).show();
                        } else {
                            MensagemUtil.snackBar(getView(), VALOR_RECEBIDO_NAO_CONFERE);
                        }
                    })
                    .setNegativeButton(CANCELAR, null)
                    .show();
        }
        return super.onContextItemSelected(item);
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
                String placa;

                for (Frete f : getDataSet()) {
                    placa = cavaloDao.localizaPeloId(f.getRefCavaloId()).getPlaca().toUpperCase(Locale.ROOT);
                    if (placa.contains(newText.toUpperCase(Locale.ROOT))) {
                        lista.add(f);
                    }
                }

                ExibirResultadoDaBusca_sucessoOuAlerta.configura(lista.size(), buscaVazia, recyclerView, VIEW_INVISIBLE);
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
            Toast.makeText(this.requireContext(), LOGOUT, Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == android.R.id.home) {
            requireActivity().finish();
        }
        return false;
    }
}
package br.com.transporte.AppGhn.ui.fragment.pagamentoComissoes;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_ADIANTAMENTO;

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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentComissoesBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.ComissoesAdapter;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.util.CentralSalariosEComissoes;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class ComissoesFragment extends Fragment implements MenuProvider {
    private FragmentComissoesBinding binding;
    private ComissoesAdapter adapter;
    private ProgressBar progressBarView;
    private TextView valorPagoTxtView, valorTotalTxtView, valorEmAbertoTxtView, dataFinalTxt, dataInicialTxt;
    private RecyclerView recycler;
    private List<Cavalo> listaCavalos;
    private BigDecimal evolucaoProgressBar;
    private LinearLayout dataLayout;
    private LocalDate dataInicial, dataFinal;
    private CavaloDAO cavaloDao;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                switch(codigoResultado){
                    case RESULT_OK:
                        atualizaAposRetornoDeResult("Registro criado");
                        break;

                    case RESULT_EDIT:
                        atualizaAposRetornoDeResult("Registro editado");
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaAposRetornoDeResult("Registro apagado");
                        break;
                }
            });

    private void atualizaAposRetornoDeResult(String msg) {
        atualizaAdapter();
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cavaloDao = new CavaloDAO();
        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoinicial();
        listaCavalos = cavaloDao.listaTodos();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentComissoesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCampos();
        configuraToolbar();
        configuraRecycler();
        configuraUi();
        configuraDateRangePicker();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        atualizaProgressBar();
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

                dataInicialAtualizada = DataUtil.formataDataParaPadraoPtBr(dataInicialAtualizada);
                dataFinalAtualizada = DataUtil.formataDataParaPadraoPtBr(dataFinalAtualizada);

                this.dataInicial = dataInicialAtualizada;
                this.dataFinal = dataFinalAtualizada;

                configuraMudancasAposSelecaoDeData();
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraMudancasAposSelecaoDeData() {
        configuraUi();
        atualizaAdapter();
    }

    private void atualizaAdapter() {
        listaCavalos = cavaloDao.listaTodos();
        adapter.atualiza(listaCavalos, dataInicial, dataFinal);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Comissao em Aberto");
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUi() {
        configuraUiMutavel();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUiMutavel() {
        dataInicialTxt.setText(FormataDataUtil.dataParaString(dataInicial));
        dataFinalTxt.setText(FormataDataUtil.dataParaString(dataFinal));

        FreteDAO daoFrete = new FreteDAO();
        List<Frete> listaFiltradaPorData = daoFrete.listaFiltradaPorData(dataInicial, dataFinal);

        BigDecimal comissaoTotalDevidaAosMotoristas = CentralSalariosEComissoes.getComissaoTotalDevidaAosMotoristas(listaFiltradaPorData);
        valorTotalTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoTotalDevidaAosMotoristas));

        BigDecimal comissaoTotalQueJaFoiPagaAosMotoristas = CentralSalariosEComissoes.getComissaoTotalQueJaFoiPagaAosMotoristas(listaFiltradaPorData);
        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoTotalQueJaFoiPagaAosMotoristas));

        BigDecimal comissaoEmAbertoASerPaga = comissaoTotalDevidaAosMotoristas.subtract(comissaoTotalQueJaFoiPagaAosMotoristas);
        valorEmAbertoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(comissaoEmAbertoASerPaga));

        try {
            evolucaoProgressBar = comissaoTotalQueJaFoiPagaAosMotoristas.divide(comissaoTotalDevidaAosMotoristas, RoundingMode.HALF_EVEN).multiply(new BigDecimal("100.00"));
            atualizaProgressBar();
        } catch (ArithmeticException e) {
            evolucaoProgressBar = BigDecimal.ZERO;
            e.printStackTrace();
            e.getMessage();
        }
    }

    private void atualizaProgressBar() {
        progressBarView.setProgress(evolucaoProgressBar.intValue());
    }

    private void inicializaCampos() {
        recycler = binding.recycler;
        dataLayout = binding.layoutData;
        dataInicialTxt = binding.dataInicial;
        dataFinalTxt = binding.dataFinal;
        progressBarView = binding.progressBar;
        valorPagoTxtView = binding.pagoValor;
        valorTotalTxtView = binding.totalComissaoValor;
        valorEmAbertoTxtView = binding.emAbertoValor;
    }

    private void configuraRecycler() {
        adapter = new ComissoesAdapter(listaCavalos, this, dataInicial, dataFinal);
        recycler.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        NavController controlador = Navigation.findNavController(requireView());
        adapter.setOnItemClickListener(cavalo -> {
            NavDirections direction = ComissoesFragmentDirections.actionNavComissoesToNavComissoesDetalhes(((Cavalo) cavalo).getId());
            controlador.navigate(direction);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int posicao = -1;
        posicao = adapter.getPosicao();
        Cavalo cavalo = listaCavalos.get(posicao);

        if (item.getItemId() == R.id.concedeAdiantamento) {
            Intent intent = new Intent(this.requireContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_ADIANTAMENTO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            activityResultLauncher.launch(intent);
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
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cavalo> listaFiltrada = new ArrayList<>();
                LinearLayout buscaVaziaLayout = binding.buscaVazia;

                for (Cavalo c : cavaloDao.listaTodos()) {
                    if (c.getPlaca().toLowerCase().contains(newText.toLowerCase())) {
                        listaFiltrada.add(c);
                    }
                }
                if (listaFiltrada.isEmpty()) {
                    buscaVaziaLayout.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.INVISIBLE);
                } else {
                    if (buscaVaziaLayout.getVisibility() == View.VISIBLE) {
                        buscaVaziaLayout.setVisibility(View.INVISIBLE);
                        recycler.setVisibility(View.VISIBLE);
                    }
                    adapter.setListaCavalos(listaFiltrada);
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
                Toast.makeText(this.requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                requireActivity().finish();
                break;
        }
        return false;
    }

}
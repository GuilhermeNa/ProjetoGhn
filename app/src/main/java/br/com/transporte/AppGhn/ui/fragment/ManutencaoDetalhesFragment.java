package br.com.transporte.AppGhn.ui.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_MANUTENCAO;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeManutencaoDAO;
import br.com.transporte.AppGhn.databinding.FragmentManutencaoDetalhesBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.ManutencaoDetalhesAdapter;
import br.com.transporte.AppGhn.util.DatePickerUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.FormataDataUtil;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ManutencaoDetalhesFragment extends Fragment {
    private FragmentManutencaoDetalhesBinding binding;
    private TextView nomeTxtView, valorTxtView, dataInicialTxtView, dataFinalTxtView;
    private LinearLayout dataLayout;
    private ManutencaoDetalhesAdapter adapter;
    private Cavalo cavalo;
    private LinearLayout vazio;
    private List<CustosDeManutencao> listaDeManutencoes;
    private FloatingActionButton fab;
    private LocalDate dataInicial, dataFinal;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                Intent dataResultado = result.getData();

                switch(codigoResultado){
                    case RESULT_EDIT:
                        atualizaUiAposRetornoResult("Registro editado");
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaUiAposRetornoResult("Registro apagado");
                        break;

                    case RESULT_OK:
                        atualizaUiAposRetornoResult("Registro adicionado");
                        break;
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void atualizaUiAposRetornoResult(String msg){
        listaDeManutencoes = getListaFiltradaPorPlacaEData(dataInicial, dataFinal);
        atualizaNaUiOTotalPagoNasManutencoes();
        configuraAdapterAposAtualizarData();
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CavaloDAO cavaloDao = new CavaloDAO();
        cavalo = recebeReferenciaExternaDeCavalo(cavaloDao);

        dataInicial = DatePickerUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DatePickerUtil.capturaDataDeHojeParaConfiguracaoinicial();
        listaDeManutencoes = getListaFiltradaPorPlacaEData(dataInicial, dataFinal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentManutencaoDetalhesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraRecycler();
        configuraUi(dataInicial, dataFinal);
        configuraDateRangePicker();
        configuraFab();
        configuraToolbar();
    }

    private void configuraFab() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_MANUTENCAO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            activityResultLauncher.launch(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<CustosDeManutencao> getListaFiltradaPorPlacaEData(LocalDate dataInicial, LocalDate dataFinal) {
        CustosDeManutencaoDAO manutencaoDao = new CustosDeManutencaoDAO();
        listaDeManutencoes = manutencaoDao.listaFiltradaPorPlacaEData(cavalo.getId(), dataInicial, dataFinal);

        return listaDeManutencoes;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraRecycler() {
        RecyclerView recyclerView = binding.fragManutencaoDetalhesRecycler;

        adapter = new ManutencaoDetalhesAdapter(this, listaDeManutencoes);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(manutencao -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_MANUTENCAO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            intent.putExtra(CHAVE_ID, ((CustosDeManutencao) manutencao).getId());
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

                configuraUiAposAtualizacaoDeData();

            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUiAposAtualizacaoDeData() {
        listaDeManutencoes = getListaFiltradaPorPlacaEData(dataInicial, dataFinal);
        configuraValoresMutaveisDaUi(dataInicial, dataFinal);
        configuraAdapterAposAtualizarData();
    }

    private void configuraAdapterAposAtualizarData() {
        adapter.atualiza(listaDeManutencoes);

        if(listaDeManutencoes.isEmpty()){
            vazio.setVisibility(View.VISIBLE);
        } else if (!listaDeManutencoes.isEmpty() && vazio.getVisibility() == View.VISIBLE){
            vazio.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraUi(LocalDate dataInicial, LocalDate dataFinal) {
        try{
            nomeTxtView.setText(cavalo.getMotorista().getNome());
        } catch (NullPointerException ignore){
            nomeTxtView.setText("S/M");
        }
        configuraValoresMutaveisDaUi(dataInicial, dataFinal);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configuraValoresMutaveisDaUi(LocalDate dataInicial, LocalDate dataFinal) {
        atualizaNaUiOTotalPagoNasManutencoes();

        String dataInicialEmString = FormataDataUtil.dataParaString(dataInicial);
        dataInicialTxtView.setText(dataInicialEmString);

        String dataFinalEmString = FormataDataUtil.dataParaString(dataFinal);
        dataFinalTxtView.setText(dataFinalEmString);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void atualizaNaUiOTotalPagoNasManutencoes() {
        BigDecimal somaTotalCustosDeManutencao = listaDeManutencoes.stream()
                .map(CustosDeManutencao::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaTotalCustosDeManutencao));
    }

    private Cavalo recebeReferenciaExternaDeCavalo(CavaloDAO cavaloDao) {
        Cavalo cavaloRecebido;
        int cavaloId = (int) ManutencaoDetalhesFragmentArgs.fromBundle(getArguments()).getCavaloId();
        cavaloRecebido = cavaloDao.localizaPeloId(cavaloId);

        return cavaloRecebido;
    }

    private void inicializaCamposDaView() {
        nomeTxtView = binding.motorista;
        valorTxtView = binding.fragManutencaoDetalhesTotalPagoValor;
        dataLayout = binding.fragManutencaoDetalhesData;
        dataInicialTxtView = binding.fragManutencaoDetalhesMesDtInicial;
        dataFinalTxtView = binding.fragManutencaoDetalhesMesDtFinal;
        vazio = binding.fragManutencaoDetalhesVazio;
        fab = binding.fragManutencaoDetalhesFab;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(cavalo.getPlaca());
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
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show();
                        break;

                    case android.R.id.home:
                        NavController controlador = Navigation.findNavController(requireView());
                        controlador.popBackStack();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);


    }
}
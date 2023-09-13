package br.com.transporte.AppGhn.ui.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_ADICIONADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.SELECIONE_O_PERIODO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_MANUTENCAO;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
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
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.databinding.FragmentManutencaoDetalhesBinding;
import br.com.transporte.AppGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.ManutencaoDetalhesAdapter;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;

public class ManutencaoDetalhesFragment extends Fragment {
    public static final String S_M = "S/M";
    private FragmentManutencaoDetalhesBinding binding;
    private TextView nomeTxtView, valorTxtView, dataInicialTxtView, dataFinalTxtView;
    private LinearLayout dataLayout;
    private ManutencaoDetalhesAdapter adapter;
    private Cavalo cavalo;
    private LinearLayout buscaVazia;
    private List<CustosDeManutencao> dataSet;
    private FloatingActionButton fab;
    private LocalDate dataInicial, dataFinal;
    private RoomCustosDeManutencaoDao manutencaoDao;
    private RecyclerView recycler;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        atualizaUiAposRetornoResult(REGISTRO_EDITADO);
                        break;

                    case RESULT_CANCELED:
                        Toast.makeText(this.requireContext(), NENHUMA_ALTERACAO_REALIZADA, Toast.LENGTH_SHORT).show();
                        break;

                    case RESULT_DELETE:
                        atualizaUiAposRetornoResult(REGISTRO_APAGADO);
                        break;

                    case RESULT_OK:
                        atualizaUiAposRetornoResult(REGISTRO_ADICIONADO);
                        break;
                }
            });
    private RoomCavaloDao cavaloDao;
    private GhnDataBase dataBase;

    private void atualizaUiAposRetornoResult(String msg) {
        atualizaDataSet();
        atualizaNaUiOTotalPagoNasManutencoes();
        configuraAdapterAposAtualizarData();
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase = GhnDataBase.getInstance(requireContext());
        cavaloDao = dataBase.getRoomCavaloDao();
        manutencaoDao = dataBase.getRoomCustosDeManutencaoDao();

        cavalo = recebeReferenciaExternaDeCavalo(cavaloDao);
        atualizaData_inicialEFinal();
        atualizaDataSet();
    }

    private void atualizaData_inicialEFinal() {
        dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
    }

    private Cavalo recebeReferenciaExternaDeCavalo(@NonNull RoomCavaloDao cavaloDao) {
        Cavalo cavaloRecebido;
        Long cavaloId = ManutencaoDetalhesFragmentArgs.fromBundle(getArguments()).getCavaloId();
        cavaloRecebido = cavaloDao.localizaPeloId(cavaloId);

        return cavaloRecebido;
    }

    private void atualizaDataSet() {
        dataSet = manutencaoDao.listaPeloCavaloId(cavalo.getId());
        dataSet = FiltraCustosManutencao.listaPorData(dataSet, dataInicial, dataFinal);
    }

    @NonNull
    @Contract(" -> new")
    private List<CustosDeManutencao> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentManutencaoDetalhesBinding.inflate(getLayoutInflater());
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

    private void configuraRecycler() {
        recycler = binding.fragManutencaoDetalhesRecycler;

        adapter = new ManutencaoDetalhesAdapter(this, getDataSet());
        recycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recycler.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(manutencaoId -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_MANUTENCAO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            intent.putExtra(CHAVE_ID, (manutencaoId));
            activityResultLauncher.launch(intent);
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

                configuraUiAposAtualizacaoDeData();

            });
        });
    }

    private void configuraUiAposAtualizacaoDeData() {
        atualizaDataSet();
        configuraValoresMutaveisDaUi(dataInicial, dataFinal);
        configuraAdapterAposAtualizarData();
    }

    private void configuraAdapterAposAtualizarData() {
        adapter.atualiza(getDataSet());
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet.size(), buscaVazia, recycler, VIEW_INVISIBLE );
    }

    private void configuraUi(LocalDate dataInicial, LocalDate dataFinal) {
        RoomMotoristaDao motoristaDao = dataBase.getRoomMotoristaDao();
        String nomeDoMotorista;
        try {
            nomeDoMotorista = motoristaDao.localizaPeloId(cavalo.getRefMotoristaId()).getNome();
            nomeTxtView.setText(nomeDoMotorista);
        } catch (NullPointerException ignore) {
            nomeDoMotorista = "S_M";
        }
        nomeTxtView.setText(nomeDoMotorista);
        configuraValoresMutaveisDaUi(dataInicial, dataFinal);
    }

    private void configuraValoresMutaveisDaUi(LocalDate dataInicial, LocalDate dataFinal) {
        atualizaNaUiOTotalPagoNasManutencoes();

        String dataInicialEmString = ConverteDataUtil.dataParaString(dataInicial);
        dataInicialTxtView.setText(dataInicialEmString);

        String dataFinalEmString = ConverteDataUtil.dataParaString(dataFinal);
        dataFinalTxtView.setText(dataFinalEmString);
    }

    private void atualizaNaUiOTotalPagoNasManutencoes() {
        BigDecimal somaTotalCustosDeManutencao = dataSet.stream()
                .map(CustosDeManutencao::getValorCusto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaTotalCustosDeManutencao));
    }

    private void inicializaCamposDaView() {
        nomeTxtView = binding.motorista;
        valorTxtView = binding.fragManutencaoDetalhesTotalPagoValor;
        dataLayout = binding.fragManutencaoDetalhesData;
        dataInicialTxtView = binding.fragManutencaoDetalhesMesDtInicial;
        dataFinalTxtView = binding.fragManutencaoDetalhesMesDtFinal;
        buscaVazia = binding.fragManutencaoDetalhesVazio;
        fab = binding.fragManutencaoDetalhesFab;
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(cavalo.getPlaca());
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
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
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);


    }
}
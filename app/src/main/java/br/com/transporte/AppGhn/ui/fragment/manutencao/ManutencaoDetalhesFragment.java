package br.com.transporte.AppGhn.ui.fragment.manutencao;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_ADICIONADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
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
import android.util.Log;
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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentManutencaoDetalhesBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ManutencaoRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.activity.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.ManutencaoDetalhesAdapter;
import br.com.transporte.AppGhn.ui.viewmodel.ManutencaoDetalhesViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.ManutencaoDetalhesViewModelFactory;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DateRangePickerUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class ManutencaoDetalhesFragment extends Fragment {
    public static final String S_M = "S/M";
    private FragmentManutencaoDetalhesBinding binding;
    private TextView campoMotorista, campoValor, campoDataInicial, campoDataFinal;
    private ManutencaoDetalhesAdapter adapter;
    private Cavalo cavalo;
    private LinearLayout alertaView;
    private FloatingActionButton fab;
    private RecyclerView recycler;
    private ManutencaoDetalhesViewModel viewModel;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                switch (codigoResultado) {
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                    case RESULT_OK:
                        MensagemUtil.toast(requireContext(), REGISTRO_ADICIONADO);
                        break;
                }
            });

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        long cavaloId = ManutencaoDetalhesFragmentArgs.fromBundle(getArguments()).getCavaloId();
        configuraViewModelObserver(cavaloId);

    }

    private void inicializaViewModel() {
        final MotoristaRepository motoristaRepository = new MotoristaRepository(requireContext());
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final ManutencaoRepository manutencaoRepository = new ManutencaoRepository(requireContext());
        final ManutencaoDetalhesViewModelFactory factory = new ManutencaoDetalhesViewModelFactory(
                motoristaRepository,
                cavaloRepository,
                manutencaoRepository
        );
        final ViewModelProvider provedor = new ViewModelProvider(this, factory);
        viewModel = provedor.get(ManutencaoDetalhesViewModel.class);
    }

    private void configuraViewModelObserver(long cavaloId) {
        ManutencaoViewModelObserverHelper observerHelper = new ManutencaoViewModelObserverHelper(viewModel, this);
        observerHelper.run(cavaloId, new ManutencaoViewModelObserverHelper.ObserverCallback() {
            @Override
            public void getCavalo(Cavalo cavaloRecebido) {
                cavalo = cavaloRecebido;
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(cavalo.getPlaca());
            }

            @Override
            public void getMotorista(Motorista motoristaRecebido) {
                Log.d("teste", "mot "+motoristaRecebido);
                if (motoristaRecebido != null)
                    campoMotorista.setText(motoristaRecebido.getNome());
                else
                    campoMotorista.setText("...");
            }

            @Override
            public void dataSet_primeiraExibicaoDeDados(List<CustosDeManutencao> dataSet) {
                adapter.atualiza(dataSet);
                defineCampoValor(dataSet);
                ExibirResultadoDaBusca_sucessoOuAlerta.configura(dataSet.size(), alertaView, recycler, VIEW_INVISIBLE);
            }

            @Override
            public void dataSet_falhaAoCarregar(String erro) {
                MensagemUtil.toast(requireContext(), erro);
            }
        });
    }

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
        configuraDateRangePicker();
        configuraRecycler();
        configuraFab();
        configuraToolbar();
    }

    private void defineCampoValor(final List<CustosDeManutencao> dataSet) {
        BigDecimal valor = CalculoUtil.somaCustosDeManutencao(dataSet);
        String valorEmString = FormataNumerosUtil.formataMoedaPadraoBr(valor);
        campoValor.setText(valorEmString);
    }

    private void inicializaCamposDaView() {
        recycler = binding.fragManutencaoDetalhesRecycler;
        campoMotorista = binding.motorista;
        campoValor = binding.fragManutencaoDetalhesTotalPagoValor;
        LinearLayout dataLayout = binding.fragManutencaoDetalhesData;
        campoDataInicial = binding.fragManutencaoDetalhesMesDtInicial;
        campoDataFinal = binding.fragManutencaoDetalhesMesDtFinal;
        alertaView = binding.fragManutencaoDetalhesVazio;
        fab = binding.fragManutencaoDetalhesFab;
    }

    private void configuraRecycler() {
        adapter = new ManutencaoDetalhesAdapter(this, new ArrayList<>());
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(manutencaoId -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_MANUTENCAO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            intent.putExtra(CHAVE_ID, (manutencaoId));
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraFab() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_MANUTENCAO);
            intent.putExtra(CHAVE_ID_CAVALO, cavalo.getId());
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraDateRangePicker() {
        final DateRangePickerUtil datePicker = new DateRangePickerUtil(getParentFragmentManager());
        datePicker.build(requireView());
        datePicker.configuraCampos(campoDataInicial, campoDataFinal);
        datePicker.setCallbackDatePicker(
                this::atualizaAposNovaSelecaoDeData
        );
    }

    private void atualizaAposNovaSelecaoDeData(LocalDate dataInicial, LocalDate dataFinal) {
        viewModel.setDataInicial(dataInicial);
        viewModel.setDataFinal(dataFinal);
        campoDataInicial.setText(ConverteDataUtil.dataParaString(dataInicial));
        campoDataFinal.setText(ConverteDataUtil.dataParaString(dataFinal));
        List<CustosDeManutencao> listaFiltradaPorNovaBusca = viewModel.filtraPorData();
        adapter.atualiza(listaFiltradaPorNovaBusca);
        defineCampoValor(listaFiltradaPorNovaBusca);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaFiltradaPorNovaBusca.size(), alertaView, recycler, VIEW_INVISIBLE);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
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
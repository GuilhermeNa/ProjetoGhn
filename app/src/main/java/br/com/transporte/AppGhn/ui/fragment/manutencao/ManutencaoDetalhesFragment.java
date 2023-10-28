package br.com.transporte.AppGhn.ui.fragment.manutencao;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.LOGOUT;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_ADICIONADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.AppGhn.ui.activity.ConstantesActivities.TAG;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.CHAVE_ID_CAVALO;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.AppGhn.ui.fragment.ConstantesFragment.VALOR_MANUTENCAO;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentManutencaoDetalhesBinding;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.abstracts.CustosEDespesas;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ManutencaoRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.repository.Resource;
import br.com.transporte.AppGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.AppGhn.ui.adapter.ManutencaoDetalhesAdapter;
import br.com.transporte.AppGhn.ui.fragment.manutencao.extensions.FragmentManutencaoBindAdapterExtension;
import br.com.transporte.AppGhn.ui.viewmodel.ManutencaoDetalhesViewModel;
import br.com.transporte.AppGhn.ui.viewmodel.factory.ManutencaoDetalhesViewModelFactory;
import br.com.transporte.AppGhn.util.CalculoUtil;
import br.com.transporte.AppGhn.util.ConverteDataUtil;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;

public class ManutencaoDetalhesFragment extends Fragment {
    public static final String S_M = "S/M";
    public static final String DATA_RANGE = "DataRange";
    public static final String SELECIONE_O_PERIODO = "Selecione o periodo";
    public static final String IS_FIRST_TIME = "ehPrimeiraExecucaoDoFragment";
    public static final String OBJ_ENVIADO = "OBJ_ENVIADO";
    private FragmentManutencaoDetalhesBinding binding;
    private ManutencaoDetalhesAdapter adapter;
    private ManutencaoDetalhesViewModel viewModel;
    private boolean isFirstTime = true;
    private RecyclerView recycler;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();
                final Intent intent = result.getData();

                switch (codigoResultado) {
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        if (intent != null) {
                            if (intent.hasExtra(OBJ_ENVIADO)) {
                                final CustosDeManutencao manutencao =
                                        (CustosDeManutencao) intent.getSerializableExtra(OBJ_ENVIADO);
                                adapter.notificaQueItemFoiEditado(manutencao);
                            }
                        }

                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;

                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        adapter.notificaQueItemFoiRemovido();
                        break;

                    case RESULT_OK:
                        MensagemUtil.toast(requireContext(), REGISTRO_ADICIONADO);
                        if (intent != null) {
                            if (intent.hasExtra(OBJ_ENVIADO)) {
                                if (intent.hasExtra(OBJ_ENVIADO)) {
                                    final CustosDeManutencao manutencao =
                                            (CustosDeManutencao) intent.getSerializableExtra(OBJ_ENVIADO);
                                    adapter.adiciona(manutencao);
                                }
                            }
                        }
                        break;
                }
            });
    private Observer<Resource<List<CustosDeManutencao>>> observer;
    private Observer<Resource<List<CustosDeManutencao>>> ultimoObserverAtivo;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isFirstTime = savedInstanceState.getBoolean(IS_FIRST_TIME);
        }

        inicializaViewModel();
        carregaArguments();
    }

    private void inicializaViewModel() {
        final MotoristaRepository motoristaRepository = new MotoristaRepository(requireContext());
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final ManutencaoRepository manutencaoRepository = new ManutencaoRepository(requireContext());
        final ManutencaoDetalhesViewModelFactory factory =
                new ManutencaoDetalhesViewModelFactory(
                        motoristaRepository,
                        cavaloRepository,
                        manutencaoRepository
                );
        final ViewModelProvider provedor = new ViewModelProvider(this, factory);
        viewModel = provedor.get(ManutencaoDetalhesViewModel.class);
    }

    private void carregaArguments() {
        viewModel.cavaloId =
                ManutencaoDetalhesFragmentArgs.fromBundle(getArguments()).getCavaloId();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isFirstTime) {
            outState.putBoolean(IS_FIRST_TIME, false);
        }
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
        buscaCavaloEMotorista();
        buscaManutencaoPorId();
        observerPorFiltragemDeData();

        configuraDateRangePicker();
        configuraRecycler();
        configuraFab();
        configuraToolbar();
    }

    private void buscaCavaloEMotorista() {
        viewModel.localizaCavalo().observe(getViewLifecycleOwner(), new Observer<Cavalo>() {
            @Override
            public void onChanged(@Nullable final Cavalo cavalo) {
                viewModel.cavalo = cavalo;
                bindPlaca();
                buscaMotorista();

                viewModel.localizaCavalo()
                        .removeObserver(this);
            }
        });
    }

    private void buscaMotorista() {
        viewModel.localizaMotorista().observe(getViewLifecycleOwner(), new Observer<Motorista>() {
            @Override
            public void onChanged(@Nullable final Motorista motorista) {
                if (motorista != null) {
                    viewModel.motorista = motorista;
                    bindMotorista();
                }

                viewModel.localizaMotorista()
                        .removeObserver(this);
            }
        });
    }

    private void buscaManutencaoPorId() {
        viewModel.buscaManutencaoPorCavaloId().observe(getViewLifecycleOwner(),
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.dataSet = resource.getDado();

                        if (isFirstTime) {
                            viewModel.aplicaFiltroDeData();
                            isFirstTime = false;
                        } else {
                            if (resource.getDado().isEmpty() ||
                                    resource.getDado().size() == 1
                            ) {
                                bindAdapter(resource.getDado());
                                bindCampoValor(resource.getDado());
                            }
                        }
                    }
                });
    }

    private void observerPorFiltragemDeData() {
        viewModel.dataSetFiltrada.observe(getViewLifecycleOwner(),
                dataSetFiltrada -> {
                    Log.d(TAG, "observerDataFiltrada: ");
                    bindAdapter(dataSetFiltrada);
                    bindCampoValor(dataSetFiltrada);
                    bindCampoData();
                });
    }

    private void configuraRecycler() {
        recycler = binding.fragManutencaoDetalhesRecycler;
        adapter = new ManutencaoDetalhesAdapter(this, new ArrayList<>());
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(manutencaoId -> {
            final Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_MANUTENCAO);
            intent.putExtra(CHAVE_ID_CAVALO, viewModel.cavaloId);
            intent.putExtra(CHAVE_ID, (manutencaoId));
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraFab() {
        binding.fragManutencaoDetalhesFab
                .setOnClickListener(v -> {
                    final Intent intent = new Intent(this.getContext(), FormulariosActivity.class);
                    intent.putExtra(CHAVE_FORMULARIO, VALOR_MANUTENCAO);
                    intent.putExtra(CHAVE_ID_CAVALO, viewModel.cavaloId);
                    activityResultLauncher.launch(intent);
                });
    }

    private void configuraDateRangePicker() {
        final LinearLayout layoutData = binding.fragManutencaoDetalhesData;

        final MaterialDatePicker<Pair<Long, Long>> datePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText(SELECIONE_O_PERIODO)
                        .setSelection(new Pair<>(
                                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                                MaterialDatePicker.todayInUtcMilliseconds()))
                        .build();

        layoutData.setOnClickListener(v -> {
            datePicker
                    .show(getChildFragmentManager(), DATA_RANGE);

            datePicker
                    .addOnPositiveButtonClickListener(
                            this::atualizaAposNovaSelecaoDeData);

            datePicker
                    .addOnDismissListener(selection -> {
                        datePicker.clearOnPositiveButtonClickListeners();
                        datePicker.clearOnDismissListeners();
                    });
        });
    }

    private void atualizaAposNovaSelecaoDeData(@NonNull final Pair<Long, Long> selection) {
        final LocalDate dataInicial = DataUtil.longToLocalDate(selection.first);
        final LocalDate dataFinal = DataUtil.longToLocalDate(selection.second);

        viewModel.atualizaData(dataInicial, dataFinal);
        viewModel.aplicaFiltroDeData();
    }

    private void configuraToolbar() {
        final Toolbar toolbar = binding.toolbar;
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
                        MensagemUtil.toast(requireContext(), LOGOUT);
                        break;

                    case android.R.id.home:
                        final NavController controlador = Navigation.findNavController(requireView());
                        controlador.popBackStack();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void bindAdapter(@Nullable final List<CustosDeManutencao> dataSetFiltrada) {
        final LinearLayout alertaView =
                binding.fragManutencaoDetalhesVazio;

        final FragmentManutencaoBindAdapterExtension bindAdapterExt =
                new FragmentManutencaoBindAdapterExtension();

        bindAdapterExt.run(dataSetFiltrada,
                new FragmentManutencaoBindAdapterExtension
                        .FragmentManutencaoBindAdapterCallback() {
                    @Override
                    public void incompletoParaBind() {
                        alertaView.setVisibility(VISIBLE);
                        recycler.setVisibility(INVISIBLE);
                    }

                    @Override
                    public void fazBind() {
                        List<CustosDeManutencao> lista =
                                new ArrayList<>(Objects.requireNonNull(dataSetFiltrada));

                        Objects.requireNonNull(
                                lista).sort(
                                Comparator.comparing(CustosEDespesas::getData));

                        Collections.reverse(lista);

                        alertaView.setVisibility(GONE);
                        recycler.setVisibility(VISIBLE);
                        adapter.atualiza(lista);
                    }
                });
    }

    private void bindCampoData() {
        final TextView campoDataInicial = binding.fragManutencaoDetalhesMesDtInicial;
        campoDataInicial.setText(ConverteDataUtil.dataParaString(viewModel.dataInicial));

        final TextView campoDataFinal = binding.fragManutencaoDetalhesMesDtFinal;
        campoDataFinal.setText(ConverteDataUtil.dataParaString(viewModel.dataFinal));
    }

    private void bindMotorista() {
        final TextView campoMotorista = binding.motorista;
        @NonNull final String nome =
                viewModel.motorista.toString();
        campoMotorista.setText(nome);
    }

    private void bindPlaca() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(viewModel.cavalo.getPlaca());
    }

    private void bindCampoValor(final List<CustosDeManutencao> dataSet) {
        final TextView campoValor = binding.fragManutencaoDetalhesTotalPagoValor;

        final BigDecimal valor =
                CalculoUtil.somaCustosDeManutencao(dataSet);
        final String valorEmString =
                FormataNumerosUtil.formataMoedaPadraoBr(valor);

        campoValor.setText(valorEmString);
    }

    @Override
    public void onResume() {
        super.onResume();
        // buscaListaManutencao();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

package br.com.transporte.appGhn.ui.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.NENHUMA_ALTERACAO_REALIZADA;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_APAGADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_CRIADO;
import static br.com.transporte.appGhn.ui.activity.ConstantesActivities.REGISTRO_EDITADO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_FORMULARIO;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.CHAVE_ID;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_DELETE;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.RESULT_EDIT;
import static br.com.transporte.appGhn.ui.fragment.ConstantesFragment.VALOR_IMPOSTOS;
import static br.com.transporte.appGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.databinding.FragmentImpostosBinding;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.ImpostoRepository;
import br.com.transporte.appGhn.ui.activity.formulario.FormulariosActivity;
import br.com.transporte.appGhn.ui.adapter.ImpostosAdapter;
import br.com.transporte.appGhn.ui.viewmodel.ImpostosViewModel;
import br.com.transporte.appGhn.ui.viewmodel.factory.ImpostosViewModelFactory;
import br.com.transporte.appGhn.util.CalculoUtil;
import br.com.transporte.appGhn.util.ConverteDataUtil;
import br.com.transporte.appGhn.util.DateRangePickerUtil;
import br.com.transporte.appGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.MensagemUtil;

public class ImpostosFragment extends Fragment {
    public static final String IMPOSTOS = "Impostos";
    private FragmentImpostosBinding binding;
    private TextView valorPagoTxtView, campoDataInicial, campoDataFinal;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ImpostosAdapter adapter;
    private ImpostosViewModel viewModel;
    private LinearLayout alertaLayout;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int codigoResultado = result.getResultCode();

                switch (codigoResultado) {
                    case RESULT_OK:
                        MensagemUtil.toast(requireContext(), REGISTRO_CRIADO);
                        break;
                    case RESULT_DELETE:
                        MensagemUtil.toast(requireContext(), REGISTRO_APAGADO);
                        break;
                    case RESULT_EDIT:
                        MensagemUtil.toast(requireContext(), REGISTRO_EDITADO);
                        break;
                    case RESULT_CANCELED:
                        MensagemUtil.toast(requireContext(), NENHUMA_ALTERACAO_REALIZADA);
                        break;
                }
            });
    private NavController controlador;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        buscaDadosObserver();
    }

    private void inicializaViewModel() {
        final ImpostoRepository impostoRepository = new ImpostoRepository(requireContext());
        final CavaloRepository cavaloRepository = new CavaloRepository(requireContext());
        final ImpostosViewModelFactory factory = new ImpostosViewModelFactory(impostoRepository, cavaloRepository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(ImpostosViewModel.class);
    }

    private void buscaDadosObserver() {
        viewModel.buscaImpostos().observe(this,
                resource -> {
                    if (resource.getDado() != null) {
                        viewModel.setDataSet_base(resource.getDado());
                        buscaCavalos();
                    }
                });
    }

    private void buscaCavalos() {
        viewModel.buscaCavalos().observe(this,
                resource -> {
                    if (resource != null) {
                        List<DespesasDeImposto> listaFiltrada = viewModel.filtraPorData();
                        adapter.setDataSetCavalo(resource.getDado());
                        configuraCampoValor(listaFiltrada);
                        adapter.atualiza(listaFiltrada);
                        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaFiltrada.size(), alertaLayout, recyclerView, VIEW_INVISIBLE);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImpostosBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlador =
                Navigation.findNavController(requireView());

        inicializaCamposDaView();
        configuraRecycler();
        configuraDateRangePicker();
        configuraFab();
        configuraToolbar();
    }

    private void inicializaCamposDaView() {
        alertaLayout = binding.fragImpostosAlerta.alerta;
        valorPagoTxtView = binding.fragImpostosPrevisaoTotalPagoValor;
        campoDataInicial = binding.fragImpostosMesDtInicial;
        campoDataFinal = binding.fragImpostosMesDtFinal;
        fab = binding.fragImpostosFab;
        recyclerView = binding.fragItemImpostosRecycler;
    }

    private void configuraRecycler() {
        adapter = new ImpostosAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(impostoId -> {
            Intent intent = new Intent(getContext(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_IMPOSTOS);
            intent.putExtra(CHAVE_ID, (impostoId));
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraCampoValor(List<DespesasDeImposto> dataSet) {
        final BigDecimal somaValorTotalDeImpostosPagos = CalculoUtil.somaDespesaImposto(dataSet);
        valorPagoTxtView.setText(FormataNumerosUtil.formataMoedaPadraoBr(somaValorTotalDeImpostosPagos));
    }

    private void configuraFab() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.requireActivity(), FormulariosActivity.class);
            intent.putExtra(CHAVE_FORMULARIO, VALOR_IMPOSTOS);
            activityResultLauncher.launch(intent);
        });
    }

    private void configuraDateRangePicker() {
        final DateRangePickerUtil dateRange = new DateRangePickerUtil(getParentFragmentManager());
        dateRange.configuraCampos(campoDataInicial, campoDataFinal);
        dateRange.build(requireView());
        dateRange.setCallbackDatePicker(
                this::atualizaAposNovaBuscaPorData);
    }

    private void atualizaAposNovaBuscaPorData(LocalDate dataInicial, LocalDate dataFinal) {
        viewModel.setDataInicial(dataInicial);
        viewModel.setDataFinal(dataFinal);
        campoDataInicial.setText(ConverteDataUtil.dataParaString(dataInicial));
        campoDataFinal.setText(ConverteDataUtil.dataParaString(dataFinal));
        List<DespesasDeImposto> listaFiltrada = viewModel.filtraPorData();
        adapter.atualiza(listaFiltrada);
        configuraCampoValor(listaFiltrada);
        ExibirResultadoDaBusca_sucessoOuAlerta.configura(listaFiltrada.size(), alertaLayout, recyclerView, VIEW_INVISIBLE);
    }

    private void configuraToolbar() {
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
                        final NavDirections direction =
                                ImpostosFragmentDirections.actionGlobalNavLogin();

                        controlador.navigate(direction);
                        break;

                    case android.R.id.home:
                        controlador.popBackStack();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
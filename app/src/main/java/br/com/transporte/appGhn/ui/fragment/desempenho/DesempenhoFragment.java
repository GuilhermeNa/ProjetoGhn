package br.com.transporte.appGhn.ui.fragment.desempenho;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.appGhn.model.enums.TipoDeRequisicao.DESPESAS_ADM;
import static br.com.transporte.appGhn.model.enums.TipoDeRequisicao.DESPESAS_IMPOSTOS;
import static br.com.transporte.appGhn.model.enums.TipoDeRequisicao.DESPESA_CERTIFICADOS;
import static br.com.transporte.appGhn.model.enums.TipoMeses.MES_DEFAULT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.databinding.FragmentDesempenhoBinding;
import br.com.transporte.appGhn.filtros.FiltraCavalo;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.repository.FragmentDesempenhoRepository;
import br.com.transporte.appGhn.repository.Resource;
import br.com.transporte.appGhn.ui.adapter.DetalhesPeriodoAdapter;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedBarChartData;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.InicializaObjetoQueArmazenaRequisicaoDeDadosExt;
import br.com.transporte.appGhn.ui.fragment.desempenho.helpers.DesempenhoBarChartHelper;
import br.com.transporte.appGhn.ui.fragment.desempenho.helpers.DesempenhoMenuProviderHelper;
import br.com.transporte.appGhn.ui.fragment.desempenho.viewmodel.DesempenhoFragmentViewModel;
import br.com.transporte.appGhn.ui.fragment.desempenho.viewmodel.DesempenhoFragmentViewModelFactory;
import br.com.transporte.appGhn.util.FormataNumerosUtil;
import br.com.transporte.appGhn.util.RecyclerDecoration;

public class DesempenhoFragment extends Fragment {
    public static final String DESEMPENHO = "Desempenho";
    private FragmentDesempenhoBinding binding;
    private DesempenhoFragmentViewModel viewModel;
    private DetalhesPeriodoAdapter adapter;
    private DesempenhoBarChartHelper barChartHelper;
    private RecyclerView recyclerView;
    private DesempenhoMenuProviderHelper menuProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaViewModel();
        inicializaObjetoQueArmazenaDadosDaRequisicao();
    }

    private void inicializaViewModel() {
        final FragmentDesempenhoRepository repository = new FragmentDesempenhoRepository(requireContext());
        final DesempenhoFragmentViewModelFactory factory = new DesempenhoFragmentViewModelFactory(repository);
        final ViewModelProvider provider = new ViewModelProvider(this, factory);
        viewModel = provider.get(DesempenhoFragmentViewModel.class);
    }

    private void inicializaObjetoQueArmazenaDadosDaRequisicao() {
        viewModel.dataRequest =
                InicializaObjetoQueArmazenaRequisicaoDeDadosExt.run();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDesempenhoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buscaMotoristasECavalos();
        configuraBarChart();
        configuraRecycler();
        configuraMenuProvider();
        configuraSwitchButton();
    }

    private void buscaMotoristasECavalos() {
        final LiveData<Resource<List<Cavalo>>> observerCavalos = viewModel.buscaCavalos();
        observerCavalos.observe(getViewLifecycleOwner(), new Observer<Resource<List<Cavalo>>>() {
            @Override
            public void onChanged(Resource<List<Cavalo>> resource) {
                if (resource.getDado().isEmpty()) {
                    final NavController controler = Navigation.findNavController(requireView());
                    controler.popBackStack();
                } else {
                    observerCavalos.removeObserver(this);
                    viewModel.dataSetCavalo = resource.getDado();
                    menuProvider.setListaCavalos(resource.getDado());
                    viewModel._data.setValue(viewModel.resourceData);
                    buscaData();
                }
            }
        });

        final LiveData<Resource<List<Motorista>>> observerMotoristas = viewModel.buscaMotoristas();
        observerMotoristas.observe(getViewLifecycleOwner(), new Observer<Resource<List<Motorista>>>() {
            @Override
            public void onChanged(Resource<List<Motorista>> resource) {
                observerMotoristas.removeObserver(this);
                viewModel.dataSetMotorista = resource.getDado();
            }
        });

    }

    private void buscaData() {
        viewModel.buscaData().observe(getViewLifecycleOwner(),
                resourceData -> {
                    viewModel.armazenaResource(resourceData);
                    mapeiaDataParaBarChart();
                    mapeiaDataParaRecycler();
                });
    }

    private void mapeiaDataParaBarChart() {
        final MappedBarChartData mappedBarChartData =
                viewModel.barChartMapper();

        barChartHelper.atualiza(mappedBarChartData);
        configuraCamposDaUiRelacionadosAoBarChart(mappedBarChartData);
    }

    private void mapeiaDataParaRecycler() {
        final List<MappedRecylerData> recyclerMappedData =
                viewModel.recyclerMapper();

        adapter.atualiza(recyclerMappedData);
        configuraCamposDaUiRelacionadosARecycler();
    }

    /** @noinspection NumberEquality */
    private void configuraMenuProvider() {
        menuProvider = new DesempenhoMenuProviderHelper(this);
        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProvider.setCallback(
                (tipo, ano, cavaloId) -> {
                    if (viewModel.dataRequest.getTipo() != tipo
                            || viewModel.dataRequest.getAno() != ano
                            || viewModel.dataRequest.getCavaloId() != cavaloId
                    ) {
                        if (viewModel.dataRequest.getTipo() != tipo)
                            viewModel.dataRequest.setTipo(tipo);

                        if (viewModel.dataRequest.getAno() != ano)
                            viewModel.dataRequest.setAno(ano);

                        if (viewModel.dataRequest.getCavaloId() != cavaloId)
                            viewModel.dataRequest.setCavaloId(cavaloId);

                        viewModel.buscaData();
                    }
                });
    }

    private void configuraBarChart() {
        barChartHelper = new DesempenhoBarChartHelper(binding.chart);
        barChartHelper.configura();
        barChartHelper.setCallback(new DesempenhoBarChartHelper.Callback() {
            @Override
            public void clickSelecionandoMes(int mesSelecionado) {
                atualizaRecyclerPorAlteracaoNoMes(mesSelecionado);
            }

            @Override
            public void clickRemovendoSelecaoDeMes() {
                atualizaRecyclerPorAlteracaoNoMes(MES_DEFAULT.getRef());
            }

            private void atualizaRecyclerPorAlteracaoNoMes(int mesSelecionado) {
                viewModel.dataRequest.setMes(mesSelecionado);
                mapeiaDataParaRecycler();
            }
        });
    }

    private void configuraRecycler() {
        recyclerView = binding.recyclerDetalhes;
        adapter = new DetalhesPeriodoAdapter(this.requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        RecyclerDecoration.linhaHorizontal(requireContext(), recyclerView);
    }


    private void configuraCamposDaUiRelacionadosAoBarChart(@NonNull MappedBarChartData mappedBarChartData) {
        final List<BigDecimal> listaDeValores = mappedBarChartData.getListaDeValores();
        ui_valorAcumulado(listaDeValores);
        ui_tituloBarChart();
    }

    private void configuraCamposDaUiRelacionadosARecycler() {
        ui_visibilidadeSwitch();
        ui_TituloRecycler();
    }

    private void ui_valorAcumulado(@NonNull List<BigDecimal> listaDeValores) {
        final BigDecimal valorAcumulado = listaDeValores.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        binding.valorAcumulado.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAcumulado));
    }

    private void ui_tituloBarChart() {
        if (viewModel.dataRequest.getCavaloId() != null) {
            final String placa =
                    Objects.requireNonNull(
                            FiltraCavalo.localizaPeloId(viewModel.resourceData.getDataSetCavalo(), viewModel.dataRequest.getCavaloId())).getPlaca();
            final String placaFormatada = viewModel.dataRequest.getTipo().getDescricao() + ", " + placa;
            binding.tipo.setText(placaFormatada);
        } else {
            binding.tipo.setText(viewModel.dataRequest.getTipo().getDescricao());
        }
    }

    private void configuraSwitchButton() {
        final SwitchMaterial switchBtn = binding.switchbtn;
        switchBtn.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    viewModel.dataRequest.setExibirRateio(isChecked);
                    mapeiaDataParaRecycler();
                });
    }

    private void ui_visibilidadeSwitch() {
        if (viewModel.dataRequest.getTipo() == DESPESAS_ADM
                || viewModel.dataRequest.getTipo() == DESPESA_CERTIFICADOS
                || viewModel.dataRequest.getTipo() == DESPESAS_IMPOSTOS) {
            binding.switchLayout.setVisibility(VISIBLE);
        } else {
            binding.switchLayout.setVisibility(GONE);
        }
    }

    private void ui_TituloRecycler() {
        if (viewModel.dataRequest.getCavaloId() != null) {
            recyclerView.setVisibility(INVISIBLE);
            binding.detalhesPeriodoTxt.setVisibility(INVISIBLE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            binding.detalhesPeriodoTxt.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


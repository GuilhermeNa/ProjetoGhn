package br.com.transporte.AppGhn.ui.fragment.desempenho;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_ADM;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_IMPOSTOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_CERTIFICADOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.FRETE_BRUTO;
import static br.com.transporte.AppGhn.model.enums.TipoMeses.MES_DEFAULT;
import static br.com.transporte.AppGhn.util.ConstVisibilidade.VIEW_INVISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.databinding.FragmentDesempenhoBinding;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.model.temporarios.ObjetoTemporario_representaCavalo;
import br.com.transporte.AppGhn.ui.adapter.DetalhesPeriodoAdapter;
import br.com.transporte.AppGhn.ui.fragment.desempenho.helpers.DesempenhoBarChartHelper;
import br.com.transporte.AppGhn.ui.fragment.desempenho.helpers.DesempenhoDataSetHelper;
import br.com.transporte.AppGhn.ui.fragment.desempenho.helpers.DesempenhoMenuProviderHelper;
import br.com.transporte.AppGhn.ui.fragment.desempenho.helpers.DesempenhoDataSetRecyclerHelper;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.ExibirResultadoDaBusca_sucessoOuAlerta;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.RecyclerDecoration;

public class DesempenhoFragment extends Fragment {
    public static final long REF_CAVALO_NULA = 0L;
    private FragmentDesempenhoBinding binding;
    private TipoDeRequisicao tipoDeRequisicao;
    private List<Object> dataSet;
    private RoomCavaloDao cavaloDao;
    private int anoRequisitado;
    private List<ObjetoTemporario_representaCavalo> dataSet_recycler;
    private DetalhesPeriodoAdapter adapter;
    private SwitchMaterial switchBtn;
    private RecyclerView recyclerView;
    private DesempenhoBarChartHelper barChart;
    private int mesSelecionado;
    private RoomMotoristaDao motoristaDao;
    private DesempenhoDataSetHelper dataSetHelper;
    private DesempenhoDataSetRecyclerHelper dataSetRecyclerHelper;
    private GhnDataBase dataBase;

    private int getMesSelecionado() {
        return mesSelecionado;
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDataBase();
        inicializaRequisitosDeBuscaNoDataSet();
        configuraDataSetBaseParaChartERecycler();
        configuraDataSetRecyclerHelper();
        //listaObjTemporarios = getListaObjetosTemporarios(anoRequisitado, tipoDeRequisicao, false);
    }

    private void inicializaDataBase() {
        dataBase = GhnDataBase.getInstance(requireContext());
        cavaloDao = dataBase.getRoomCavaloDao();
        motoristaDao = dataBase.getRoomMotoristaDao();
    }

    private void inicializaRequisitosDeBuscaNoDataSet() {
        tipoDeRequisicao = FRETE_BRUTO;
        anoRequisitado = DataUtil.capturaDataDeHojeParaConfiguracaoInicial().getYear();
    }

    private void configuraDataSetBaseParaChartERecycler() {
        dataSetHelper = new DesempenhoDataSetHelper(requireContext());
        dataSet = dataSetHelper.getDataSet(anoRequisitado, tipoDeRequisicao);
    }

    private void configuraDataSetRecyclerHelper() {
        dataSetRecyclerHelper = new DesempenhoDataSetRecyclerHelper(getDataSet(), cavaloDao.todos(), dataBase);
        dataSetRecyclerHelper.atualizaDataSet(tipoDeRequisicao, MES_DEFAULT.getRef(), false, anoRequisitado);
        dataSet_recycler = dataSetRecyclerHelper.getDataSet();
    }

    private void atualizaDataSet(int ano, TipoDeRequisicao tipo) {
        dataSet = dataSetHelper.getDataSet(ano, tipo);
    }

    private void atualizaDataSetRecycler(TipoDeRequisicao tipoRequisicao, int mes, boolean switchIsChecked) {
        dataSetRecyclerHelper.atualizaDataSet(tipoRequisicao, mes, switchIsChecked, anoRequisitado);
        dataSet_recycler = dataSetRecyclerHelper.getDataSet();
    }

    @NonNull
    private List<Object> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    @NonNull
    private List<ObjetoTemporario_representaCavalo> getDataSetRecycler() {
        return new ArrayList<>(dataSet_recycler);
    }

    //----------------------------------------------------------------------------------------------
    //                                          onCreateView                                      ||
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDesempenhoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnViewCreated                                     ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaCamposDaView();
        configuraBarChart();
        configuraToolbar();
        configuraMenuProvider();
        configuraRecycler();
        configuraUi(tipoDeRequisicao, REF_CAVALO_NULA);
        configuraSwitchButton();
    }

    private void inicializaCamposDaView() {
        recyclerView = binding.recyclerDetalhes;
        switchBtn = binding.switchbtn;
    }

    private void configuraSwitchButton() {
        switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            atualizaDataSetRecycler(tipoDeRequisicao, getMesSelecionado(), isChecked);
            adapter.atualiza(getDataSetRecycler());
        });
    }

    private void configuraMenuProvider() {
        DesempenhoMenuProviderHelper menuProvider = new DesempenhoMenuProviderHelper(this);
        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProvider.setCallback((tipo, ano, cavaloId) -> {
            if (tipoDeRequisicao != tipo || anoRequisitado != ano) {
                if (tipoDeRequisicao != tipo) tipoDeRequisicao = tipo;
                if (anoRequisitado != ano) anoRequisitado = ano;
                atualizaDataSet(ano, tipo);
                dataSetRecyclerHelper.atualizaCopiaDataSet(getDataSet());
            }
            atualizaDataSetRecycler(tipo, MES_DEFAULT.getRef(), false);
            barChart.atualizaChart(ano, tipo, cavaloId);
            configuraUi(tipo, cavaloId);
            adapter.atualiza(getDataSetRecycler());
        });
    }

    private void configuraBarChart() {
        barChart = new DesempenhoBarChartHelper(requireContext(), binding.chart);
        barChart.show();
        barChart.setCallback(new DesempenhoBarChartHelper.Callback() {
            @Override
            public void clickSelecionandoMes(int mesSelecionado) {
                atualizaDataSetRecycler(tipoDeRequisicao, mesSelecionado, switchBtn.isChecked());
                adapter.atualiza(getDataSetRecycler());
            }

            @Override
            public void clickRemovendoSelecaoDeMes() {
                atualizaDataSetRecycler(tipoDeRequisicao, MES_DEFAULT.getRef(), switchBtn.isChecked());
                adapter.atualiza(getDataSetRecycler());
            }
        });
    }

    private void configuraRecycler() {
        adapter = new DetalhesPeriodoAdapter(this.requireContext(), getDataSetRecycler());
        recyclerView.setAdapter(adapter);
        RecyclerDecoration.linhaHorizontal(requireContext(), recyclerView);
    }

    private void configuraToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    //------------------------------------------------------------------------
    // -> Configura Ui                                                      ||
    //------------------------------------------------------------------------

    private void configuraUi(TipoDeRequisicao tipoDeRequisicao, Long cavaloId) {
        ui_valorAcumulado();
        ui_tituloBarChart(tipoDeRequisicao, cavaloId);
        ui_visibilidadeSwitch(tipoDeRequisicao);
        ui_TituloRecycler(cavaloId);
    }

    private void ui_valorAcumulado() {
        BigDecimal valorAcumulado = barChart.getListaDeDados().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        binding.valorAcumulado.setText(FormataNumerosUtil.formataMoedaPadraoBr(valorAcumulado));
    }

    private void ui_tituloBarChart(TipoDeRequisicao tipoDeRequisicao, Long cavaloId) {
        if (cavaloId != REF_CAVALO_NULA) {
            String placa = cavaloDao.localizaPeloId(cavaloId).getPlaca();
            String placaFormatada = tipoDeRequisicao.getDescricao() + ", " + placa;
            binding.tipo.setText(placaFormatada);
        } else {
            binding.tipo.setText(tipoDeRequisicao.getDescricao());
        }
    }

    private void ui_visibilidadeSwitch(TipoDeRequisicao tipoDeRequisicao) {
        if (tipoDeRequisicao == DESPESAS_ADM || tipoDeRequisicao == DESPESA_CERTIFICADOS || tipoDeRequisicao == DESPESAS_IMPOSTOS) {
            binding.switchLayout.setVisibility(VISIBLE);
        } else {
            binding.switchLayout.setVisibility(GONE);
        }
    }

    private void ui_TituloRecycler(Long cavaloId) {
        if (cavaloId != REF_CAVALO_NULA) {
            recyclerView.setVisibility(INVISIBLE);
            binding.detalhesPeriodoTxt.setVisibility(INVISIBLE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            binding.detalhesPeriodoTxt.setVisibility(VISIBLE);
        }
    }

}


/*
package br.com.transporte.AppGhn.ui.fragment.desempenho;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_ADM;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_IMPOSTOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_CERTIFICADOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.FRETE_BRUTO;

import android.os.Bundle;
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
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.databinding.FragmentDesempenhoBinding;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.model.temporarios.ObjetoTemporario_representaCavalo;
import br.com.transporte.AppGhn.ui.adapter.DetalhesPeriodoAdapter;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension;
import br.com.transporte.AppGhn.ui.fragment.desempenho.helpers.DesempenhoBarChartHelper;
import br.com.transporte.AppGhn.ui.fragment.desempenho.helpers.DesempenhoMenuProviderHelper;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.RecyclerDecoration;

public class DesempenhoFragment extends Fragment {
    public static final long REF_CAVALO_NULA = 0;
    private FragmentDesempenhoBinding binding;
    private TipoDeRequisicao tipoDeRequisicao;
    private RoomCavaloDao cavaloDao;
    private int anoRequisitado;
    private List<ObjetoTemporario_representaCavalo> listaObjTemporarios;
    private DetalhesPeriodoAdapter adapter;
    private SwitchMaterial switchbtn;
    private RecyclerView recyclerView;
    private DesempenhoBarChartHelper barChart;
    private int mesSelecionado;

    private int getMesSelecionado() {
        return mesSelecionado;
    }

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GhnDataBase dataBase = GhnDataBase.getInstance(requireContext());
        cavaloDao = dataBase.getRoomCavaloDao();
        tipoDeRequisicao = FRETE_BRUTO;
        anoRequisitado = DataUtil.capturaDataDeHojeParaConfiguracaoInicial().getYear();
        listaObjTemporarios = getListaObjetosTemporarios(anoRequisitado, tipoDeRequisicao, false);
    }

    private List<ObjetoTemporario_representaCavalo> getListaObjetosTemporarios(int ano, TipoDeRequisicao tipo, boolean isCheched) {
        return CriaObjetoTemporarioParaExibirNaRecyclerExtension.solicitaDataAnual(ano, tipo, isCheched);
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
        switchbtn = binding.switchbtn;
        configuraBarChart();
        configuraToolbar();
        configuraMenuProvider();
        configuraRecycler();
        configuraSwitchButton();
        configuraUi(tipoDeRequisicao, REF_CAVALO_NULA);
    }

    private void configuraSwitchButton() {
        switchbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listaObjTemporarios = CriaObjetoTemporarioParaExibirNaRecyclerExtension.solicitaDadosPorSwitch(isChecked, getMesSelecionado());
            adapter.atualiza(listaObjTemporarios);
        });
    }

    private void configuraMenuProvider() {
        DesempenhoMenuProviderHelper menuProvider = new DesempenhoMenuProviderHelper(this);
        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        menuProvider.setCallback((tipo, ano, cavaloId) -> {
            tipoDeRequisicao = tipo;
            anoRequisitado = ano;
            barChart.atualizaChart(ano, tipo, cavaloId);
            configuraUi(tipo, cavaloId);
            atualizaRecycler(tipo, ano, switchbtn.isChecked());
        });
    }

    private void configuraBarChart() {
        barChart = new DesempenhoBarChartHelper(binding.chart);
        barChart.show();
        barChart.setCallback(new DesempenhoBarChartHelper.Callback() {
            @Override
            public void clickSelecionandoMes(int mesSelecionado) {
                listaObjTemporarios =
                        CriaObjetoTemporarioParaExibirNaRecyclerExtension.solicitaDataMensal(mesSelecionado, switchbtn.isChecked());
                adapter.atualiza(listaObjTemporarios);
            }

            @Override
            public void clickRemovendoSelecaoDeMes() {
                listaObjTemporarios
                        = getListaObjetosTemporarios(anoRequisitado, tipoDeRequisicao, switchbtn.isChecked());
                adapter.atualiza(listaObjTemporarios);
            }
        });
    }

    private void configuraRecycler() {
        recyclerView = binding.recyclerDetalhes;
        adapter = new DetalhesPeriodoAdapter(this.requireContext(), this.listaObjTemporarios);
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

    //----------------------------------------------------------------------------------------------
    //                                    CallBack -> MenuProvider                                ||
    //----------------------------------------------------------------------------------------------

    private void atualizaRecycler(TipoDeRequisicao tipo, int ano, boolean isCheched) {
        List<ObjetoTemporario_representaCavalo> listaObjTemporarios_aposNovaSolicitacao = CriaObjetoTemporarioParaExibirNaRecyclerExtension.solicitaDataAnual(ano, tipo, isCheched);
        adapter.atualiza(listaObjTemporarios_aposNovaSolicitacao);
    }
}

*/

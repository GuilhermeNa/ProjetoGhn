/*
package br.com.transporte.AppGhn.ui.fragment.desempenho;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_ADM;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESAS_IMPOSTOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.DESPESA_CERTIFICADOS;
import static br.com.transporte.AppGhn.model.enums.TipoDeRequisicao.FRETE_BRUTO;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.transporte.AppGhn.R;
import br.com.transporte.AppGhn.dao.CavaloDAO;
import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.dao.SalarioDAO;
import br.com.transporte.AppGhn.databinding.FragmentDesempenhoBinding;
import br.com.transporte.AppGhn.model.enums.TipoDeRequisicao;
import br.com.transporte.AppGhn.model.temporarios.ObjetoTemporario_representaCavalo;
import br.com.transporte.AppGhn.ui.adapter.DetalhesPeriodoAdapter;
import br.com.transporte.AppGhn.ui.fragment.desempenho.dialog.BottomDialogDesempenho;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.BarChartExtension;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.CriaObjetoTemporarioParaExibirNaRecyclerExtension;
import br.com.transporte.AppGhn.util.DataUtil;
import br.com.transporte.AppGhn.util.FormataNumerosUtil;
import br.com.transporte.AppGhn.util.MensagemUtil;
import br.com.transporte.AppGhn.util.RecyclerDecoration;

public class DesempenhoFragment extends Fragment {
    private FragmentDesempenhoBinding binding;
    private List<BarEntry> listaDeBarras;
    private List<String> listaDeMesesParaExibicao;
    private BarChart barChart;
    private List<BigDecimal> listaDeValoresParaExibicao;
    private boolean indexSelectionado;
    private int posicao;
    private BarDataSet dataSet;
    private BarData barData;
    private List<BigDecimal> listaDeDados;
    private CavaloDAO cavaloDao;
    private TipoDeRequisicao tipoDeRequisicao;
    private int anoRequisitado;
    private List<ObjetoTemporario_representaCavalo> listaDeDetalhesDesempenho;
    private DetalhesPeriodoAdapter adapter;
    private SwitchMaterial switchbtn;
    private RecyclerView recyclerView;

    //----------------------------------------------------------------------------------------------
    //                                          OnCreate                                          ||
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cavaloDao = new CavaloDAO();
        anoRequisitado = DataUtil.capturaDataDeHojeParaConfiguracaoinicial().getYear();
        tipoDeRequisicao = FRETE_BRUTO;
        atualizaDadosNecessariosParaExibicao(anoRequisitado, tipoDeRequisicao, 0);
    }

    private void atualizaDadosNecessariosParaExibicao(int ano, TipoDeRequisicao tipoDeRequisicao, int id) {
        listaDeDados = BarChartExtension.filtraListaParaExibicao(ano, tipoDeRequisicao, id);
        listaDeValoresParaExibicao = BarChartExtension.removeValoresVaziosDaLista(listaDeDados);
        listaDeMesesParaExibicao = BarChartExtension.removeMesesVaziosDaLista(listaDeDados);
        listaDeBarras = criaListaDeBarras(listaDeValoresParaExibicao);
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

        configuraChart();
        atualizaUi(FRETE_BRUTO, 0);
        configuraToolbar();
        configuraRecycler();
    }

    private void configuraRecycler() {
        recyclerView = binding.recyclerDetalhes;

        listaDeDetalhesDesempenho = getListaDeDetalhesDesempenho(anoRequisitado, FRETE_BRUTO, switchbtn.isChecked());
        adapter = new DetalhesPeriodoAdapter(this.requireContext(), this.listaDeDetalhesDesempenho);
        recyclerView.setAdapter(adapter);

        RecyclerDecoration.linhaHorizontal(requireContext(), recyclerView);
    }

    private List<ObjetoTemporario_representaCavalo> getListaDeDetalhesDesempenho(int ano, TipoDeRequisicao tipo, boolean isCheched) {
        return CriaObjetoTemporarioParaExibirNaRecyclerExtension.solicitaDataAnual(ano, tipo, isCheched);
    }

    private void atualizaLista(int ano, TipoDeRequisicao tipoDeRequisicao, int id) {
        dataSet.clear();
        barData.removeDataSet(dataSet);
        barChart.removeAllViews();
        barChart.clear();

        atualizaDadosNecessariosParaExibicao(ano, tipoDeRequisicao, id);

        try {
            configuraValorDoEixoY(listaDeValoresParaExibicao);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        dataSet.setValues(listaDeBarras);
        barData.addDataSet(dataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(listaDeMesesParaExibicao));

        configuraVisibilidadeDeBarras(listaDeValoresParaExibicao);

        barChart.animateY(700);
        barChart.invalidate();

    }

    private void atualizaUi(TipoDeRequisicao tipoDeRequisicao, int id) {
        BigDecimal reduce = listaDeDados.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        binding.valorAcumulado.setText(FormataNumerosUtil.formataMoedaPadraoBr(reduce));

        if (id != 0) {
            String placa = cavaloDao.localizaPeloId(id).getPlaca();
            binding.tipo.setText(tipoDeRequisicao.getDescricao() + ", " + placa);
        } else {
            binding.tipo.setText(tipoDeRequisicao.getDescricao());
        }

        if (tipoDeRequisicao == DESPESAS_ADM || tipoDeRequisicao == DESPESA_CERTIFICADOS || tipoDeRequisicao == DESPESAS_IMPOSTOS) {
            binding.switchLayout.setVisibility(VISIBLE);
        } else {
            binding.switchLayout.setVisibility(GONE);
        }

        switchbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listaDeDetalhesDesempenho = CriaObjetoTemporarioParaExibirNaRecyclerExtension.solicitaDadosPorSwitch(isChecked, getPosicao());
            adapter.atualiza(listaDeDetalhesDesempenho);
        });

    }

    private void configuraChart() {
        dataSet = new BarDataSet(listaDeBarras, " ");
        dataSet.setValueTextSize(10);
        dataSet.setBarBorderWidth(0.5f);

        barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);

        barChart = binding.chart;
        barChart.setDragEnabled(true);
        barChart.setData(barData);
        barChart.animateY(1500);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setHighlightPerTapEnabled(true);
        barChart.setHighlightPerDragEnabled(false);
        barChart.invalidate();
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawLabels(false);
        axisRight.setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(listaDeMesesParaExibicao));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        configuraVisibilidadeDeBarras(listaDeValoresParaExibicao);

        YAxis yAxis = barChart.getAxisLeft();
        configuraValorDoEixoY(listaDeValoresParaExibicao);
        yAxis.setAxisLineWidth(1f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(5);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawLabels(true);
        yAxis.setGridColor(Color.GRAY);

        configuraCLick();
    }

    private void configuraCLick() {
        indexSelectionado = false;

        barChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                indexSelectionado = true;
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if (indexSelectionado) {
                    int i = Math.round(e.getX());
                    setPosicao(i + 1);
                    indexSelectionado = false;
                }

                int mesSelecionado = getPosicao();
                List<ObjetoTemporario_representaCavalo> listaDeDetalhesDesempenhoPorMes = CriaObjetoTemporarioParaExibirNaRecyclerExtension.solicitaDataMensal(mesSelecionado, switchbtn.isChecked());
                adapter.atualiza(listaDeDetalhesDesempenhoPorMes);
            }

            @Override
            public void onNothingSelected() {
                setPosicao(0);
                listaDeDetalhesDesempenho = getListaDeDetalhesDesempenho(anoRequisitado, tipoDeRequisicao, switchbtn.isChecked());
                adapter.atualiza(listaDeDetalhesDesempenho);

            }
        });

    }

    @NonNull
    private List<BarEntry> criaListaDeBarras(@NonNull List<BigDecimal> lista) {
        List<BarEntry> listaDeBarras = new ArrayList<>();
        String valorEmString;
        BigDecimal bd;
        float valor;

        for (int i = 0; i < lista.size(); i++) {

            bd = lista.get(i).setScale(2, RoundingMode.HALF_EVEN);
            valorEmString = bd.toPlainString()
                    .replace(".", "");
            valorEmString = valorEmString.substring(0, valorEmString.length() - 2);

            valor = Float.parseFloat(valorEmString);
            BarEntry barra = new BarEntry(i, valor);
            listaDeBarras.add(barra);

        }
        return listaDeBarras;
    }

    private void configuraVisibilidadeDeBarras(@NonNull List<BigDecimal> listaDeValoresParaExibicao) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.resetAxisMaximum();

        if (listaDeValoresParaExibicao.size() > 6) {
            barChart.setVisibleXRange(6, 6);
            xAxis.setLabelCount(listaDeValoresParaExibicao.size());

        } else {
            barChart.setVisibleXRange(listaDeValoresParaExibicao.size(), 6);
            xAxis.setLabelCount(listaDeValoresParaExibicao.size());

        }

    }

    private void configuraValorDoEixoY(@NonNull List<BigDecimal> listaDeValoresParaExibicao) {
        BigDecimal maiorNDaLista = listaDeValoresParaExibicao.get(0);
        BigDecimal menorNDaLista = listaDeValoresParaExibicao.get(0);
        int compare;

        for (int i = 0; i < listaDeValoresParaExibicao.size() - 1; i++) {

            compare = maiorNDaLista.compareTo(listaDeValoresParaExibicao.get(i + 1));
            if (compare < 0) {
                maiorNDaLista = listaDeValoresParaExibicao.get(i + 1);
            }

            compare = menorNDaLista.compareTo(listaDeValoresParaExibicao.get(i + 1));
            if (compare > 0) {
                menorNDaLista = listaDeValoresParaExibicao.get(i + 1);
            }

        }

        BigDecimal divideMax = maiorNDaLista.divide(new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
        divideMax = divideMax.add(maiorNDaLista);
        String max = divideMax.toPlainString()
                .replace(".", "");
        max = max.substring(0, max.length() - 2);

        barChart.getAxisLeft().setAxisMaximum(Float.parseFloat(max));

        compare = menorNDaLista.compareTo(BigDecimal.ZERO);

        if (compare >= 0) {
            barChart.getAxisLeft().setAxisMinimum(0);
            barChart.setDrawValueAboveBar(true);

        } else {
            BigDecimal divideMin = menorNDaLista.divide(new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
            divideMin = divideMin.add(menorNDaLista);
            String min = divideMin.toPlainString()
                    .replace(".", "");
            min = min.substring(0, min.length() - 2);

            barChart.getAxisLeft().setAxisMinimum(Float.parseFloat(min));
            barChart.setDrawValueAboveBar(false);

        }

    }

    //------------------------------------- Metodos publicos ---------------------------------------

    public int getPosicao() {
        return posicao;
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
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
                menu.removeItem(R.id.menu_padrao_search);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_padrao_logout:
                        MensagemUtil.toast(requireContext(), "Logout");
                        break;

                    case R.id.menu_padrao_editar:
                        BottomDialogDesempenho dialog = new BottomDialogDesempenho(requireContext(), DesempenhoFragment.this);
                        dialog.showBottomDialog();
                        dialog.setCallback((tipo, ano, cavaloId) -> {
                            tipoDeRequisicao = tipo;
                            anoRequisitado = ano;
                            atualizaUiAposFiltragem(tipo, ano, cavaloId);
                        });
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

    private void atualizaUiAposFiltragem(TipoDeRequisicao tipo, int ano, int cavaloId) {
        if(cavaloId != 0){
            recyclerView.setVisibility(INVISIBLE);
            binding.detalhesPeriodo.setVisibility(INVISIBLE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            binding.detalhesPeriodo.setVisibility(VISIBLE);
        }

        atualizaLista(ano, tipo, cavaloId);
        atualizaUi(tipo, cavaloId);
        atualizaRecycler(tipo, ano, switchbtn.isChecked());
    }

    private void atualizaRecycler(TipoDeRequisicao tipo, int ano, boolean isCheched) {
        listaDeDetalhesDesempenho = getListaDeDetalhesDesempenho(ano, tipo, isCheched);
        adapter.atualiza(listaDeDetalhesDesempenho);
    }

}

*/

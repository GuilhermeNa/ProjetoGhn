package br.com.transporte.appGhn.ui.fragment.desempenho.helpers;

import android.graphics.Color;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedBarChartData;

public class DesempenhoBarChartHelper {
    private final BarChart barChart;
    private boolean indexSelecionado;
    private int posicao;
    private BarData barData;
    private BarDataSet dataSet;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DesempenhoBarChartHelper(BarChart barChart) {
        this.barChart = barChart;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    //----------------------------------------------------------------------------------------------
    //                                            Show                                            ||
    //----------------------------------------------------------------------------------------------

    public void configura() {
        configuraCLick();

        dataSet = new BarDataSet(new ArrayList<>(), " ");
        dataSet.setValueTextSize(10);
        dataSet.setBarBorderWidth(0.5f);

        barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);

        barChart.setDragEnabled(true);
        barChart.setData(barData);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setHighlightPerTapEnabled(true);
        barChart.setHighlightPerDragEnabled(false);
        barChart.invalidate();
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        final YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawLabels(false);
        axisRight.setEnabled(false);

        final XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new ArrayList<>()));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);

        final YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisLineWidth(1f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(5);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawLabels(true);
        yAxis.setGridColor(Color.GRAY);
    }

    @NonNull
    private List<BarEntry> criaListaDeBarras(@NonNull List<BigDecimal> lista) {
        final List<BarEntry> listaDeBarras = new ArrayList<>();
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

    private void configuraCLick() {
        indexSelecionado = false;

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
                indexSelecionado = true;
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
                if (indexSelecionado) {
                    int i = Math.round(e.getX());
                    setPosicao(i + 1);
                    indexSelecionado = false;
                }
                callback.clickSelecionandoMes(getPosicao());
            }

            @Override
            public void onNothingSelected() {
                setPosicao(0);
                callback.clickRemovendoSelecaoDeMes();
            }
        });

    }

    //----------------------------------------------------------------------------------------------
    //                                    Metodos Publicos                                        ||
    //----------------------------------------------------------------------------------------------

    public void atualiza(@NonNull final MappedBarChartData resourceMappedData) {
        dataSet.clear();
        barData.removeDataSet(dataSet);
        barChart.removeAllViews();
        barChart.clear();

        List<BarEntry> listaDeBarras = criaListaDeBarras(resourceMappedData.getListaDeValores());

        try {
            configuraValorDoEixoY(resourceMappedData.getListaDeValores());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        dataSet.setValues(listaDeBarras);
        barData.addDataSet(dataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(resourceMappedData.getListaDeMeses()));

        configuraVisibilidadeDeBarras(resourceMappedData.getListaDeValores());

        barChart.animateY(700);
        barChart.invalidate();
    }

    public interface Callback {
        void clickSelecionandoMes(int mesSelecionado);

        void clickRemovendoSelecaoDeMes();
    }

}

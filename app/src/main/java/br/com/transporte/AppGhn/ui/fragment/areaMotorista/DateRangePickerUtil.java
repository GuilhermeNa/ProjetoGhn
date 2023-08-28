package br.com.transporte.AppGhn.ui.fragment.areaMotorista;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

import br.com.transporte.AppGhn.util.DataUtil;

public class DateRangePickerUtil {
    public static final String DATA_RANGE = "DataRange";
    public static final String SELECIONE_O_PERIODO = "Selecione o periodo";
    private final FragmentManager fragmentManager;
    private static ZonedDateTime dataInicialEmUtc;
    private static ZonedDateTime dataFinalEmUtc;
    private CallbackDatePicker callbackDatePicker;

    public void setCallbackDatePicker(CallbackDatePicker callbackDatePicker) {
        this.callbackDatePicker = callbackDatePicker;
    }

    public DateRangePickerUtil(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    //----------------------------------------------------------------------------------------------
    //                                          Build                                             ||
    //----------------------------------------------------------------------------------------------

    public void build(@NonNull View view) {
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(SELECIONE_O_PERIODO)
                .setSelection(getSelection())
                .build();

        view.setOnClickListener(v -> {
            dateRangePicker.show(fragmentManager, DATA_RANGE);
            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                selecionaNovoDataRange(selection);
                callbackDatePicker.selecionaDataComSucesso(
                        getDataInicialEmLocalDate(),
                        getDataFinalEmLocalDate()
                );
            });
            dateRangePicker.addOnDismissListener(selection -> {
                dateRangePicker.clearOnPositiveButtonClickListeners();
                dateRangePicker.clearOnDismissListeners();
            });
        });
    }

    @NonNull
    private Pair<Long, Long> getSelection() {
        if (dataInicialEmUtc == null) {
            return new Pair<>(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
            );
        }
        return new Pair<>(
                dataInicialEmUtc.getLong(ChronoField.INSTANT_SECONDS),
                dataFinalEmUtc.getLong(ChronoField.INSTANT_SECONDS)
        );
    }

    private void selecionaNovoDataRange(@NonNull Pair<Long, Long> selection) {
        dataInicialEmUtc = Instant.ofEpochMilli(Long.parseLong(String.valueOf(selection.first)))
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC));

        dataFinalEmUtc = Instant.ofEpochMilli(Long.parseLong(String.valueOf(selection.second)))
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC));
    }

    //----------------------------------------------------------------------------------------------
    //                                          Metodos Publicos                                  ||
    //----------------------------------------------------------------------------------------------

    public LocalDate getDataInicialEmLocalDate() {
        if (dataInicialEmUtc == null)
            return DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
        else
            return dataInicialEmUtc.toLocalDate();
    }

    public LocalDate getDataFinalEmLocalDate() {
        if (dataFinalEmUtc == null)
            return DataUtil.capturaDataDeHojeParaConfiguracaoInicial();
        else
            return dataFinalEmUtc.toLocalDate();
    }

    //----------------------------------------------------------------------------------------------
    //                                          CallBack                                          ||
    //----------------------------------------------------------------------------------------------

    public interface CallbackDatePicker {
        void selecionaDataComSucesso(LocalDate dataInicial, LocalDate dataFinal);
    }

}

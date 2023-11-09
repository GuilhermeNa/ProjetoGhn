package br.com.transporte.appGhn.util;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataUtil {

    public static LocalDate formataDataParaPadraoPtBr(LocalDate data) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy", new Locale("pt-br"));
        String dataFormatadaEmString = formato.format(data);
        return ConverteDataUtil.stringParaData(dataFormatadaEmString);
    }

    public static LocalDate capturaDataDeHojeParaConfiguracaoInicial() {
        LocalDate data = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.todayInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();
        data = formataDataParaPadraoPtBr(data);

        return data;
    }

    public static LocalDate capturaPrimeiroDiaDoMesParaConfiguracaoInicial() {
        LocalDate data =
                Instant
                        .ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.thisMonthInUtcMilliseconds())))
                        .atZone(ZoneId.of("America/Sao_Paulo"))
                        .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                        .toLocalDate();
        data = formataDataParaPadraoPtBr(data);

        return data;
    }

    public static boolean verificaSeEstaNoRange(
            @NonNull LocalDate dataDoObjeto,
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {
        return !dataDoObjeto.isBefore(dataInicial) && !dataDoObjeto.isAfter(dataFinal);
    }


    public static LocalDate milliToLocalDate(final Long longValue) {
        return
                Instant
                        .ofEpochMilli(Long.parseLong(String.valueOf(longValue)))
                        .atZone(ZoneId.of("America/Sao_Paulo"))
                        .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                        .toLocalDate();
    }

    public static long localDateToMilli(@NonNull final LocalDate localDate) {
        final LocalDateTime dataEmTime =
                localDate.atTime(0, 0, 0);
        final ZoneId zona =
                ZoneId.of("America/Sao_Paulo");

        return dataEmTime
                .atZone(zona)
                .toInstant()
                .toEpochMilli();
    }

}

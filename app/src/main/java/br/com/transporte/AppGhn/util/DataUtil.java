package br.com.transporte.AppGhn.util;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataUtil {

    public static LocalDate formataDataParaPadraoPtBr(LocalDate data) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy", new Locale("pt-br"));
        String dataFormatadaEmString = formato.format(data);
        LocalDate dataEmFormatoPtBr = FormataDataUtil.stringParaData(dataFormatadaEmString);
        return dataEmFormatoPtBr;
    }

    public static LocalDate capturaDataDeHojeParaConfiguracaoinicial() {
        LocalDate data = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.todayInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();
        data = formataDataParaPadraoPtBr(data);

        return data;

    }

    public static LocalDate capturaPrimeiroDiaDoMesParaConfiguracaoInicial() {
        LocalDate data = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.thisMonthInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();
        data = formataDataParaPadraoPtBr(data);

        return data;
    }

    public static boolean capturaRange(
            @NonNull LocalDate dataDoObjeto,
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {
        return !dataDoObjeto.isBefore(dataInicial) && !dataDoObjeto.isAfter(dataFinal);
    }

}

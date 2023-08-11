package br.com.transporte.AppGhn.ui.adapter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class VencimentoUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int verificaQuantosDiasFaltam(LocalDate dataDoVencimento) {
        LocalDate dataDeHoje = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.todayInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();

        Period periodo = Period.between(dataDeHoje, dataDoVencimento);
        int anosEmDias = (periodo.getYears() * 360);
        int mesesEmDias = (periodo.getMonths() * 30);

        return (periodo.getDays() + mesesEmDias + anosEmDias);
    }

}

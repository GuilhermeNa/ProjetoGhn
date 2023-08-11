package br.com.transporte.AppGhn.util;

import android.os.Build;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.transporte.AppGhn.ui.dialog.AlteraSemiReboqueDoCavalo;

public class DatePickerUtil{

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate formataDataParaPadraoPtBr(LocalDate data) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy", new Locale("pt-br"));
        String dataFormatadaEmString = formato.format(data);
        LocalDate dataEmFormatoPtBr = FormataDataUtil.stringParaData(dataFormatadaEmString);
        return dataEmFormatoPtBr;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate capturaDataDeHojeParaConfiguracaoinicial() {
        LocalDate data = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.todayInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();
        data = formataDataParaPadraoPtBr(data);

        return data;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate capturaPrimeiroDiaDoMesParaConfiguracaoInicial() {
        LocalDate data = Instant.ofEpochMilli(Long.parseLong(String.valueOf(MaterialDatePicker.thisMonthInUtcMilliseconds()))).atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDate();
        data = formataDataParaPadraoPtBr(data);

        return data;
    }


}

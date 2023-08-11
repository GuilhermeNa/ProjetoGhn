package br.com.transporte.AppGhn.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class FormataDataUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String dataParaString(LocalDate data) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yy", new Locale("pt-br"));
        String dataFormatada = formatoData.format(data);
        return dataFormatada;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate stringParaData(String dataEmString) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate data = null;

        try {
            data = LocalDate.parse(dataEmString, formatoData);
        } catch (DateTimeParseException e) {
            e.printStackTrace();

            String dataHojeEmString = dataParaString(LocalDate.now());
            data = LocalDate.parse(dataHojeEmString, formatoData);

            return data;
        }
        return data;
    }


}

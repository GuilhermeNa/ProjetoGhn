package br.com.transporte.appGhn.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class ConverteDataUtil {

    public static String dataParaString(LocalDate data) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yy", new Locale("pt-br"));
        return formatoData.format(data);
    }

    public static LocalDate stringParaData(String dataEmString) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate data;

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

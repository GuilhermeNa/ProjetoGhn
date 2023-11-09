package br.com.transporte.appGhn.filtros;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.model.custos.CustosDeSalario;
import br.com.transporte.appGhn.util.DataUtil;

public class FiltraSalario {

    public static List<CustosDeSalario> listaPorData(@NonNull List<CustosDeSalario> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(c -> DataUtil.verificaSeEstaNoRange(c.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

}

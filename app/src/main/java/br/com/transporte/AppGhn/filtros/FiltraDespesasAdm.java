package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.DespesasAdmDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraDespesasAdm {
    private static final DespesasAdmDAO dao = new DespesasAdmDAO();

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static DespesaAdm localizaPeloId(int despesaId) throws ObjetoNaoEncontrado {
        DespesaAdm deslezaLocalizada = null;

        for (DespesaAdm d : dao.listaTodos()) {
            if (d.getId() == despesaId) {
                deslezaLocalizada = d;
            }
        }

        if (deslezaLocalizada != null) {
            return deslezaLocalizada;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<DespesaAdm> listaPorCavaloId(@NonNull List<DespesaAdm> dataSet, int cavaloId){
        return dataSet.stream()
                .filter(d -> d.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<DespesaAdm> listaPorData(@NonNull List<DespesaAdm> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(d -> DataUtil.capturaRange(d.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<DespesaAdm> listaPorTipo(@NonNull List<DespesaAdm> dataSet, TipoDespesa tipo){
        return dataSet.stream()
                .filter(d -> d.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<DespesaAdm> listaPorAno(@NonNull List<DespesaAdm> dataSet, int ano){
        return dataSet.stream()
                .filter(d -> d.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<DespesaAdm> listaPorMes(@NonNull List<DespesaAdm> dataSet, int mes){
        return dataSet.stream()
                .filter(d -> d.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }






}

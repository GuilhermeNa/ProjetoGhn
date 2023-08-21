package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.FreteDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraFrete {
    static FreteDAO dao = new FreteDAO();


    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Frete localizaPeloId(int freteId) throws ObjetoNaoEncontrado {
        Frete freteLocalizado = null;

        for (Frete f : dao.listaTodos()) {
            if (f.getId() == freteId) {
                freteLocalizado = f;
            }
        }

        if (freteLocalizado != null) {
            return freteLocalizado;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Frete> listaPorData(@NonNull List<Frete> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(f -> DataUtil.capturaRange(f.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Frete> listaPorCavaloId(@NonNull List<Frete> dataSet, int cavaloId) {
        return dataSet.stream()
                .filter(f -> f.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Frete> listaPorStatusDePagamentoDaComissao(@NonNull List<Frete> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(f -> f.getAdmFrete().isComissaoJaFoiPaga())
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(f -> !f.getAdmFrete().isComissaoJaFoiPaga())
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Frete> listaPorStatusDeRecebimentoDoFrete(@NonNull List<Frete> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(f -> f.getAdmFrete().isFreteJaFoiPago())
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(f -> !f.getAdmFrete().isFreteJaFoiPago())
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Frete> listaPorAno(@NonNull List<Frete> dataSet, int ano) {
        return dataSet.stream()
                .filter(f -> f.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Frete> listaPorMes(@NonNull List<Frete> dataSet, int mes) {
        return dataSet.stream()
                .filter(f -> f.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }



}

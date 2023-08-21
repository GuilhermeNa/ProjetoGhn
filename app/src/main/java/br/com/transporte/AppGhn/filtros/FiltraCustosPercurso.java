package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.CustosDePercursoDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.model.enums.TipoCustoDePercurso;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraCustosPercurso {
    private final static CustosDePercursoDAO dao = new CustosDePercursoDAO();

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static CustosDePercurso localizaPeloId(int custoId) throws ObjetoNaoEncontrado {
        CustosDePercurso custoLocalizado = null;

        for (CustosDePercurso c : dao.listaTodos()) {
            if (c.getId() == custoId) {
                custoLocalizado = c;
            }
        }

        if (custoLocalizado != null) {
            return custoLocalizado;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<CustosDePercurso> listaPorCavaloId(@NonNull List<CustosDePercurso> dataSet, int cavaloId) {
        return dataSet.stream()
                .filter(c -> c.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<CustosDePercurso> listaPorTipo(@NonNull List<CustosDePercurso> dataSet, TipoCustoDePercurso tipo) {
        return dataSet.stream()
                .filter(c -> c.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<CustosDePercurso> listaPorData(@NonNull List<CustosDePercurso> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(c -> DataUtil.capturaRange(c.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<CustosDePercurso> listaPorAno(@NonNull List<CustosDePercurso> dataSet, int ano){
        return dataSet.stream()
                .filter(c -> c.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<CustosDePercurso> listaPorMes(@NonNull List<CustosDePercurso> dataSet, int mes){
        return dataSet.stream()
                .filter(c -> c.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.CustosDeManutencaoDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraCustosManutencao {
    private static final CustosDeManutencaoDAO dao = new CustosDeManutencaoDAO();

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static CustosDeManutencao localizaPeloId(int manutencaoId) throws ObjetoNaoEncontrado {
        CustosDeManutencao manutencaoLocalizada = null;

        for (CustosDeManutencao c : dao.listaTodos()) {
            if (c.getId() == manutencaoId) {
                manutencaoLocalizada = c;
            }
        }

        if (manutencaoLocalizada != null) {
            return manutencaoLocalizada;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<CustosDeManutencao> listaPorCavaloId(@NonNull List<CustosDeManutencao> dataSet, int cavaloId){
        return dataSet.stream()
                .filter(c -> c.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<CustosDeManutencao> listaPorData(@NonNull List<CustosDeManutencao> dataSet, LocalDate dataInicial, LocalDate dataFinal){
        return dataSet.stream()
                .filter(c-> DataUtil.capturaRange(c.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<CustosDeManutencao> listaPorAno(@NonNull List<CustosDeManutencao> dataSet, int ano){
        return dataSet.stream()
                .filter(c -> c.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<CustosDeManutencao> listaPorMes(@NonNull List<CustosDeManutencao> dataSet, int mes){
        return dataSet.stream()
                .filter(c -> c.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }




}

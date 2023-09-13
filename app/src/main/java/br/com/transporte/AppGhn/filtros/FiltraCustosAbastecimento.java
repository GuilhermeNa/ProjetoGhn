package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.util.DataUtil;

public class FiltraCustosAbastecimento {
    private static final CustosDeAbastecimentoDAO dao = new CustosDeAbastecimentoDAO();

    @NonNull
    public static CustosDeAbastecimento localizaPeloId(int adiantamentoId) throws ObjetoNaoEncontrado {
        CustosDeAbastecimento abastecimentoLocalizado = null;

        for (CustosDeAbastecimento c : dao.listaTodos()) {
            if (c.getId() == adiantamentoId) {
                abastecimentoLocalizado = c;
            }
        }

        if (abastecimentoLocalizado != null) {
            return abastecimentoLocalizado;
        }

        throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public static List<CustosDeAbastecimento> listaPorCavaloId(@NonNull List<CustosDeAbastecimento> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(c -> c.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<CustosDeAbastecimento> listaPorData(@NonNull List<CustosDeAbastecimento> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(a -> DataUtil.verificaSeEstaNoRange(a.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<CustosDeAbastecimento> listaPorAno(@NonNull List<CustosDeAbastecimento> dataSet, int ano) {
        return dataSet.stream()
                .filter(c -> c.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<CustosDeAbastecimento> listaPorMes(@NonNull List<CustosDeAbastecimento> dataSet, int mes) {
        return dataSet.stream()
                .filter(c -> c.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

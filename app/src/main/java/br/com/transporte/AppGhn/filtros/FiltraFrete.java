package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

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

        throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public static List<Frete> listaPorData(@NonNull List<Frete> dataSet, LocalDate dataInicial, LocalDate dataFinal) {
        return dataSet.stream()
                .filter(f -> DataUtil.verificaSeEstaNoRange(f.getData(), dataInicial, dataFinal))
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorCavaloId(@NonNull List<Frete> dataSet, int cavaloId) {
        return dataSet.stream()
                .filter(f -> f.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorStatusDePagamentoDaComissao(@NonNull List<Frete> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(f -> f.getAdmFrete().isComissaoJaFoiPaga())
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(f -> !f.getAdmFrete().isComissaoJaFoiPaga())
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorStatusDeRecebimentoDoFrete(@NonNull List<Frete> dataSet, boolean isPago) {
        if (isPago) return dataSet.stream()
                .filter(f -> f.getAdmFrete().isFreteJaFoiPago())
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(f -> !f.getAdmFrete().isFreteJaFoiPago())
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorAno(@NonNull List<Frete> dataSet, int ano) {
        return dataSet.stream()
                .filter(f -> f.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<Frete> listaPorMes(@NonNull List<Frete> dataSet, int mes) {
        return dataSet.stream()
                .filter(f -> f.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

}

package br.com.transporte.appGhn.filtros;

import static br.com.transporte.appGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.appGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.model.enums.TipoRecebimentoFrete;

public class FiltraRecebimentoFrete {

    @NonNull
    public static RecebimentoDeFrete localizaPeloId(@NonNull List<RecebimentoDeFrete> dataSet, int recebimentoId) throws ObjetoNaoEncontrado {
        RecebimentoDeFrete recebimentoLocalizado = null;

        for (RecebimentoDeFrete r : dataSet) {
            if (r.getId() == recebimentoId) {
                recebimentoLocalizado = r;
            }
        }
        if (recebimentoLocalizado != null) return recebimentoLocalizado;
        else throw new ObjetoNaoEncontrado(OBJETO_NULL);
    }

    public static List<RecebimentoDeFrete> listaPeloIdDoFrete(@NonNull List<RecebimentoDeFrete> dataSet, long freteId) {
        return dataSet.stream()
                .filter(r -> r.getRefFreteId() == freteId)
                .collect(Collectors.toList());
    }

    public static BigDecimal valorTotalRecebido(@NonNull List<RecebimentoDeFrete> dataSet) {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);

        for (RecebimentoDeFrete r : dataSet) {
            total = total.add(r.getValor());
        }
        return total;
    }

    @NonNull
    public static RecebimentoDeFrete localizaPorTipo(@NonNull List<RecebimentoDeFrete> dataSet, TipoRecebimentoFrete tipo) throws ObjetoNaoEncontrado {
        RecebimentoDeFrete recebimentoLocalizado = null;

        for (RecebimentoDeFrete r : dataSet) {
            if (r.getTipoRecebimentoFrete() == tipo) {
                recebimentoLocalizado = r;
            }
        }

        if (recebimentoLocalizado != null) return recebimentoLocalizado;
        else throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

}

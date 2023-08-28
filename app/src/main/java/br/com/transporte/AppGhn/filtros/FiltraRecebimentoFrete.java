package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.RecebimentoFreteDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.model.enums.TipoRecebimentoFrete;

public class FiltraRecebimentoFrete {
    private static final RecebimentoFreteDAO dao = new RecebimentoFreteDAO();

    @NonNull
    public static RecebimentoDeFrete localizaPeloId(int recebimentoId) throws ObjetoNaoEncontrado {
        RecebimentoDeFrete recebimentoLocalizado = null;

        for (RecebimentoDeFrete r : dao.listaTodos()) {
            if (r.getId() == recebimentoId) {
                recebimentoLocalizado = r;
            }
        }

        if (recebimentoLocalizado != null) return recebimentoLocalizado;
        else throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public static List<RecebimentoDeFrete> listaPeloIdDoFrete(int freteId) {
        return dao.listaTodos().stream()
                .filter(r -> r.getRefFrete() == freteId)
                .collect(Collectors.toList());
    }

    public static BigDecimal valorTotalRecebido(int freteId) {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);

        for (RecebimentoDeFrete r : listaPeloIdDoFrete(freteId)) {
            total = total.add(r.getValor());
        }

        return total;
    }

    public static RecebimentoDeFrete localizaPorTipo(int freteId, TipoRecebimentoFrete tipo) throws ObjetoNaoEncontrado {
        RecebimentoDeFrete recebimentoLocalizado = null;

        for (RecebimentoDeFrete r : listaPeloIdDoFrete(freteId)) {
            if (r.getTipoRecebimentoFrete() == tipo) {
                recebimentoLocalizado = r;
            }
        }

        if (recebimentoLocalizado != null) return recebimentoLocalizado;
        else throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

}

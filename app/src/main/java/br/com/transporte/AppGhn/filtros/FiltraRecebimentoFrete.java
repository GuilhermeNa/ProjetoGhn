package br.com.transporte.AppGhn.filtros;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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
        RecebimentoDeFrete recebimentoLocalizada = null;

        for (RecebimentoDeFrete r : dao.listaTodos()) {
            if (r.getId() == recebimentoId) {
                recebimentoLocalizada = r;
            }
        }

        if (recebimentoLocalizada != null) {
            return recebimentoLocalizada;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<RecebimentoDeFrete> listaPeloIdDoFrete(int freteId) {
        return dao.listaTodos().stream()
                .filter(r -> r.getRefFrete() == freteId)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static RecebimentoDeFrete localizaAdiantamento(int freteId) throws ObjetoNaoEncontrado {
        RecebimentoDeFrete recebimentoLocalizado = null;

        for (RecebimentoDeFrete r : listaPeloIdDoFrete(freteId)) {
            if (r.getTipoRecebimentoFrete() == TipoRecebimentoFrete.ADIANTAMENTO) {
                recebimentoLocalizado = r;
            }
        }

        if (recebimentoLocalizado != null) {
            return recebimentoLocalizado;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static RecebimentoDeFrete localizaSaldo(int freteId) throws ObjetoNaoEncontrado {
        RecebimentoDeFrete recebimentoLocalizado = null;

        for (RecebimentoDeFrete r : listaPeloIdDoFrete(freteId)) {
            if (r.getTipoRecebimentoFrete() == TipoRecebimentoFrete.SALDO) {
                recebimentoLocalizado = r;
            }
        }

        if (recebimentoLocalizado != null) {
            return recebimentoLocalizado;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static BigDecimal valorTotalRecebido(int freteId) {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);

        for (RecebimentoDeFrete r : listaPeloIdDoFrete(freteId)) {
            total = total.add(r.getValor());
        }

        return total;
    }


}

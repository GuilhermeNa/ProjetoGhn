package br.com.transporte.AppGhn.ui.fragment.areaMotorista.resumo;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.AppGhn.dao.CustosDeAbastecimentoDAO;
import br.com.transporte.AppGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.util.CalculoUtil;

public class CalculaCardAbastecimento {

    public interface CalculaUiAbastecimentoCallback{
        void litragemTotal(BigDecimal valor);
        void abastecimentoTotal(BigDecimal valor);
        void kmPercorrido(BigDecimal valor);
    }

    public void run(
            final List<CustosDeAbastecimento> listaFiltradaRecebida,
            final List<CustosDeAbastecimento> listaTodosRecebida,
            @NonNull final CalculaUiAbastecimentoCallback callback
            ) {
        BigDecimal litragemTotal = CalculoUtil.somaLitragemTotal(listaFiltradaRecebida);
        BigDecimal abastecimentoTotal = CalculoUtil.somaCustosDeAbastecimento(listaFiltradaRecebida);
        BigDecimal kmPercorrido = ui_kmPercorrido(listaFiltradaRecebida, listaTodosRecebida);

        callback.litragemTotal(litragemTotal);
        callback.abastecimentoTotal(abastecimentoTotal);
        callback.kmPercorrido(kmPercorrido);
    }

    private BigDecimal ui_kmPercorrido(List<CustosDeAbastecimento> listaFiltradaRecebida, List<CustosDeAbastecimento> listaTodosRecebida) {
        BigDecimal kmPercorrido;
        try {
            kmPercorrido = calculaKmPercorrido(listaFiltradaRecebida, listaTodosRecebida);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            kmPercorrido = BigDecimal.ZERO;
        }
        return kmPercorrido;
    }

    private BigDecimal calculaKmPercorrido(List<CustosDeAbastecimento> listaFiltradaRecebida, List<CustosDeAbastecimento> listaTodosRecebida) {
        List<CustosDeAbastecimento> listaTodos = listaTodosRecebida;
        List<CustosDeAbastecimento> listaFiltrada = listaFiltradaRecebida;

        Comparator<CustosDeAbastecimento> comparing = Comparator.comparing(CustosDeAbastecimento::getMarcacaoKm);
        listaTodos.sort(comparing);
        listaFiltrada.sort(comparing);

        CustosDeAbastecimento dMenos1;
        CustosDeAbastecimento d0;
        CustosDeAbastecimento dMais1;

        if (listaFiltrada.size() == 0) {
            throw new IndexOutOfBoundsException("Lista filtrada não encontrada");
        } else if (listaTodos.size() == 0) {
            throw new IndexOutOfBoundsException("Lista total não encontrada");
        }
        d0 = listaFiltrada.get(0);
        dMais1 = listaFiltrada.get(listaFiltrada.size() - 1);
        int d0PosicaoGeral = listaTodos.indexOf(d0);

        try {
            dMenos1 = listaTodos.get(d0PosicaoGeral - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            dMenos1 = listaTodos.get(d0PosicaoGeral);
        }

        return dMais1.getMarcacaoKm().subtract(dMenos1.getMarcacaoKm());
    }


}

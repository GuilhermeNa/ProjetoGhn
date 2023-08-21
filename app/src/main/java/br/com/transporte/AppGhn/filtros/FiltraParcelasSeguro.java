package br.com.transporte.AppGhn.filtros;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.ParcelaDeSeguro;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class FiltraParcelasSeguro {
    private static final ParcelaDeSeguroDAO dao = new ParcelaDeSeguroDAO();

    @NonNull
    public static ParcelaDeSeguro localizaPeloId(int parcelaId) throws ObjetoNaoEncontrado {
        ParcelaDeSeguro parcelaLocalizada = null;

        for (ParcelaDeSeguro p : dao.listaTodos()) {
            if (p.getId() == parcelaId) {
                parcelaLocalizada = p;
            }
        }

        if (parcelaLocalizada != null) {
            return parcelaLocalizada;
        }

        throw new ObjetoNaoEncontrado("Objeto não localizado");

    }

    public static List<ParcelaDeSeguro> listaPeloIdDoSeguro(@NonNull List<ParcelaDeSeguro> dataSet, int seguroId) {
        return dataSet.stream()
                .filter(p -> p.getRefSeguro() == seguroId)
                .collect(Collectors.toList());
    }

    public static List<ParcelaDeSeguro> listaPeloCavaloId(@NonNull List<ParcelaDeSeguro> dataSet, int cavaloId) {
        return dataSet.stream()
                .filter(p -> p.getRefCavalo() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<ParcelaDeSeguro> listaPorAno(@NonNull List<ParcelaDeSeguro> dataSet, int ano){
        return dataSet.stream()
                .filter(p -> p.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<ParcelaDeSeguro> listaPorTipo(@NonNull List<ParcelaDeSeguro> dataSet, TipoDespesa tipo){
        return dataSet.stream()
                .filter(p -> p.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    public static List<ParcelaDeSeguro> listaPorMes(@NonNull List<ParcelaDeSeguro> dataSet, int mes){
        return dataSet.stream()
                .filter(p -> p.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

    public static List<ParcelaDeSeguro> listaPorStatusDePagamento(@NonNull List<ParcelaDeSeguro> dataSet, boolean isPago){
        if(isPago) return dataSet.stream()
                .filter(Parcela::isPaga)
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(p -> !p.isPaga())
                .collect(Collectors.toList());
    }




}

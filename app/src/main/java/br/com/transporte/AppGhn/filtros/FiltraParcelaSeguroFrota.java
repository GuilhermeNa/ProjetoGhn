package br.com.transporte.AppGhn.filtros;

import static br.com.transporte.AppGhn.filtros.ConstantesFiltros.OBJETO_NULL;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import br.com.transporte.AppGhn.dao.ParcelaDeSeguroDAO;
import br.com.transporte.AppGhn.exception.ObjetoNaoEncontrado;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.model.abstracts.Parcela;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class FiltraParcelaSeguroFrota {
    private static final ParcelaDeSeguroDAO dao = new ParcelaDeSeguroDAO();

    @NonNull
    public static Parcela_seguroFrota localizaPeloId(int parcelaId) throws ObjetoNaoEncontrado {
        Parcela_seguroFrota parcelaLocalizada = null;

        for (Parcela_seguroFrota p : dao.listaTodos()) {
            if (p.getId() == parcelaId) {
                parcelaLocalizada = p;
            }
        }

        if (parcelaLocalizada != null) {
            return parcelaLocalizada;
        }

        throw new ObjetoNaoEncontrado(OBJETO_NULL);

    }

    public static List<Parcela_seguroFrota> listaPeloIdDoSeguro(@NonNull List<Parcela_seguroFrota> dataSet, int seguroId) {
        return dataSet.stream()
                .filter(p -> p.getRefSeguro() == seguroId)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaPorCavaloId(@NonNull List<Parcela_seguroFrota> dataSet, Long cavaloId) {
        return dataSet.stream()
                .filter(p -> p.getRefCavaloId() == cavaloId)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaPorAno(@NonNull List<Parcela_seguroFrota> dataSet, int ano){
        return dataSet.stream()
                .filter(p -> p.getData().getYear() == ano)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaPorTipo(@NonNull List<Parcela_seguroFrota> dataSet, TipoDespesa tipo){
        return dataSet.stream()
                .filter(p -> p.getTipoDespesa() == tipo)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaDoMesSolicitado(@NonNull List<Parcela_seguroFrota> dataSet, int mes){
        return dataSet.stream()
                .filter(p -> p.getData().getMonthValue() == mes)
                .collect(Collectors.toList());
    }

    public static List<Parcela_seguroFrota> listaPorStatusDePagamento(@NonNull List<Parcela_seguroFrota> dataSet, boolean isPago){
        if(isPago) return dataSet.stream()
                .filter(Parcela::isPaga)
                .collect(Collectors.toList());

        return dataSet.stream()
                .filter(p -> !p.isPaga())
                .collect(Collectors.toList());
    }

}

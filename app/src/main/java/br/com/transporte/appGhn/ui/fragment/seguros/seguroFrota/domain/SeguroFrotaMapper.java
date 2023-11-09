package br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.domain;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.domain.model.DespesaSeguroFrotaObject;

public class SeguroFrotaMapper {
    private final List<Cavalo> listaDeCavalos;
    private final List<DespesaComSeguroFrota> listaDeSeguros;

    public SeguroFrotaMapper(
            List<Cavalo> cavalos,
            List<DespesaComSeguroFrota> listaDeSeguros
    ) {
        this.listaDeCavalos = cavalos;
        this.listaDeSeguros = listaDeSeguros;
    }

    //----------------------------------------------------------------------------------------------

    public List<DespesaSeguroFrotaObject> geraListaDeSeguroFrotaObjetos() {
        final List<DespesaSeguroFrotaObject> listaDeObjetosMapeados = new ArrayList<>();

        final HashMap<Long, String> hashMapComDataCavalosNecessaria =
                transformaAListaDeCavalosEmHashMapParaFacilitarABuscaQuandoNecessario();

        paraCadaSeguroFrotaCriaUmObjetoMapParaInserirNaLista(
                listaDeObjetosMapeados);

        paraCadaObjetoInserePlacaDoCavaloQueGerouAquelaDespesa(
                listaDeObjetosMapeados, hashMapComDataCavalosNecessaria);

        return listaDeObjetosMapeados;
    }

    @NonNull
    private HashMap<Long, String> transformaAListaDeCavalosEmHashMapParaFacilitarABuscaQuandoNecessario() {
        final HashMap<Long, String> hashMap = new HashMap<>();
        for (Cavalo c : listaDeCavalos) {
            hashMap.put(c.getId(), c.getPlaca());
        }
        return hashMap;
    }

    private void paraCadaSeguroFrotaCriaUmObjetoMapParaInserirNaLista(
            final List<DespesaSeguroFrotaObject> listaObjetosMapeados
    ) {
        for (DespesaComSeguroFrota seguro : listaDeSeguros) {
            listaObjetosMapeados.add(
                    new DespesaSeguroFrotaObject(
                            seguro.getId(),
                            seguro.getRefCavaloId(),
                            seguro.getValorDespesa(),
                            seguro.getDataInicial(),
                            seguro.getDataFinal()
                    ));
        }
    }

    @Contract(pure = true)
    private void paraCadaObjetoInserePlacaDoCavaloQueGerouAquelaDespesa(
            @NonNull final List<DespesaSeguroFrotaObject> listaObjetosMapeados,
            final HashMap<Long, String> hashMapComDataCavalosNecessaria
    ) {
        for (DespesaSeguroFrotaObject seguroObject : listaObjetosMapeados) {
            final Long idCavalo = seguroObject.getIdCavalo();
            final String placa = hashMapComDataCavalosNecessaria.get(idCavalo);
            seguroObject.setPlaca(placa);
        }
    }

}

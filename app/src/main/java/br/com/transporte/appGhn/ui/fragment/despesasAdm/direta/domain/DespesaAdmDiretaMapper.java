package br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.domain;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.domain.model.DespesaAdmDiretaObject;

public class DespesaAdmDiretaMapper {
    private final List<Cavalo> listaDeCavalos;
    private final List<DespesaAdm> listaDeDespesasAdm;

    public DespesaAdmDiretaMapper(
            List<Cavalo> cavalos,
            List<DespesaAdm> despesasAdm
    ) {
        this.listaDeCavalos = cavalos;
        this.listaDeDespesasAdm = despesasAdm;
    }

    //----------------------------------------------------------------------------------------------

    public List<DespesaAdmDiretaObject> geraListaDeDespesaAdmObjetos() {
        final List<DespesaAdmDiretaObject> listaDeObjetosMapeados = new ArrayList<>();

        final HashMap<Long, String> hashMapComDataCavalosNecessaria =
                transformaAListaDeCavalosEmHashMapParaFacilitarABuscaQuandoNecessario();

        paraCadaDespesaAdmCriaUmDespesaObjetoMapParaInserirNaLista(
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

    private void paraCadaDespesaAdmCriaUmDespesaObjetoMapParaInserirNaLista(
            final List<DespesaAdmDiretaObject> listaObjetosMapeados
    ) {
        for (DespesaAdm despesaAdm : listaDeDespesasAdm) {
            listaObjetosMapeados.add(
                    new DespesaAdmDiretaObject(
                            despesaAdm.getId(),
                            despesaAdm.getRefCavaloId(),
                            despesaAdm.getValorDespesa(),
                            despesaAdm.getData(),
                            despesaAdm.getDescricao()
                    ));
        }
    }

    @Contract(pure = true)
    private void paraCadaObjetoInserePlacaDoCavaloQueGerouAquelaDespesa(
            @NonNull final List<DespesaAdmDiretaObject> listaObjetosMapeados,
            final HashMap<Long, String> hashMapComDataCavalosNecessaria
    ) {
        for (DespesaAdmDiretaObject despesaObjeto : listaObjetosMapeados) {
            final Long idCavalo = despesaObjeto.getIdCavalo();
            final String placa = hashMapComDataCavalosNecessaria.get(idCavalo);
            despesaObjeto.setPlaca(placa);
        }
    }

}

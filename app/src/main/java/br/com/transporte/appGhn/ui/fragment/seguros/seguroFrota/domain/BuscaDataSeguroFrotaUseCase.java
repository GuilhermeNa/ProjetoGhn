package br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.SeguroFrotaRepository;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.domain.model.DespesaSeguroFrotaObject;

public class BuscaDataSeguroFrotaUseCase {
    private final CavaloRepository cavaloRepository;
    private final SeguroFrotaRepository seguroFrotaRepository;
    private final LifecycleOwner lifecycleOwner;

    public BuscaDataSeguroFrotaUseCase(Context context, LifecycleOwner lifecycleOwner) {
        this.cavaloRepository = new CavaloRepository(context);
        this.seguroFrotaRepository = new SeguroFrotaRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface SeguroFrotaObjectsUseCaseCallback {
        void finalizaMapper(List<DespesaSeguroFrotaObject> dataSet);
    }

    //----------------------------------------------------------------------------------------------

    public void buscaDataRelacionadaAoSeguroFrota(final SeguroFrotaObjectsUseCaseCallback callback){
        cavaloRepository.buscaCavalos().observe(lifecycleOwner,
                cavalos -> {
                    if (cavalos.getDado() != null) {
                        buscaListaDeSegurosAtivos(cavalos.getDado(), callback);
                    }
                });
    }

    private void buscaListaDeSegurosAtivos(
            final List<Cavalo> cavalos,
            final SeguroFrotaObjectsUseCaseCallback callback
    ) {
        seguroFrotaRepository.buscaPorStatus(true).observe(lifecycleOwner,
                listaSeguro -> {
                    if(listaSeguro != null){
                        mapeiaListaDeSeguroFrotaObjetosComDataRelevanteParaUi(cavalos, listaSeguro, callback);
                    }
                });
    }

    private static void mapeiaListaDeSeguroFrotaObjetosComDataRelevanteParaUi(
            final List<Cavalo> cavalos,
            final List<DespesaComSeguroFrota> seguro,
            @NonNull final SeguroFrotaObjectsUseCaseCallback callback
    ) {
        final SeguroFrotaMapper mapper =
                new SeguroFrotaMapper(cavalos, seguro);

        final List<DespesaSeguroFrotaObject> listaSeguroFrotaObject
                = mapper.geraListaDeSeguroFrotaObjetos();

        callback.finalizaMapper(listaSeguroFrotaObject);
    }

}

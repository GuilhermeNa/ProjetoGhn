package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalParcelaSeguroFrotaDataSource;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;

public class ParcelaSeguroFrotaRepository {
    private final LocalParcelaSeguroFrotaDataSource localDataSource;

    public ParcelaSeguroFrotaRepository(Context context) {
        this.localDataSource = new LocalParcelaSeguroFrotaDataSource(context);
    }

    //---------------------------------------------------------------------------------------------

    private final MediatorLiveData<List<Parcela_seguroFrota>> mediator = new MediatorLiveData<>();

    public LiveData<List<Parcela_seguroFrota>> buscaPorSeguroId(final Long seguroId) {
        mediator.addSource(
                localDataSource.buscaParcelasPorSeguroId(seguroId),
                mediator::setValue
        );
        return mediator;
    }

    public LiveData<Long> edita(final Parcela_seguroFrota parcela) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.edita(parcela,
                () -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<Long> adiciona(final Parcela_seguroFrota parcela) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.adiciona(parcela, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long resultado) {
                liveData.setValue(resultado);
            }

            @Override
            public void falha(String msg) {

            }
        });
        return liveData;
    }


    public LiveData<Void> adicionaLista(final List<Parcela_seguroFrota> listaParcelas) {
        final MutableLiveData<Void> liveData = new MutableLiveData<>();
        localDataSource.adicionaLista(listaParcelas,
                () -> {
                   liveData.setValue(null);
                });
        return liveData;
    }
}

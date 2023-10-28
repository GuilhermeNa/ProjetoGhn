package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalParcelaSeguroVidaDataSource;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;

public class ParcelaSeguroVidaRepository {
    private final LocalParcelaSeguroVidaDataSource localDataSource;

    public ParcelaSeguroVidaRepository(Context context) {
        this.localDataSource = new LocalParcelaSeguroVidaDataSource(context);
    }

    //---------------------------------------------------------------------------------------------

    private final MediatorLiveData<List<Parcela_seguroVida>> mediator = new MediatorLiveData<>();

    public LiveData<List<Parcela_seguroVida>> buscaPorSeguroId(final Long seguroId) {
        mediator.addSource(
                localDataSource.buscaParcelasPorSeguroId(seguroId),
                mediator::setValue
        );
        return mediator;
    }

    public LiveData<Long> edita(final Parcela_seguroVida parcela) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.edita(parcela,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<Void> adicionaLista(final List<Parcela_seguroVida> parcelasDoSeguro) {
        final MutableLiveData<Void> liveData = new MutableLiveData<>();
        localDataSource.adicionaLista(parcelasDoSeguro,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

}

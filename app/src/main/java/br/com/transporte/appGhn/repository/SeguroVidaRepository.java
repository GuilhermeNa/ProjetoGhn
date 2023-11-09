package br.com.transporte.appGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.transporte.appGhn.dataSource.local.LocalSeguroVidaDataSource;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;

public class SeguroVidaRepository {
    private final LocalSeguroVidaDataSource localSource;

    public SeguroVidaRepository(Context context) {
        this.localSource = new LocalSeguroVidaDataSource(context);
    }

    //----------------------------------------------------------------------------------------------

    final MediatorLiveData<List<DespesaComSeguroDeVida>> mediator = new MediatorLiveData<>();

    public LiveData<List<DespesaComSeguroDeVida>> buscaPorStatus(boolean isValido) {
        mediator.addSource(localSource.buscaPorStatus(isValido),
                mediator::setValue
        );
        return mediator;
    }


    public LiveData<DespesaComSeguroDeVida> localizaPeloId(final Long id) {
        return localSource.localizaPeloId(id);
    }

    public LiveData<Long> edita(final DespesaComSeguroDeVida seguro) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localSource.edita(seguro,
                () -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<Long> adiciona(final DespesaComSeguroDeVida seguro) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localSource.adiciona(seguro, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long id) {
                liveData.setValue(id);
            }

            @Override
            public void falha(String msg) {
            }
        });
        return liveData;
    }

    public LiveData<String> deleta(final DespesaComSeguroDeVida seguro) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        localSource.deleta(seguro, new RepositoryCallback<String>() {
            @Override
            public void sucesso(String resultado) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String msg) {
            }
        });
        return liveData;
    }

}

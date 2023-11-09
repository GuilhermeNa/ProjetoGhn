package br.com.transporte.appGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.appGhn.dataSource.local.LocalFreteDataSource;
import br.com.transporte.appGhn.model.Frete;

public class FreteRepository {
    private final LocalFreteDataSource localDataSource;
    private final MediatorLiveData<Resource<List<Frete>>> mediator = new MediatorLiveData<>();

    public FreteRepository(Context context) {
        this.localDataSource = new LocalFreteDataSource(context);

    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Long> adicionaFrete(final Frete frete) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.adicionaFrete(frete, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long id) {
                liveData.setValue(id);
            }

            @Override
            public void falha(String ignore) {
            }
        });
        return liveData;
    }

    public LiveData<Long> editaFrete(final Frete frete) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.editaFrete(frete,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<String> deletaFrete(final Frete frete) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        localDataSource.deletaFrete(frete, new RepositoryCallback<String>() {
            @Override
            public void sucesso(String ignore) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String msg) {
                liveData.setValue(msg);
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<Frete>>> buscaFretes() {
        mediator.addSource(localDataSource.buscaFretes(),
                fretes -> mediator.setValue(new Resource<>(fretes, null)));
        return mediator;
    }

    public LiveData<Resource<List<Frete>>> buscaFretesPorCavaloId(final long cavaloId) {
        mediator.addSource(localDataSource.buscaFretesPorCavaloId(cavaloId),
                fretes -> mediator.setValue(new Resource<>(fretes, null)));
        return mediator;
    }

    public LiveData<Frete> localizaFrete(final long id) {
        return localDataSource.localizaFrete(id);
    }

    public LiveData<List<Frete>> bucaFretePorCavaloEData(
            final Long id,
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        final MutableLiveData<List<Frete>> liveData = new MutableLiveData<>();
        localDataSource.bucaFretePorCavaloEData(id, dataInicial, dataFinal, new RepositoryCallback<List<Frete>>() {
            @Override
            public void sucesso(List<Frete> resultado) {
                liveData.setValue(resultado);
            }

            @Override
            public void falha(String msg) {

            }
        });
        return liveData;
    }

}

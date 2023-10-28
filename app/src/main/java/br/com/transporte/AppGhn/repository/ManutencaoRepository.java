package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalManutencaoDataSource;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;

public class ManutencaoRepository {
    private final LocalManutencaoDataSource localDataSource;

    public ManutencaoRepository(Context context) {
        localDataSource = new LocalManutencaoDataSource(context);
    }

    //----------------------------------------------------------------------------------------------

    final MediatorLiveData<Resource<List<CustosDeManutencao>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<CustosDeManutencao>>> buscaManutencaoPorCavaloId(final long cavaloId) {
        mediator.addSource(localDataSource.buscaManutencaoPorCavaloId_room(cavaloId),
                listaManutencao -> {
                    if (listaManutencao != null) {
                        mediator.setValue(new Resource<>(listaManutencao, null));
                    } else {
                        mediator.setValue(new Resource<>(null, "Não foi possível carregar a lista"));
                    }
                });
        return mediator;
    }

    public LiveData<CustosDeManutencao> localizaManutencao(final long id) {
        return localDataSource.localizaManutencao_room(id);
    }

    public LiveData<Long> atualizaManutencao(final CustosDeManutencao manutencao) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.atualizaManutencao_room(manutencao,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<Long> adicionaManutencao(final CustosDeManutencao manutencao) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.adicionaManutencao_room(manutencao, new RepositoryCallback<Long>() {
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

    public LiveData<String> deletaManutencao(final CustosDeManutencao manutencao) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        localDataSource.deletaManutencao_room(manutencao,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    //----------------------------------------------------------------------------------------------


}

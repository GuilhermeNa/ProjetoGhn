package br.com.transporte.AppGhn.repository;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.tasks.frete.AdicionaFreteTask;
import br.com.transporte.AppGhn.tasks.frete.AtualizaFreteTask;
import br.com.transporte.AppGhn.tasks.frete.DeletaFreteTask;

public class FreteRepository {
    private final RoomFreteDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public FreteRepository(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomFreteDao();
        final GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<Frete>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<Frete>>> buscaFretes() {
        mediator.addSource(buscaFretes_room(),
                fretes -> {
                    mediator.setValue(new Resource<>(fretes, null));
                });
        return mediator;
    }

    public LiveData<Resource<List<Frete>>> buscaFretesPorCavaloId(final long cavaloId) {
        mediator.addSource(buscaFretesPorCavaloId_room(cavaloId),
                fretes -> {
                    mediator.setValue(new Resource<>(fretes, null));
                });
        return mediator;
    }

    public LiveData<Resource<List<Frete>>> buscaFretesPorStatusDeRecebimento(final boolean isRecebido){
        mediator.addSource(buscaFretesPorStatusRecebimento_room(isRecebido),
                fretes -> {
                    mediator.setValue(new Resource<>(fretes, null));
                });
        return mediator;
    }

    public LiveData<Frete> localizaFrete(final long id) {
        return localizaFrete_room(id);
    }

    public LiveData<Long> adicionaFrete(final Frete frete) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaFrete_room(frete, new RepositoryCallback<Long>() {
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
        editaFrete_room(frete,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<String> deletaFrete(final Frete frete) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        deletaFrete_room(frete, new RepositoryCallback<String>() {
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

    //----------------------------------------------------------------------------------------------
    //                                       Busca Interna                                        ||
    //----------------------------------------------------------------------------------------------

    private LiveData<List<Frete>> buscaFretes_room() {
        return dao.todos();
    }

    private LiveData<List<Frete>> buscaFretesPorCavaloId_room(final long cavaloId) {
        return dao.listaPorCavaloId(cavaloId);
    }

    private LiveData<Frete> localizaFrete_room(final long id) {
        return dao.localizaPeloId(id);
    }

    private void adicionaFrete_room(
            final Frete frete,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaFreteTask task = new AdicionaFreteTask(executor, handler);
        task.solicitaAdicao(frete, dao,
                callback::sucesso
        );
    }

    private void editaFrete_room(
            final Frete frete,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaFreteTask task = new AtualizaFreteTask(executor, handler);
        task.solicitaAtualizacao(dao, frete,
                callback::quandoFinaliza);
    }

    private void deletaFrete_room(
            final Frete frete,
            @NonNull final RepositoryCallback<String> callback
    ) {
        final DeletaFreteTask task = new DeletaFreteTask(executor, handler);
        if (frete != null) {
            task.solicitaRemocao(dao, frete,
                    () -> callback.sucesso(null)
            );
        } else {
            callback.falha("Falha ao remover");
        }
    }

    private LiveData<List<Frete>> buscaFretesPorStatusRecebimento_room(final boolean isRecebido){
        return dao.listaPorStatusDeRecebimento(isRecebido);
    }

}

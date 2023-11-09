package br.com.transporte.appGhn.repository;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.tasks.reboque.AdicionaReboqueTask;
import br.com.transporte.appGhn.tasks.reboque.AtualizaReboqueTask;
import br.com.transporte.appGhn.tasks.reboque.DeletaReboqueTask;

public class ReboqueRepository {
    private final RoomSemiReboqueDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public ReboqueRepository(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomReboqueDao();
        GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<SemiReboque>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<SemiReboque>>> buscaReboques() {
        mediator.addSource(buscaReboques_room(),
                listaReboques ->
                        mediator.setValue(new Resource<>(listaReboques, null)));
        return mediator;
    }

    public LiveData<SemiReboque> localizaReboque(final long reboqueId) {
        return localizaReboque_room(reboqueId);
    }

    public LiveData<Long> adicionaReboque(final SemiReboque reboque) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaReboque_room(reboque, new RepositoryCallback<Long>() {
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

    public LiveData<Long> substituiReboque(final SemiReboque reboque) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        substituiReboque_room(reboque,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<String> deletaReboque(final SemiReboque reboque) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        deletaReboque_room(reboque, new RepositoryCallback<String>() {
            @Override
            public void sucesso(String ignore) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String erro) {
                liveData.setValue(erro);
            }
        });
        return liveData;
    }

    //----------------------------------------------------------------------------------------------
    //                                       Busca Interna                                        ||
    //----------------------------------------------------------------------------------------------

    private LiveData<List<SemiReboque>> buscaReboques_room() {
        return dao.todos();
    }

    private LiveData<SemiReboque> localizaReboque_room(final long id) {
        return dao.localizaPeloId(id);
    }

    private void adicionaReboque_room(
            final SemiReboque reboque,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaReboqueTask task = new AdicionaReboqueTask(executor, handler);
        task.solicitaAdicao(dao, reboque,
                callback::sucesso
        );
    }

    private void substituiReboque_room(
            final SemiReboque reboque,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaReboqueTask task = new AtualizaReboqueTask(executor, handler);
        task.solicitaAtualizacao(dao, reboque,
                callback::quandoFinaliza
        );
    }

    private void deletaReboque_room(
            final SemiReboque reboque,
            final RepositoryCallback<String> callback
    ) {
        final DeletaReboqueTask task = new DeletaReboqueTask(executor, handler);
        if (reboque != null) {
            task.solicitaAtualizacao(dao, reboque,
                    () -> callback.sucesso(null)
            );
        } else {
            callback.falha("Falha ao remover");
        }
    }

}

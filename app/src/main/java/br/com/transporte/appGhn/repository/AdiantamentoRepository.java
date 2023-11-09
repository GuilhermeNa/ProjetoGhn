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
import br.com.transporte.appGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.tasks.adiantamento.AdicionaAdiantamentoTask;
import br.com.transporte.appGhn.tasks.adiantamento.AlteraAdiantamentoTask;
import br.com.transporte.appGhn.tasks.adiantamento.DeletaAdiantamentoTask;

public class AdiantamentoRepository {
    private final RoomAdiantamentoDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public AdiantamentoRepository(final Context context) {
        dao = GhnDataBase.getInstance(context).getRoomAdiantamentoDao();
        final GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<Adiantamento>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<Adiantamento>>> buscaAdiantamentos() {
        mediator.addSource(buscaAdiantamentos_room(),
                listaAdiantamentos ->
                        mediator.setValue(new Resource<>(listaAdiantamentos, null)));
        return mediator;
    }

    public LiveData<Resource<List<Adiantamento>>> buscaAdiantamentosPorCavaloId(final long cavaloId) {
        mediator.addSource(buscaAdiantamentosPorCavaloId_room(cavaloId),
                listaAdiantamentos ->
                        mediator.setValue(new Resource<>(listaAdiantamentos, null)));
        return mediator;
    }

    public LiveData<Adiantamento> localizaAdiantamento(final Long id) {
        return localizaAdiantamento_room(id);
    }

    @NonNull
    public LiveData<Long> adiciona(final Adiantamento adiantamento) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaAdiantamento_room(adiantamento, new RepositoryCallback<Long>() {
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

    public LiveData<Long> altera(final Adiantamento adiantamento) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        alteraAdiantamento_room(adiantamento,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<String> deleta(final Adiantamento adiantamento) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        deletaAdiantamento_room(adiantamento,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    //----------------------------------------------------------------------------------------------

    private LiveData<List<Adiantamento>> buscaAdiantamentos_room() {
        return dao.todos();
    }

    private LiveData<List<Adiantamento>> buscaAdiantamentosPorCavaloId_room(final long cavaloId) {
        return dao.listaPorCavaloId(cavaloId);
    }

    private LiveData<Adiantamento> localizaAdiantamento_room(final Long id) {
        return dao.localizaPeloId(id);
    }

    private void adicionaAdiantamento_room(
            final Adiantamento adiantamento,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaAdiantamentoTask task = new AdicionaAdiantamentoTask(executor, handler);
        task.solicitaAdicao(dao, adiantamento,
                callback::sucesso
        );
    }

    private void alteraAdiantamento_room(
            final Adiantamento adiantamento,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AlteraAdiantamentoTask task = new AlteraAdiantamentoTask(executor, handler);
        task.solicitaAlteracao(dao, adiantamento,
                callback::quandoFinaliza
        );
    }

    private void deletaAdiantamento_room(
            final Adiantamento adiantamento,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final DeletaAdiantamentoTask task = new DeletaAdiantamentoTask(executor, handler);
        task.solicitaRemocao(dao, adiantamento,
                callback::quandoFinaliza
        );
    }

}

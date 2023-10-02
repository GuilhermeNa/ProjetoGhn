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
import br.com.transporte.AppGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.AppGhn.model.RecebimentoDeFrete;
import br.com.transporte.AppGhn.tasks.TaskCallback;
import br.com.transporte.AppGhn.tasks.recebimentoFrete.AdicionaRecebimentoTask;
import br.com.transporte.AppGhn.tasks.recebimentoFrete.DeletaRecebimentoTask;
import br.com.transporte.AppGhn.tasks.recebimentoFrete.SubstituiRecebimentoTask;

public class RecebimentoDeFreteRepository {
    private final RoomRecebimentoFreteDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public RecebimentoDeFreteRepository(final Context context) {
        dao = GhnDataBase.getInstance(context).getRoomRecebimentoFreteDao();
        final GhnApplication application = new GhnApplication();
        handler = application.getMainThreadHandler();
        executor = application.getExecutorService();
    }

    //----------------------------------------------------------------------------------------------

    final MediatorLiveData<Resource<List<RecebimentoDeFrete>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<RecebimentoDeFrete>>> buscaRecebimentos() {
        mediator.addSource(buscaRecebimentos_room(),
                recebimentoDeFretes -> {
                    mediator.setValue(new Resource<>(recebimentoDeFretes, null));
                });
        return mediator;
    }

    public LiveData<List<RecebimentoDeFrete>> buscaRecebimentosPorFreteId(final Long freteId) {
        return buscaRecebimentosPorFreteId_room(freteId);
    }

    public LiveData<RecebimentoDeFrete> localizaPeloId(final Long id) {
        return localizaPeloId_room(id);
    }

    public LiveData<Long> substitui(final RecebimentoDeFrete recebimento) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        substitui_room(recebimento,
                () -> {
                    liveData.setValue(null);
                });

        return liveData;
    }

    public LiveData<Long> adiciona(final RecebimentoDeFrete recebimento) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adiciona_room(recebimento, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long id) {
                liveData.setValue(id);
            }

            @Override
            public void falha(String msg) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public LiveData<String> deleta(final RecebimentoDeFrete recebimentoArmazenado) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        deleta_room(recebimentoArmazenado, new RepositoryCallback<String>() {
            @Override
            public void sucesso(String ignore) {

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

    private LiveData<List<RecebimentoDeFrete>> buscaRecebimentos_room() {
        return dao.todos();
    }

    private LiveData<List<RecebimentoDeFrete>> buscaRecebimentosPorFreteId_room(final Long freteId) {
        return dao.listaPorFreteId(freteId);
    }

    private LiveData<RecebimentoDeFrete> localizaPeloId_room(final Long recebimentoId) {
        return dao.localizaPeloId(recebimentoId);
    }

    private void deleta_room(
            final RecebimentoDeFrete recebimento,
            final RepositoryCallback<String> callback
    ) {
        final DeletaRecebimentoTask task = new DeletaRecebimentoTask(executor, handler);
        if (recebimento != null) {
            task.solicitaRemocao(dao, recebimento,
                    () -> callback.sucesso(null)
            );
        } else {
            callback.falha("Falha ao remover");
        }
    }

    private void adiciona_room(
            final RecebimentoDeFrete recebimento,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaRecebimentoTask task = new AdicionaRecebimentoTask(executor, handler);
        task.solicitaAdicaoTask(recebimento, dao,
                callback::sucesso
        );
    }

    private void substitui_room(
            final RecebimentoDeFrete recebimento,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final SubstituiRecebimentoTask task = new SubstituiRecebimentoTask(executor, handler);
        task.solicitaSubstituicaoTask(recebimento, dao,
                callback::quandoFinaliza
        );
    }

}

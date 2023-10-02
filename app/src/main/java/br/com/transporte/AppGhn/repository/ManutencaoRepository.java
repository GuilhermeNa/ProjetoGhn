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
import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.tasks.manutencao.AdicionaManutencaoTask;
import br.com.transporte.AppGhn.tasks.manutencao.AtualizaManutencaoTask;
import br.com.transporte.AppGhn.tasks.manutencao.DeletaManutencaoTask;

public class ManutencaoRepository {
    private final RoomCustosDeManutencaoDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public ManutencaoRepository(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomCustosDeManutencaoDao();
        final GhnApplication app = new GhnApplication();
        executor = app.getExecutorService();
        handler = app.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    final MediatorLiveData<Resource<List<CustosDeManutencao>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<CustosDeManutencao>>> buscaManutencaoPorCavaloId(final long cavaloId) {
        mediator.addSource(buscaManutencaoPorCavaloId_room(cavaloId),
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
        return localizaManutencao_room(id);
    }

    public LiveData<Long> atualizaManutencao(final CustosDeManutencao manutencao) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        atualizaManutencao_room(manutencao,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<Long> adicionaManutencao(final CustosDeManutencao manutencao) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaManutencao_room(manutencao, new RepositoryCallback<Long>() {
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
        deletaManutencao_room(manutencao,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    //----------------------------------------------------------------------------------------------

    private LiveData<List<CustosDeManutencao>> buscaManutencaoPorCavaloId_room(final long cavaloId) {
        return dao.listaPeloCavaloId(cavaloId);
    }

    private LiveData<CustosDeManutencao> localizaManutencao_room(final long id) {
        return dao.localizaPeloId(id);
    }

    private void atualizaManutencao_room(
            final CustosDeManutencao manutencao,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaManutencaoTask task = new AtualizaManutencaoTask(executor, handler);
        task.solicitaAtualizacao(dao, manutencao,
                callback::quandoFinaliza
        );
    }

    private void adicionaManutencao_room(
            final CustosDeManutencao manutencao,
            final RepositoryCallback<Long> callback
    ) {
        final AdicionaManutencaoTask task = new AdicionaManutencaoTask(executor, handler);
        task.solicitaAdicao(dao, manutencao,
                id -> {
                    if (id > 0) {
                        callback.sucesso(id);
                    }
                });
    }


    private void deletaManutencao_room(
            final CustosDeManutencao manutencao,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final DeletaManutencaoTask task = new DeletaManutencaoTask(executor, handler);
        task.solicitaRemocao(dao, manutencao,
                callback::quandoFinaliza
        );
    }

}

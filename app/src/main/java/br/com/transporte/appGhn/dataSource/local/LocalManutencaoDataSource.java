package br.com.transporte.appGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.repository.RepositoryCallback;
import br.com.transporte.appGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.appGhn.tasks.manutencao.AdicionaManutencaoTask;
import br.com.transporte.appGhn.tasks.manutencao.AtualizaManutencaoTask;
import br.com.transporte.appGhn.tasks.manutencao.DeletaManutencaoTask;

public class LocalManutencaoDataSource {
    private final RoomCustosDeManutencaoDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalManutencaoDataSource(Context context) {
        this.dao = GhnDataBase.getInstance(context).getRoomCustosDeManutencaoDao();
        final GhnApplication application = new GhnApplication();
        this.executor = application.getExecutorService();
        this.handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<List<CustosDeManutencao>> buscaManutencaoPorCavaloId_room(final long cavaloId) {
        return dao.listaPeloCavaloId(cavaloId);
    }

    public LiveData<CustosDeManutencao> localizaManutencao_room(final long id) {
        return dao.localizaPeloId(id);
    }

    public void atualizaManutencao_room(
            final CustosDeManutencao manutencao,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaManutencaoTask task = new AtualizaManutencaoTask(executor, handler);
        task.solicitaAtualizacao(dao, manutencao,
                callback::quandoFinaliza
        );
    }

    public void adicionaManutencao_room(
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

    public void deletaManutencao_room(
            final CustosDeManutencao manutencao,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final DeletaManutencaoTask task = new DeletaManutencaoTask(executor, handler);
        task.solicitaRemocao(dao, manutencao,
                callback::quandoFinaliza
        );
    }

}

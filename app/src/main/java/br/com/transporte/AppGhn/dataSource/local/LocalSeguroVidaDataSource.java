package br.com.transporte.AppGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.repository.RepositoryCallback;
import br.com.transporte.AppGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.AppGhn.tasks.seguroVida.AdicionaSeguroVidaTask;
import br.com.transporte.AppGhn.tasks.seguroVida.DeletaSeguroVidaTask;
import br.com.transporte.AppGhn.tasks.seguroVida.EditaSeguroVidaTask;

public class LocalSeguroVidaDataSource {
    private final RoomDespesaSeguroVidaDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalSeguroVidaDataSource(Context context) {
        this.dao = GhnDataBase.getInstance(context).getRoomDespesaSeguroVidaDao();
        final GhnApplication application = new GhnApplication();
        this.executor = application.getExecutorService();
        this.handler = application.getMainThreadHandler();
    }

//----------------------------------------------------------------------------------------------

    public void adiciona(
            final DespesaComSeguroDeVida seguro,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaSeguroVidaTask task = new AdicionaSeguroVidaTask(executor, handler);
        task.solicitaAdicao(seguro, dao,
                callback::sucesso);
    }

    public void edita(
            final DespesaComSeguroDeVida seguro,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final EditaSeguroVidaTask task = new EditaSeguroVidaTask(executor, handler);
        task.solicitaAtualizacao(dao, seguro,
                callback::quandoFinaliza);
    }

    public void deleta(
            final DespesaComSeguroDeVida seguro,
            final RepositoryCallback<String> callback
    ) {
        final DeletaSeguroVidaTask task = new DeletaSeguroVidaTask(executor, handler);
        if (seguro != null) {
            task.solicitaRemocao(dao, seguro,
                    () -> {
                        callback.sucesso(null);
                    });
        } else {
            callback.falha("Falha ao remover");
        }
    }

    public LiveData<List<DespesaComSeguroDeVida>> buscaTodos() {
        return dao.todos();
    }

    public LiveData<List<DespesaComSeguroDeVida>> buscaPorStatus(boolean isValido) {
        return dao.buscaPorStatus(isValido);
    }

    public LiveData<DespesaComSeguroDeVida> localizaPeloId(final Long id) {
        return dao.localizaPeloId(id);
    }
}

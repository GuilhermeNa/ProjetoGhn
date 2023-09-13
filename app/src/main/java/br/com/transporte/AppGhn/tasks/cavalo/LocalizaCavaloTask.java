package br.com.transporte.AppGhn.tasks.cavalo;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.model.Cavalo;

public class LocalizaCavaloTask {
    private final Executor executor;
    private final Handler resultHandler;

    public LocalizaCavaloTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void buscaFinalizada(Cavalo cavalo);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomCavaloDao dao,
            final Long cavaloId,
            final TaskCallback callback
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Cavalo cavalo = realizaSubstituicaoSincrona(dao, cavaloId);
                notificaResultado(callback, cavalo);
            }
        });
    }

    private Cavalo realizaSubstituicaoSincrona(
            @NonNull RoomCavaloDao dao,
            Long cavaloId
    ) {
        return dao.localizaPeloId(cavaloId);
    }

    private void notificaResultado(
          TaskCallback callback,
          Cavalo cavalo) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.buscaFinalizada(cavalo);
            }
        });
    }



}

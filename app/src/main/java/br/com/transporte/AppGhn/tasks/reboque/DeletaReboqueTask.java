package br.com.transporte.AppGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.SemiReboque;

public class DeletaReboqueTask {
    private final Executor executor;
    private final Handler resultHandler;

    public DeletaReboqueTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void remocaoFinalizada();
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomSemiReboqueDao dao,
            final SemiReboque reboque,
            final TaskCallback callback
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                realizaRemocaoSincrona(dao, reboque);
                notificaResultado(callback);
            }
        });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomSemiReboqueDao dao,
            final SemiReboque reboque
    ) {
        dao.deleta(reboque);
    }

    private void notificaResultado(final TaskCallback callback) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.remocaoFinalizada();

            }
        });
    }

}

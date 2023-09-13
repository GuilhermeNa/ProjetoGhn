package br.com.transporte.AppGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.dao.SemiReboqueDAO;
import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;

public class LocalizaReboqueTask {
    private final Executor executor;
    private final Handler resultHandler;

    public LocalizaReboqueTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void buscaFinalizada(SemiReboque reboque);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomSemiReboqueDao dao,
            final Long reboqueId,
            final TaskCallback callback
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                SemiReboque reboque = realizaBuscaSincrona(dao, reboqueId);
                notificaResultado(callback, reboque);
            }
        });
    }

    private SemiReboque realizaBuscaSincrona(
            @NonNull RoomSemiReboqueDao dao,
            Long reboqueId
    ) {
        return dao.localizaPeloId(reboqueId);
    }

    private void notificaResultado(
            TaskCallback callback,
            SemiReboque reboque) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.buscaFinalizada(reboque);
            }
        });
    }
}

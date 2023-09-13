package br.com.transporte.AppGhn.tasks.cavalo;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.model.Cavalo;

public class BuscaTodosCavalosTask {
    private final Executor executor;
    private final Handler resultHandler;

    public BuscaTodosCavalosTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void buscaFinalizada(List<Cavalo> todosCavalos);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomCavaloDao dao,
            final TaskCallback callBack
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Cavalo> result = realizaBuscaSincrona(dao);
                notificaResultado(callBack, result);
            }
        });
    }

    private List<Cavalo> realizaBuscaSincrona(
            @NonNull final RoomCavaloDao dao
    ) {
        return dao.todos();
    }

    private void notificaResultado(
            final TaskCallback callBack,
            final List<Cavalo> result
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.buscaFinalizada(result);
            }
        });
    }

}

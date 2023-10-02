package br.com.transporte.AppGhn.tasks.cavalo;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.model.Cavalo;

public class DeletaCavaloTask {
    private final ExecutorService executor;
    private final Handler handler;

    public DeletaCavaloTask(ExecutorService executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    public interface TaskCallback {
        void quandoFinaliza();
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomCavaloDao dao,
            final Cavalo cavalo,
            final TaskCallback callBack
    ) {
        executor.execute(
                () -> {
                    realizaRemocao(cavalo, dao);
                    notificaResultado(callBack);
                }
        );
    }

    private void realizaRemocao(
            final Cavalo cavalo,
            @NonNull final RoomCavaloDao dao
    ) {
        dao.deleta(cavalo);
    }

    private void notificaResultado(@NonNull TaskCallback callBack) {
        handler.post(
                callBack::quandoFinaliza);
    }

}

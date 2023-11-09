package br.com.transporte.appGhn.tasks.cavalo;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.appGhn.database.dao.RoomCavaloDao;
import br.com.transporte.appGhn.model.Cavalo;

public class AdicionaCavaloTask {
    private final Executor executor;
    private final Handler resultHandler;

    public AdicionaCavaloTask(Executor executor, Handler handler) {
        this.executor = executor;
        this.resultHandler = handler;
    }

    public interface TaskCallback {
        void adicaoFinalizada(Long id);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomCavaloDao dao,
            final Cavalo cavalo,
            final TaskCallback callBack
    ) {
        executor.execute(() -> {
            long result = realizaAdicaoSincrona(dao, cavalo);
            notificaResultado(callBack, result);
        });
    }

    private long realizaAdicaoSincrona(
            @NonNull final RoomCavaloDao dao,
            Cavalo cavalo
    ) {
        return dao.adiciona(cavalo);
    }

    private void notificaResultado(
            final TaskCallback callBack,
            final long result
    ) {
        resultHandler.post(() -> callBack.adicaoFinalizada(result));
    }

}

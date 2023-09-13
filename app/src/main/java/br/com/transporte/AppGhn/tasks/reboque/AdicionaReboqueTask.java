package br.com.transporte.AppGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.tasks.cavalo.AdicionaCavaloTask;

public class AdicionaReboqueTask {
    private final Executor executor;
    private final Handler resultHandler;

    public AdicionaReboqueTask(Executor executor, Handler handler) {
        this.executor = executor;
        this.resultHandler = handler;
    }

    public interface TaskCallback {
        void adicaoFinalizada(Long id);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomSemiReboqueDao dao,
            final SemiReboque reboque,
            final TaskCallback callBack
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long result = realizaAdicaoSincrona(dao, reboque);
                notificaResultado(callBack, result);
            }
        });
    }

    private long realizaAdicaoSincrona(
            @NonNull final RoomSemiReboqueDao dao,
            SemiReboque reboque
    ) {
        return dao.adiciona(reboque);
    }

    private void notificaResultado(
            final TaskCallback callBack,
            final long result
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.adicaoFinalizada(result);
            }
        });
    }

}

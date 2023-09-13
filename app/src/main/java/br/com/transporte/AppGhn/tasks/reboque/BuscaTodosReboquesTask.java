package br.com.transporte.AppGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.SemiReboque;

public class BuscaTodosReboquesTask {
    private final ExecutorService executor;
    private final Handler resultHandler;

    public BuscaTodosReboquesTask(ExecutorService executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void buscaFinalizada(List<SemiReboque> todosReboques);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomSemiReboqueDao dao,
            final TaskCallback callBack
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<SemiReboque> result = realizaBuscaSincrona(dao);
                notificaResultado(callBack, result);
            }
        });
    }

    private List<SemiReboque> realizaBuscaSincrona(
            @NonNull final RoomSemiReboqueDao dao
    ){
        return dao.todos();
    }

    private void notificaResultado(
            final TaskCallback callBack,
            final List<SemiReboque> result
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.buscaFinalizada(result);
            }
        });
    }
}

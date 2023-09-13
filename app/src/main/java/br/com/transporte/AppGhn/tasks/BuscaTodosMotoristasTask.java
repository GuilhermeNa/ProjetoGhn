package br.com.transporte.AppGhn.tasks;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.model.Motorista;

public class BuscaTodosMotoristasTask {
    private final Executor executor;
    private final Handler resultHandler;

    public BuscaTodosMotoristasTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void buscaFinalizada(List<Motorista> result);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomMotoristaDao dao,
            final TaskCallback callBack
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Motorista> result = realizaBuscaSincrona(dao);
                notificaResultado(result, callBack);
            }
        });
    }

    private List<Motorista> realizaBuscaSincrona(
            @NonNull final RoomMotoristaDao dao
    ) {
        return dao.todos();
    }

    private void notificaResultado(
            List<Motorista> result,
            @NonNull TaskCallback callBack
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.buscaFinalizada(result);
            }
        });
    }

}

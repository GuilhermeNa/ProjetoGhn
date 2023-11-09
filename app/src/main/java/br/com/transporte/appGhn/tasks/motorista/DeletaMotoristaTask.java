package br.com.transporte.appGhn.tasks.motorista;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.appGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.appGhn.model.Motorista;

public class DeletaMotoristaTask {
    private final Executor executor;
    private final Handler resultHandler;

    public DeletaMotoristaTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void remocaoFinalizada();
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomMotoristaDao dao,
            final Motorista motorista,
            final TaskCallback callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, motorista);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomMotoristaDao dao,
            final Motorista motorista
    ) {
        dao.deleta(motorista);
    }

    private void notificaResultado(@NonNull final TaskCallback callback) {
        resultHandler.post(
                callback::remocaoFinalizada
        );
    }
}

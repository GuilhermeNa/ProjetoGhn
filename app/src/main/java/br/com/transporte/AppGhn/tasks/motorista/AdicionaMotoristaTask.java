package br.com.transporte.AppGhn.tasks.motorista;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.AppGhn.model.Motorista;

public class AdicionaMotoristaTask {
    private final Executor executor;
    private final Handler resultHandler;

    public AdicionaMotoristaTask(Executor executor, Handler handler) {
        this.executor = executor;
        this.resultHandler = handler;
    }

    public interface TaskCallback {
        void adicaoFinalizada(Long id);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomMotoristaDao dao,
            final Motorista motorista,
            final TaskCallback callBack
    ) {
        executor.execute(
                () -> {
                    long motoristaId = realizaAdicaoSincrona(dao, motorista);
                    notificaResultado(callBack, motoristaId);
                });
    }

    private long realizaAdicaoSincrona(
            @NonNull final RoomMotoristaDao dao,
            Motorista motorista
    ) {
        return dao.adiciona(motorista);
    }

    private void notificaResultado(
            final TaskCallback callBack,
            final long motoristaId
    ) {
        resultHandler.post(
                () -> callBack.adicaoFinalizada(motoristaId)
        );
    }

}

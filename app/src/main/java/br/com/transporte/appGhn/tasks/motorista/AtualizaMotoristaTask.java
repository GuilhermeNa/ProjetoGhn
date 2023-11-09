package br.com.transporte.appGhn.tasks.motorista;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.appGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.appGhn.model.Motorista;

public class AtualizaMotoristaTask {
    private final Executor executor;
    private final Handler resultHandler;

    public AtualizaMotoristaTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void finalizaAtualizacao();
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            RoomMotoristaDao dao,
            Motorista motorista,
            TaskCallback callBack
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, motorista);
                    notificaResultado(callBack);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull RoomMotoristaDao dao,
            Motorista motorista
    ) {
        dao.substitui(motorista);
    }

    private void notificaResultado(@NonNull TaskCallback callBack) {
        resultHandler.post(
                callBack::finalizaAtualizacao
        );
    }

}

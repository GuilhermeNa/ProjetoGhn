package br.com.transporte.AppGhn.tasks.cavalo;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.model.Cavalo;

public class AtualizaCavaloTask {
    private final Executor executor;
    private final Handler resultHandler;

    public AtualizaCavaloTask(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public interface TaskCallback {
        void atualizacaoFinalizada();
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomCavaloDao dao,
            final Cavalo cavalo,
            final TaskCallback callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, cavalo);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomCavaloDao dao,
            final Cavalo cavalo
    ) {
        dao.substitui(cavalo);
    }

    private void notificaResultado(@NonNull final TaskCallback callback) {
        resultHandler.post(callback::atualizacaoFinalizada);
    }

}

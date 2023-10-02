package br.com.transporte.AppGhn.tasks.frete;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class AtualizaFreteTask {
    private final ExecutorService executor;
    private final Handler handler;

    public AtualizaFreteTask(ExecutorService executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomFreteDao dao,
            final Frete frete,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, frete);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomFreteDao dao,
            final Frete frete
    ) {
        dao.substitui(frete);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

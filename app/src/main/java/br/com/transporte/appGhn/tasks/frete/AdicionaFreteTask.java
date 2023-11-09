package br.com.transporte.appGhn.tasks.frete;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomFreteDao;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaFreteTask {
    private final ExecutorService executor;
    private final Handler handler;

    public AdicionaFreteTask(ExecutorService executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final Frete frete,
            final RoomFreteDao dao,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    Long id = realizaAdicaoSincrona(frete, dao);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            final Frete frete,
            @NonNull final RoomFreteDao dao
    ) {
        return dao.adiciona(frete);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(() -> callback.finalizado(id));
    }

}

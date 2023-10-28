package br.com.transporte.AppGhn.tasks.cavalo;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

public class LocalizaPeloIdTask extends BaseTask {

    public LocalizaPeloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomCavaloDao dao,
            final Long cavaloId,
            final TaskCallback<Cavalo> callback
    ) {
        executor.execute(
                () -> {
                    final Cavalo cavalo = realizaBuscaSincrona(dao, cavaloId);
                    devolveResultado(cavalo, callback);
                });
    }

    private Cavalo realizaBuscaSincrona(
            @NonNull final RoomCavaloDao dao,
            final Long cavaloId
    ) {
        return dao.localizaPeloIdParaTask(cavaloId);
    }

    private void devolveResultado(
            final Cavalo cavalo,
            final TaskCallback<Cavalo> callback
    ) {
        handler.post(() -> callback.finalizado(cavalo));
    }

}

package br.com.transporte.appGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class DeletaReboqueTask extends BaseTask {
    public DeletaReboqueTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomSemiReboqueDao dao,
            final SemiReboque reboque,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, reboque);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomSemiReboqueDao dao,
            final SemiReboque reboque
    ) {
        dao.deleta(reboque);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

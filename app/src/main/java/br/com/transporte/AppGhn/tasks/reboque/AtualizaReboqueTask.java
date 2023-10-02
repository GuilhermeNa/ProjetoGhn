package br.com.transporte.AppGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class AtualizaReboqueTask extends BaseTask {

    public AtualizaReboqueTask(ExecutorService executor, Handler handler) {
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
                    realizaSubstituicaoSincrona(dao, reboque);
                    notificaResultado(callback);
                });
    }

    private void realizaSubstituicaoSincrona(
            @NonNull final RoomSemiReboqueDao dao,
            final SemiReboque reboque
    ) {
        dao.substitui(reboque);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }
}

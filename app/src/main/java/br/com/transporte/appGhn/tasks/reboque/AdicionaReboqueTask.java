package br.com.transporte.appGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaReboqueTask extends BaseTask {
    public AdicionaReboqueTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomSemiReboqueDao dao,
            final SemiReboque reboque,
            final TaskCallback<Long> callBack
    ) {
        executor.execute(
                () -> {
                    long result = realizaAdicaoSincrona(dao, reboque);
                    notificaResultado(callBack, result);
                });
    }

    private long realizaAdicaoSincrona(
            @NonNull final RoomSemiReboqueDao dao,
            SemiReboque reboque
    ) {
        return dao.adiciona(reboque);
    }

    private void notificaResultado(
            final TaskCallback<Long> callBack,
            final long result
    ) {
        handler.post(
                () -> callBack.finalizado(result)
        );
    }

}

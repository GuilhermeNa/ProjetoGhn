package br.com.transporte.AppGhn.tasks.reboque;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCavaloDao;
import br.com.transporte.AppGhn.database.dao.RoomSemiReboqueDao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.SemiReboque;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;
import br.com.transporte.AppGhn.tasks.cavalo.AdicionaCavaloTask;

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

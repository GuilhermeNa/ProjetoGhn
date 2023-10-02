package br.com.transporte.AppGhn.tasks.adiantamento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class DeletaAdiantamentoTask extends BaseTask {
    public DeletaAdiantamentoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomAdiantamentoDao dao,
            final Adiantamento adiantamento,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, adiantamento);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomAdiantamentoDao dao,
            final Adiantamento adiantamento
    ) {
        dao.deleta(adiantamento);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );

    }



}

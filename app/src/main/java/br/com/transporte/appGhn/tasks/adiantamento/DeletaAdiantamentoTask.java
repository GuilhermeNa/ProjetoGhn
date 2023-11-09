package br.com.transporte.appGhn.tasks.adiantamento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

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

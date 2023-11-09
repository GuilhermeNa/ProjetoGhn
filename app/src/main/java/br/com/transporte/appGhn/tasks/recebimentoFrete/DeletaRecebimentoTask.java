package br.com.transporte.appGhn.tasks.recebimentoFrete;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class DeletaRecebimentoTask extends BaseTask {
    public DeletaRecebimentoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomRecebimentoFreteDao dao,
            final RecebimentoDeFrete recebimento,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, recebimento);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomRecebimentoFreteDao dao,
            final RecebimentoDeFrete recebimento
    ) {
        dao.deleta(recebimento);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

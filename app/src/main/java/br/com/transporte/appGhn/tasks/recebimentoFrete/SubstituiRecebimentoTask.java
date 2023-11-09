package br.com.transporte.appGhn.tasks.recebimentoFrete;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class SubstituiRecebimentoTask extends BaseTask {
    public SubstituiRecebimentoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaSubstituicaoTask(
            final RecebimentoDeFrete recebimento,
            final RoomRecebimentoFreteDao dao,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaSubstituicaoAssincrona(recebimento, dao);
                    notificaResultado(callback);
                });
    }

    private void realizaSubstituicaoAssincrona(
            final RecebimentoDeFrete recebimento,
            @NonNull final RoomRecebimentoFreteDao dao
    ) {
       dao.substitui(recebimento);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

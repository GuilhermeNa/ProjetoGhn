package br.com.transporte.appGhn.tasks.recebimentoFrete;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomRecebimentoFreteDao;
import br.com.transporte.appGhn.model.RecebimentoDeFrete;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaRecebimentoTask extends BaseTask {
    public AdicionaRecebimentoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicaoTask(
            final RecebimentoDeFrete recebimento,
            final RoomRecebimentoFreteDao dao,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoAssincrona(recebimento, dao);
                    notificaResultado(id, callback);
                });
    }

    @Nullable
    @Contract(pure = true)
    private Long realizaAdicaoAssincrona(
            final RecebimentoDeFrete recebimento,
            @NonNull final RoomRecebimentoFreteDao dao
    ) {
        return dao.adiciona(recebimento);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id));
    }

}

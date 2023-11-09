package br.com.transporte.appGhn.tasks.seguroFrota;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaComSeguroFrotaDao;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaSeguroFrotaTask extends BaseTask {
    public AdicionaSeguroFrotaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final DespesaComSeguroFrota seguro,
            final RoomDespesaComSeguroFrotaDao dao,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoAssincrona(seguro, dao);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoAssincrona(
            final DespesaComSeguroFrota seguro,
            @NonNull final RoomDespesaComSeguroFrotaDao dao
    ) {
        return dao.adiciona(seguro);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(() -> callback.finalizado(id));
    }

}

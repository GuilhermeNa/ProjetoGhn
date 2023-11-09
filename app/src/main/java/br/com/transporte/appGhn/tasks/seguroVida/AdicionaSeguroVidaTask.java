package br.com.transporte.appGhn.tasks.seguroVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaSeguroVidaTask extends BaseTask {
    public AdicionaSeguroVidaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    public void solicitaAdicao(
            final DespesaComSeguroDeVida seguro,
            final RoomDespesaSeguroVidaDao dao,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoAssincrona(seguro, dao);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoAssincrona(
            final DespesaComSeguroDeVida seguro,
            @NonNull final RoomDespesaSeguroVidaDao dao
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

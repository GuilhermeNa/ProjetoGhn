package br.com.transporte.AppGhn.tasks.seguroVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

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

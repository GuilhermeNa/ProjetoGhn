package br.com.transporte.appGhn.tasks.adiantamento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaAdiantamentoTask extends BaseTask {
    public AdicionaAdiantamentoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomAdiantamentoDao dao,
            final Adiantamento adiantamento,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, adiantamento);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            @NonNull final RoomAdiantamentoDao dao,
            final Adiantamento adiantamento
    ) {
        return dao.adiciona(adiantamento);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id)
        );
    }

}

package br.com.transporte.AppGhn.tasks.despesaAdm;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

public class AdicionaDespesaAdmTask extends BaseTask {
    public AdicionaDespesaAdmTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //-----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomDespesaAdmDao dao,
            final DespesaAdm despesa,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, despesa);
                    notificaResultado(id, callback);
                });
    }

    @NonNull
    private Long realizaAdicaoSincrona(
            @NonNull final RoomDespesaAdmDao dao,
            final DespesaAdm despesa
    ) {
        return dao.adiciona(despesa);
    }

    private void notificaResultado(
            final Long id,
            @NonNull final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id)
        );
    }

}

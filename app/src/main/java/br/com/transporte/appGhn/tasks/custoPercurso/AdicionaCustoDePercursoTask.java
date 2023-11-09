package br.com.transporte.appGhn.tasks.custoPercurso;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaCustoDePercursoTask extends BaseTask {

    public AdicionaCustoDePercursoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomCustosPercursoDao dao,
            final CustosDePercurso custo,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, custo);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            @NonNull final RoomCustosPercursoDao dao,
            final CustosDePercurso custo
    ) {
        return dao.adiciona(custo);
    }

    private void notificaResultado(
            final long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id)
        );
    }

}

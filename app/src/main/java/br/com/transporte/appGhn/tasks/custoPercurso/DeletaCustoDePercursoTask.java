package br.com.transporte.appGhn.tasks.custoPercurso;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class DeletaCustoDePercursoTask extends BaseTask {
    public DeletaCustoDePercursoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomCustosPercursoDao dao,
            final CustosDePercurso custo,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, custo);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomCustosPercursoDao dao,
            final CustosDePercurso custo
    ) {
        dao.deleta(custo);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

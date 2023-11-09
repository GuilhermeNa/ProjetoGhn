package br.com.transporte.appGhn.tasks.despesaAdm;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class AlteraDespesaAdmTask extends BaseTask {

    public AlteraDespesaAdmTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAlteracao(
            final RoomDespesaAdmDao dao,
            final DespesaAdm despesa,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAlteracaoSincrona(dao, despesa);
                    notificaResultado(callback);
                });
    }

    private void realizaAlteracaoSincrona(
            @NonNull final RoomDespesaAdmDao dao,
            final DespesaAdm despesa
    ) {
        dao.atualiza(despesa);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

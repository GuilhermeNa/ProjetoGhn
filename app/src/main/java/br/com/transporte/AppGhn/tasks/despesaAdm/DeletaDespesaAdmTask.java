package br.com.transporte.AppGhn.tasks.despesaAdm;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class DeletaDespesaAdmTask extends BaseTask {

    public DeletaDespesaAdmTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomDespesaAdmDao dao,
            final DespesaAdm despesa,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, despesa);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomDespesaAdmDao dao,
            final DespesaAdm despesa
    ) {
        dao.deleta(despesa);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }




}

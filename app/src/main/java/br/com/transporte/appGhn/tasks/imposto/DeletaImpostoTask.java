package br.com.transporte.appGhn.tasks.imposto;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class DeletaImpostoTask extends BaseTask {
    public DeletaImpostoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomDespesaImpostoDao dao,
            final DespesasDeImposto imposto,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, imposto);
                    notificaResultado(callback);
                });

    }

    private static void realizaRemocaoSincrona(
            @NonNull final RoomDespesaImpostoDao dao,
            final DespesasDeImposto imposto
    ) {
        dao.deleta(imposto);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

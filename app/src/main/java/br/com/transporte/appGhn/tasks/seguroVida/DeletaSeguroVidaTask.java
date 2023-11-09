package br.com.transporte.appGhn.tasks.seguroVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class DeletaSeguroVidaTask extends BaseTask {
    public DeletaSeguroVidaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    public void solicitaRemocao(
            final RoomDespesaSeguroVidaDao dao,
            final DespesaComSeguroDeVida seguro,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, seguro);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomDespesaSeguroVidaDao dao,
            final DespesaComSeguroDeVida seguro
    ) {
        dao.deleta(seguro);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

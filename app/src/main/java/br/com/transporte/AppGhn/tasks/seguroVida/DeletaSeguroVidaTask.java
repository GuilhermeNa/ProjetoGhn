package br.com.transporte.AppGhn.tasks.seguroVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

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

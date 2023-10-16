package br.com.transporte.AppGhn.tasks.seguroVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaSeguroVidaDao;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroDeVida;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class EditaSeguroVidaTask extends BaseTask {
    public EditaSeguroVidaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }


    public void solicitaAtualizacao(
            final RoomDespesaSeguroVidaDao dao,
            final DespesaComSeguroDeVida seguro,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, seguro);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomDespesaSeguroVidaDao dao,
            final DespesaComSeguroDeVida seguro
    ) {
        dao.substitui(seguro);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

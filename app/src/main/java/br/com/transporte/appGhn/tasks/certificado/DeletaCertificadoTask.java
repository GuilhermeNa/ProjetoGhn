package br.com.transporte.appGhn.tasks.certificado;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class DeletaCertificadoTask extends BaseTask {
    public DeletaCertificadoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomDespesaCertificadoDao dao,
            final DespesaCertificado certificado,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, certificado);
                    notificaResultado(callback);
                });
    }

    private static void realizaRemocaoSincrona(
            @NonNull final RoomDespesaCertificadoDao dao,
            final DespesaCertificado certificado
    ) {
        dao.deleta(certificado);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

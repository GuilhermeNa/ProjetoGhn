package br.com.transporte.appGhn.tasks.certificado;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaCertificadoTask extends BaseTask {
    public AdicionaCertificadoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomDespesaCertificadoDao dao,
            final DespesaCertificado certificado,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, certificado);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            @NonNull final RoomDespesaCertificadoDao dao,
            final DespesaCertificado certificado
    ) {
        return dao.adiciona(certificado);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id)
        );
    }

}

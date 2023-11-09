package br.com.transporte.appGhn.tasks.certificado;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.appGhn.model.despesas.DespesaCertificado;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class AtualizaCertificadoTask extends BaseTask {
    public AtualizaCertificadoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomDespesaCertificadoDao dao,
            final DespesaCertificado certificado,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    solicitaAtualizacaoSincrona(dao, certificado);
                    notificaResultado(callback);
                });
    }

    private void solicitaAtualizacaoSincrona(
            @NonNull RoomDespesaCertificadoDao dao,
            final DespesaCertificado certificado
    ) {
        dao.atualiza(certificado);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

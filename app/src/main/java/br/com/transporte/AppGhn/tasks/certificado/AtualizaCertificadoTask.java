package br.com.transporte.AppGhn.tasks.certificado;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class AtualizaCertificadoTask extends BaseTask {
    public AtualizaCertificadoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomDespesaImpostoDao dao,
            final DespesasDeImposto imposto,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    solicitaAtualizacaoSincrona(dao, imposto);
                    notificaResultado(callback);
                });
    }

    private void solicitaAtualizacaoSincrona(
            @NonNull final RoomDespesaImpostoDao dao,
            final DespesasDeImposto imposto
    ) {
        dao.edita(imposto);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

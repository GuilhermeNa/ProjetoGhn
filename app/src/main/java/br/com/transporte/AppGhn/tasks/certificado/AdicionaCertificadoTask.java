package br.com.transporte.AppGhn.tasks.certificado;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

public class AdicionaCertificadoTask extends BaseTask {
    public AdicionaCertificadoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomDespesaImpostoDao dao,
            final DespesasDeImposto imposto,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, imposto);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            @NonNull final RoomDespesaImpostoDao dao,
            final DespesasDeImposto imposto
    ) {
        return dao.adiciona(imposto);
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

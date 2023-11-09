package br.com.transporte.appGhn.tasks.adiantamento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.appGhn.model.Adiantamento;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class AlteraAdiantamentoTask extends BaseTask {
    public AlteraAdiantamentoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAlteracao(
            final RoomAdiantamentoDao dao,
            final Adiantamento adiantamento,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAlteracaoSincrona(dao, adiantamento);
                    notificaResultado(callback);
                });
    }

    private void realizaAlteracaoSincrona(
            @NonNull final RoomAdiantamentoDao dao,
            final Adiantamento adiantamento
    ) {
        dao.altera(adiantamento);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

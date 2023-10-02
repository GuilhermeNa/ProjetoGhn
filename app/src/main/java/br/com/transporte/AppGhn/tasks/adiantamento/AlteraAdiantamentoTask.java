package br.com.transporte.AppGhn.tasks.adiantamento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomAdiantamentoDao;
import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

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

package br.com.transporte.AppGhn.tasks.seguroFrota;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaComSeguroFrotaDao;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class EditaSeguroFrotaTask extends BaseTask {

    public EditaSeguroFrotaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    public void solicitaAtualizacao(
            final RoomDespesaComSeguroFrotaDao dao,
            final DespesaComSeguroFrota seguro,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, seguro);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomDespesaComSeguroFrotaDao dao,
            final DespesaComSeguroFrota seguro
    ) {
        dao.substitui(seguro);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

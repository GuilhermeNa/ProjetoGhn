package br.com.transporte.AppGhn.tasks.manutencao;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class DeletaManutencaoTask extends BaseTask {
    public DeletaManutencaoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomCustosDeManutencaoDao dao,
            final CustosDeManutencao manutencao,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocaoSincrona(dao, manutencao);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocaoSincrona(
            @NonNull final RoomCustosDeManutencaoDao dao,
            final CustosDeManutencao manutencao
    ) {
        dao.deleta(manutencao);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

package br.com.transporte.AppGhn.tasks.manutencao;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class AtualizaManutencaoTask extends BaseTask {
    public AtualizaManutencaoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomCustosDeManutencaoDao dao,
            final CustosDeManutencao manutencao,
            final TaskCallbackVoid callback
            ){
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, manutencao);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomCustosDeManutencaoDao dao,
            final CustosDeManutencao manutencao
    ) {
        dao.atualiza(manutencao);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );

    }


}

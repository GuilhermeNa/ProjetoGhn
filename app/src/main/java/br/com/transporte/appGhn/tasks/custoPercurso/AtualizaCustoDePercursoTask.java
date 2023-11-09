package br.com.transporte.appGhn.tasks.custoPercurso;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class AtualizaCustoDePercursoTask extends BaseTask {

    public AtualizaCustoDePercursoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomCustosPercursoDao dao,
            final CustosDePercurso custo,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, custo);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomCustosPercursoDao dao,
            final CustosDePercurso custo
    ) {
        dao.atualiza(custo);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

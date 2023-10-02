package br.com.transporte.AppGhn.tasks.custoSalario;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class AlteraCustoSalarioTask extends BaseTask {
    public AlteraCustoSalarioTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAlteracao(
            final RoomCustosDeSalarioDao dao,
            final CustosDeSalario salario,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAlteracaoSincrona(dao, salario);
                    notificaResultado(callback);
                });
    }

    private void realizaAlteracaoSincrona(
            @NonNull final RoomCustosDeSalarioDao dao,
            final CustosDeSalario salario
    ) {
        dao.altera(salario);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(
                callback::finalizado
        );
    }

}

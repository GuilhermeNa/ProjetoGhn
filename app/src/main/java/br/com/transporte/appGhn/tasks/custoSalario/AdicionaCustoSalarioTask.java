package br.com.transporte.appGhn.tasks.custoSalario;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.appGhn.model.custos.CustosDeSalario;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaCustoSalarioTask extends BaseTask {
    public AdicionaCustoSalarioTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //-----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomCustosDeSalarioDao dao,
            final CustosDeSalario salario,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, salario);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            @NonNull final RoomCustosDeSalarioDao dao,
            final CustosDeSalario salario
    ) {
        return dao.adiciona(salario);
    }

    private void notificaResultado(
            final Long id,
            @NonNull final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id)
        );
    }

}

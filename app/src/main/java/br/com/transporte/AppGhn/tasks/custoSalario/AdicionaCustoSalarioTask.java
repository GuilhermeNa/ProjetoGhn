package br.com.transporte.AppGhn.tasks.custoSalario;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

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

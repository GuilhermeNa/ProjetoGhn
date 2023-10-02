package br.com.transporte.AppGhn.tasks.custoPercurso;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;
import br.com.transporte.AppGhn.tasks.cavalo.DeletaCavaloTask;

public class AdicionaCustoDePercursoTask extends BaseTask {

    public AdicionaCustoDePercursoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomCustosPercursoDao dao,
            final CustosDePercurso custo,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, custo);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            @NonNull final RoomCustosPercursoDao dao,
            final CustosDePercurso custo
    ) {
        return dao.adiciona(custo);
    }

    private void notificaResultado(
            final long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id)
        );
    }

}

package br.com.transporte.appGhn.tasks.manutencao;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class AdicionaManutencaoTask extends BaseTask {
    public AdicionaManutencaoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomCustosDeManutencaoDao dao,
            final CustosDeManutencao manutencao,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    Long id = realizaAdicaoSincrona(dao, manutencao);
                    notificaResultado(id, callback);
                });
    }

    @Nullable
    @Contract(pure = true)
    private Long realizaAdicaoSincrona(
            @NonNull final RoomCustosDeManutencaoDao dao,
            final CustosDeManutencao manutencao
    ) {
        return dao.adiciona(manutencao);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id));
    }

}

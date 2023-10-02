package br.com.transporte.AppGhn.tasks.custoAbastecimento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.tasks.TaskCallback;

public class AdicionaCustoDeAbastecimentoTask {
    private final ExecutorService executor;
    private final Handler handler;

    public AdicionaCustoDeAbastecimentoTask(ExecutorService executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final RoomCustosAbastecimentoDao dao,
            final CustosDeAbastecimento abastecimento,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoSincrona(dao, abastecimento);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoSincrona(
            @NonNull final RoomCustosAbastecimentoDao dao,
            final CustosDeAbastecimento abastecimento
    ) {
        return dao.adiciona(abastecimento);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(
                () -> callback.finalizado(id)
        );
    }

}

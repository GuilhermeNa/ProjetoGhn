package br.com.transporte.appGhn.tasks.custoAbastecimento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class AtualizaCustoDeAbastecimentoTask {
    private final ExecutorService executor;
    private final Handler handler;

    public AtualizaCustoDeAbastecimentoTask(ExecutorService executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomCustosAbastecimentoDao dao,
            final CustosDeAbastecimento abastecimento,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, abastecimento);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomCustosAbastecimentoDao dao,
            final CustosDeAbastecimento abastecimento
    ) {
        dao.substitui(abastecimento);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

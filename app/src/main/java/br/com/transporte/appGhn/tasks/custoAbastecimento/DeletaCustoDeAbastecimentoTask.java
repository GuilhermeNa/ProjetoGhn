package br.com.transporte.appGhn.tasks.custoAbastecimento;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class DeletaCustoDeAbastecimentoTask extends BaseTask {
    public DeletaCustoDeAbastecimentoTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaRemocao(
            final RoomCustosAbastecimentoDao dao,
            final CustosDeAbastecimento abastecimento,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaRemocao(dao, abastecimento);
                    notificaResultado(callback);
                });
    }

    private void realizaRemocao(
            @NonNull final RoomCustosAbastecimentoDao dao,
            final CustosDeAbastecimento abastecimento
    ) {
        dao.deleta(abastecimento);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
            handler.post(callback::finalizado);
    }

}

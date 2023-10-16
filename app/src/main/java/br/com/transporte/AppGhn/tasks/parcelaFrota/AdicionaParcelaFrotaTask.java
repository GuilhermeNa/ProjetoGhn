package br.com.transporte.AppGhn.tasks.parcelaFrota;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

public class AdicionaParcelaFrotaTask extends BaseTask {

    public AdicionaParcelaFrotaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final Parcela_seguroFrota parcela,
            final RoomParcela_seguroFrotaDao dao,
            final TaskCallback<Long> callback
    ) {
        executor.execute(
                () -> {
                    final Long id = realizaAdicaoAssincrona(parcela, dao);
                    notificaResultado(id, callback);
                });
    }

    private Long realizaAdicaoAssincrona(
            final Parcela_seguroFrota seguro,
            @NonNull final RoomParcela_seguroFrotaDao dao
    ) {
        return dao.adiciona(seguro);
    }

    private void notificaResultado(
            final Long id,
            final TaskCallback<Long> callback
    ) {
        handler.post(() -> callback.finalizado(id));
    }


}

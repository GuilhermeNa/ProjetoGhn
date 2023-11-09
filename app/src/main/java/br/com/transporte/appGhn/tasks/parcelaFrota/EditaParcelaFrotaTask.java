package br.com.transporte.appGhn.tasks.parcelaFrota;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class EditaParcelaFrotaTask extends BaseTask {
    public EditaParcelaFrotaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomParcela_seguroFrotaDao dao,
            final Parcela_seguroFrota parcela,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, parcela);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final RoomParcela_seguroFrotaDao dao,
            final Parcela_seguroFrota parcela
    ) {
        dao.substitui(parcela);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

package br.com.transporte.appGhn.tasks.parcelaVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class EditaParcelaVidaTask extends BaseTask {
    public EditaParcelaVidaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAtualizacao(
            final RoomParcela_seguroVidaDao dao,
            final Parcela_seguroVida parcela,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAtualizacaoSincrona(dao, parcela);
                    notificaResultado(callback);
                });
    }

    private void realizaAtualizacaoSincrona(
            @NonNull final  RoomParcela_seguroVidaDao dao,
            final Parcela_seguroVida parcela
    ) {
        dao.substitui(parcela);
    }

    private void notificaResultado(@NonNull final TaskCallbackVoid callback) {
        handler.post(callback::finalizado);
    }

}

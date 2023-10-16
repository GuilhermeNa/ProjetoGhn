package br.com.transporte.AppGhn.tasks.parcelaVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

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

package br.com.transporte.AppGhn.tasks.parcelaVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallbackVoid;

public class AdicionaListaParcelasVidaTask extends BaseTask {
    public AdicionaListaParcelasVidaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final List<Parcela_seguroVida> listaParcelas,
            final RoomParcela_seguroVidaDao dao,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAdicaoAssincrona(listaParcelas, dao);
                    notificaResultado(callback);
                });
    }

    private void realizaAdicaoAssincrona(
            final List<Parcela_seguroVida> listaParcelas,
            @NonNull final RoomParcela_seguroVidaDao dao
    ) {
        dao.adicionaTodos(listaParcelas);
    }

    private void notificaResultado(
            @NonNull final TaskCallbackVoid callback
    ) {
        handler.post(callback::finalizado);
    }

}

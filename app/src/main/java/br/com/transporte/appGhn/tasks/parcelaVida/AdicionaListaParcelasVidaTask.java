package br.com.transporte.appGhn.tasks.parcelaVida;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

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

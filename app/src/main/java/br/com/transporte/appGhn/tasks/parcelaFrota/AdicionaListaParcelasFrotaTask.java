package br.com.transporte.appGhn.tasks.parcelaFrota;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallbackVoid;

public class AdicionaListaParcelasFrotaTask extends BaseTask {


    public AdicionaListaParcelasFrotaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

//--------------------------------------------------------------------------------------------------

    public void solicitaAdicao(
            final List<Parcela_seguroFrota> listaParcelas,
            final RoomParcela_seguroFrotaDao dao,
            final TaskCallbackVoid callback
    ) {
        executor.execute(
                () -> {
                    realizaAdicaoAssincrona(listaParcelas, dao);
                    notificaResultado(callback);
                });
    }

    private void realizaAdicaoAssincrona(
            final List<Parcela_seguroFrota> listaParcelas,
            @NonNull final RoomParcela_seguroFrotaDao dao
    ) {
        dao.adicionaTodos(listaParcelas);
    }

    private void notificaResultado(
            @NonNull final TaskCallbackVoid callback
    ) {
        handler.post(callback::finalizado);
    }

}

package br.com.transporte.appGhn.tasks.cavalo;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCavaloDao;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class LocalizaPelaPlacaTask extends BaseTask {

    public LocalizaPelaPlacaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //---------------------------------------------------------------------------------------------

    public void solicitaLocalizacao(
            final RoomCavaloDao dao,
            final String placa,
            final TaskCallback<Cavalo> callback
    ) {
        executor.execute(
                () -> {
                    final Cavalo cavalo = realizaBuscaAssincrona(dao, placa);
                    notificaResultado(cavalo, callback);
                });
    }

    @Nullable
    @Contract(pure = true)
    private Cavalo realizaBuscaAssincrona(
            @NonNull final RoomCavaloDao dao,
            final String placa
    ) {
        return dao.localizaPelaPlaca(placa);
    }

    private void notificaResultado(
            final Cavalo cavalo,
            final TaskCallback<Cavalo> callback
    ) {
        handler.post(() -> callback.finalizado(cavalo));
    }

}

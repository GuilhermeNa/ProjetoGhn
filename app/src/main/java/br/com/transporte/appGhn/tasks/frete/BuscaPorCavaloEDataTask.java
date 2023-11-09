package br.com.transporte.appGhn.tasks.frete;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomFreteDao;
import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.model.Frete;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class BuscaPorCavaloEDataTask extends BaseTask {
    public BuscaPorCavaloEDataTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomFreteDao dao,
            final Long id,
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            final TaskCallback<List<Frete>> callback
    ) {
        executor.execute(
                () -> {
                    List<Frete> lista = realizaBuscaSincrona(dao, id, dataInicial, dataFinal);
                    notificaResultado(lista, callback);
                });
    }

    private List<Frete> realizaBuscaSincrona(
            @NonNull final RoomFreteDao dao,
            final Long id,
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        List<Frete> lista = dao.buscaPorCavaloIdParaTask(id);
        return FiltraFrete.listaPorData(lista, dataInicial, dataFinal);
    }

    private void notificaResultado(
            final List<Frete> lista,
            @NonNull final TaskCallback<List<Frete>> callback
    ) {
        handler.post(
                () -> callback.finalizado(lista));
    }

}

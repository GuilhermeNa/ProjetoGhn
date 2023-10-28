package br.com.transporte.AppGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.filtros.FiltraFrete;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaFretePorAnoECavaloIdTask extends BaseTask {

    public BuscaFretePorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomFreteDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<Frete> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetFrete(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<Frete> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomFreteDao dao,
            final int ano
    ) {
        List<Frete> fretes = dao.buscaTodosParaTask();
        return FiltraFrete.listaPorAno(fretes, ano);
    }

    private List<Frete> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomFreteDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<Frete> fretes = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraFrete.listaPorAno(fretes, ano);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback

    ) {
        handler.post(() -> {
            callback.finalizado(resourceData);
        });
    }


}

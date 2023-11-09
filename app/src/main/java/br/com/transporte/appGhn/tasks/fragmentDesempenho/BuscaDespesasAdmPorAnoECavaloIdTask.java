package br.com.transporte.appGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.appGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaDespesasAdmPorAnoECavaloIdTask extends BaseTask {
    public BuscaDespesasAdmPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomDespesaAdmDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<DespesaAdm> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetDespesaAdm(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<DespesaAdm> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomDespesaAdmDao dao,
            final int ano
    ) {
        List<DespesaAdm> despesasAdm = dao.buscaTodosParaTask();
        return FiltraDespesasAdm.listaPorAno(despesasAdm, ano);
    }

    private List<DespesaAdm> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomDespesaAdmDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<DespesaAdm> despesasAdm = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraDespesasAdm.listaPorAno(despesasAdm, ano);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback
    ) {
        handler.post(() -> callback.finalizado(resourceData));
    }

}

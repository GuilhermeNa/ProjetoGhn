package br.com.transporte.appGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.appGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaImpostosPorAnoECavaloIdTask extends BaseTask {
    public BuscaImpostosPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomDespesaImpostoDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<DespesasDeImposto> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetDespesaImposto(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<DespesasDeImposto> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomDespesaImpostoDao dao,
            final int ano
    ) {
        List<DespesasDeImposto> despesasImposto = dao.buscaTodosParaTask();
        return FiltraDespesasImposto.listaPorAno(despesasImposto, ano);
    }

    private List<DespesasDeImposto> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomDespesaImpostoDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<DespesasDeImposto> despesasImposto = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraDespesasImposto.listaPorAno(despesasImposto, ano);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback
    ) {
        handler.post(() -> callback.finalizado(resourceData));
    }


}

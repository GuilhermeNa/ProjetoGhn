package br.com.transporte.AppGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.filtros.FiltraDespesasCertificado;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaCertificadosPorAnoECavaloIdTask extends BaseTask {
    public BuscaCertificadosPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomDespesaCertificadoDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<DespesaCertificado> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetDespesaCertificado(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<DespesaCertificado> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomDespesaCertificadoDao dao,
            final int ano
    ) {
        List<DespesaCertificado> despesaCertificados = dao.buscaTodosParaTask();
        return FiltraDespesasCertificado.listaPorAno(despesaCertificados, ano);
    }

    private List<DespesaCertificado> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomDespesaCertificadoDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<DespesaCertificado> despesaCertificados = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraDespesasCertificado.listaPorAno(despesaCertificados, ano);
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

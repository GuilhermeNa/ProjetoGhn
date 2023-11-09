package br.com.transporte.appGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaCustosPercursoPorAnoECavaloIdTask extends BaseTask {
    public BuscaCustosPercursoPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomCustosPercursoDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<CustosDePercurso> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetCustoPercurso(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<CustosDePercurso> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomCustosPercursoDao dao,
            final int ano
    ) {
        List<CustosDePercurso> custosPercurso = dao.buscaTodosParaTask();
        return FiltraCustosPercurso.listaPorAno(custosPercurso, ano);
    }

    private List<CustosDePercurso> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomCustosPercursoDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<CustosDePercurso> custosPercurso = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraCustosPercurso.listaPorAno(custosPercurso, ano);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback

    ) {
        handler.post(() -> callback.finalizado(resourceData));
    }

}

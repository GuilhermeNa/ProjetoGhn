package br.com.transporte.appGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosDeManutencaoDao;
import br.com.transporte.appGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaManutencaoPorAnoECavaloIdTask extends BaseTask {
    public BuscaManutencaoPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------


    public void solicitaBusca(
            final RoomCustosDeManutencaoDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<CustosDeManutencao> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetCustoManutencao(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<CustosDeManutencao> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomCustosDeManutencaoDao dao,
            final int ano
    ) {
        List<CustosDeManutencao> custosDeManutencao = dao.buscaTodosParaTask();
        return FiltraCustosManutencao.listaPorAno(custosDeManutencao, ano);
    }

    private List<CustosDeManutencao> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomCustosDeManutencaoDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<CustosDeManutencao> custosDeManutencao = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraCustosManutencao.listaPorAno(custosDeManutencao, ano);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback

    ) {
        handler.post(() -> callback.finalizado(resourceData));
    }


}

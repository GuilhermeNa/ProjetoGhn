package br.com.transporte.appGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.appGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaAbastecimentosPorAnoECavaloIdTask extends BaseTask {
    public BuscaAbastecimentosPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomCustosAbastecimentoDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<CustosDeAbastecimento> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetAbastecimento(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<CustosDeAbastecimento> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomCustosAbastecimentoDao dao,
            final int ano
    ) {
        List<CustosDeAbastecimento> abastecimentos = dao.buscaTodosParaTask();
        return FiltraCustosAbastecimento.listaPorAno(abastecimentos, ano);
    }

    private List<CustosDeAbastecimento> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomCustosAbastecimentoDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<CustosDeAbastecimento> abastecimentos = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraCustosAbastecimento.listaPorAno(abastecimentos, ano);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback

    ) {
        handler.post(() -> callback.finalizado(resourceData));
    }

}

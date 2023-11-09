package br.com.transporte.appGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.appGhn.filtros.FiltraParcelaSeguroVida;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaDespesasVidaPorAnoECavaloIdTask extends BaseTask {
    public BuscaDespesasVidaPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomParcela_seguroVidaDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<Parcela_seguroVida> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetDespesaSeguroVida(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<Parcela_seguroVida> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomParcela_seguroVidaDao dao,
            final int ano
    ) {
        List<Parcela_seguroVida> parcelaSeguroVida = dao.buscaTodosParaTask();
        return FiltraParcelaSeguroVida.listaPorAno(parcelaSeguroVida, ano);
    }

    private List<Parcela_seguroVida> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomParcela_seguroVidaDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<Parcela_seguroVida> parcelaSeguroVida = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraParcelaSeguroVida.listaPorAno(parcelaSeguroVida, ano);
    }

    private void notificaResultado(
            final ResourceData resourceData,
            @NonNull final TaskCallback<ResourceData> callback
    ) {
        handler.post(() -> callback.finalizado(resourceData));
    }

}

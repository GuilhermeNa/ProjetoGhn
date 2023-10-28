package br.com.transporte.AppGhn.tasks.fragmentDesempenho;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.filtros.FiltraParcelaSeguroFrota;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class BuscaDespesasFrotaPorAnoECavaloIdTask extends BaseTask {
    public BuscaDespesasFrotaPorAnoECavaloIdTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomParcela_seguroFrotaDao dao,
            final int ano,
            @Nullable final Long cavaloId,
            final TaskCallback<ResourceData> callback
    ) {
        final ResourceData resourceData = new ResourceData();

        executor.execute(
                () -> {
                    List<Parcela_seguroFrota> lista;
                    if (cavaloId == null) {
                        lista = realizaBuscaDeTodosAssincrona(dao, ano);
                    } else {
                        lista = realizaBuscaPorCavaloAssincrona(dao, ano, cavaloId);
                    }
                    resourceData.setDataSetDespesaSeguroFrota(lista);
                    notificaResultado(resourceData, callback);
                });
    }

    private List<Parcela_seguroFrota> realizaBuscaDeTodosAssincrona(
            @NonNull final RoomParcela_seguroFrotaDao dao,
            final int ano
    ) {
        List<Parcela_seguroFrota> parcelaSeguroFrota = dao.buscaTodosParaTask();
        return FiltraParcelaSeguroFrota.listaPorAno(parcelaSeguroFrota, ano);
    }

    private List<Parcela_seguroFrota> realizaBuscaPorCavaloAssincrona(
            @NonNull final RoomParcela_seguroFrotaDao dao,
            final int ano,
            final Long cavaloId
    ) {
        List<Parcela_seguroFrota> parcelaSeguroFrota = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraParcelaSeguroFrota.listaPorAno(parcelaSeguroFrota, ano);
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

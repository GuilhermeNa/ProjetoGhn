package br.com.transporte.AppGhn.tasks.despesaAdm;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

public class FiltraDespesaAdmPorDataTask extends BaseTask {

    public FiltraDespesaAdmPorDataTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final List<DespesaAdm> dataSet,
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            final TaskCallback<List<DespesaAdm>> callback
    ) {
        executor.execute(
                () -> {
                    final List<DespesaAdm> lista = realizaBuscaAssincrona(dataSet, dataInicial, dataFinal);
                    notificaResultado(lista, callback);
                });
    }

    private List<DespesaAdm> realizaBuscaAssincrona(
            final List<DespesaAdm> dataSet,
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        return FiltraDespesasAdm.listaPorData(dataSet, dataInicial, dataFinal);
    }

    private void notificaResultado(
            final List<DespesaAdm> lista,
            final TaskCallback<List<DespesaAdm>> callback
    ) {
        handler.post(() -> {
            callback.finalizado(lista);
        });
    }

}

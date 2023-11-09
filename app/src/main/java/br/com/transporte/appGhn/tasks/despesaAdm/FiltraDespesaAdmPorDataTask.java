package br.com.transporte.appGhn.tasks.despesaAdm;

import android.os.Handler;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.filtros.FiltraDespesasAdm;
import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

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
        handler.post(() -> callback.finalizado(lista));
    }

}

package br.com.transporte.appGhn.tasks.custoPercurso;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;
import br.com.transporte.appGhn.tasks.BaseTask;
import br.com.transporte.appGhn.tasks.TaskCallback;

public class BuscaCustoPorCavaloEDataTask extends BaseTask {
    public BuscaCustoPorCavaloEDataTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    //----------------------------------------------------------------------------------------------

    public void solicitaBusca(
            final RoomCustosPercursoDao dao,
            final Long cavaloId,
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            final TaskCallback<List<CustosDePercurso>> callback
    ) {
        executor.execute(
                () -> {
                    final List<CustosDePercurso> lista = realizaBuscaSincrona(dao, cavaloId, dataInicial, dataFinal);
                    notificaResultado(lista, callback);
                });
    }

    private List<CustosDePercurso> realizaBuscaSincrona(
            @NonNull final RoomCustosPercursoDao dao,
            final Long cavaloId,
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        List<CustosDePercurso> lista = dao.buscaPorCavaloIdParaTask(cavaloId);
        return FiltraCustosPercurso.listaPorData(lista, dataInicial, dataFinal);


    }

    private void notificaResultado(
            final List<CustosDePercurso> lista,
            @NonNull final TaskCallback<List<CustosDePercurso>> callback) {
        handler.post(
                () -> callback.finalizado(lista));
    }

}

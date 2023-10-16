package br.com.transporte.AppGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomDespesaAdmDao;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.repository.RepositoryCallback;
import br.com.transporte.AppGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.AppGhn.tasks.despesaAdm.AdicionaDespesaAdmTask;
import br.com.transporte.AppGhn.tasks.despesaAdm.AlteraDespesaAdmTask;
import br.com.transporte.AppGhn.tasks.despesaAdm.FiltraDespesaAdmPorDataTask;
import br.com.transporte.AppGhn.tasks.despesaAdm.DeletaDespesaAdmTask;

public class LocalDespesaAdmDataSource {
    private final RoomDespesaAdmDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalDespesaAdmDataSource(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomDespesaAdmDao();
        final GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    public void adiciona(
            final DespesaAdm despesaAdm,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaDespesaAdmTask task = new AdicionaDespesaAdmTask(executor, handler);
        task.solicitaAdicao(dao, despesaAdm,
                callback::sucesso
        );
    }

    public void edita(
            final DespesaAdm despesaAdm,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AlteraDespesaAdmTask task = new AlteraDespesaAdmTask(executor, handler);
        task.solicitaAlteracao(dao, despesaAdm,
                callback::quandoFinaliza);
    }

    public void deleta(
            final DespesaAdm despesa,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final DeletaDespesaAdmTask task = new DeletaDespesaAdmTask(executor, handler);
        task.solicitaRemocao(dao, despesa,
                callback::quandoFinaliza);
    }

    public LiveData<DespesaAdm> localizaPeloId(final long id) {
        return dao.localizaPeloId(id);
    }

    public LiveData<List<DespesaAdm>> buscaTodos() {
        return dao.buscaTodos();
    }

    public LiveData<List<DespesaAdm>> buscaPorTipo(final TipoDespesa tipoDespesa) {
        return dao.buscaPorTipo(tipoDespesa);
    }

    public void filtraDespesaAdmPorData(
            final List<DespesaAdm> dataSet,
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            final RepositoryCallback<List<DespesaAdm>> callback
    ) {
        final FiltraDespesaAdmPorDataTask task = new FiltraDespesaAdmPorDataTask(executor, handler);
        task.solicitaBusca(dataSet, dataInicial, dataFinal,
                despesaAdm -> {
                    if(despesaAdm != null){
                        callback.sucesso(despesaAdm);
                    }
                });
    }
}

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
import br.com.transporte.AppGhn.database.dao.RoomFreteDao;
import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.repository.RepositoryCallback;
import br.com.transporte.AppGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.AppGhn.tasks.frete.AdicionaFreteTask;
import br.com.transporte.AppGhn.tasks.frete.AtualizaFreteTask;
import br.com.transporte.AppGhn.tasks.frete.BuscaPorCavaloEDataTask;
import br.com.transporte.AppGhn.tasks.frete.DeletaFreteTask;

public class LocalFreteDataSource {
    private final RoomFreteDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalFreteDataSource(Context context) {
        this.dao = GhnDataBase.getInstance(context).getRoomFreteDao();
        final GhnApplication application = new GhnApplication();
        this.executor = application.getExecutorService();
        this.handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<List<Frete>> buscaFretes() {
        return dao.todos();
    }

    public LiveData<List<Frete>> buscaFretesPorCavaloId(final long cavaloId) {
        return dao.listaPorCavaloId(cavaloId);
    }

    public LiveData<Frete> localizaFrete(final long id) {
        return dao.localizaPeloId(id);
    }

    public void adicionaFrete(
            final Frete frete,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaFreteTask task = new AdicionaFreteTask(executor, handler);
        task.solicitaAdicao(frete, dao,
                callback::sucesso
        );
    }

    public void editaFrete(
            final Frete frete,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaFreteTask task = new AtualizaFreteTask(executor, handler);
        task.solicitaAtualizacao(dao, frete,
                callback::quandoFinaliza);
    }

    public void deletaFrete(
            final Frete frete,
            @NonNull final RepositoryCallback<String> callback
    ) {
        final DeletaFreteTask task = new DeletaFreteTask(executor, handler);
        if (frete != null) {
            task.solicitaRemocao(dao, frete,
                    () -> callback.sucesso(null)
            );
        } else {
            callback.falha("Falha ao remover");
        }
    }

    public LiveData<List<Frete>> buscaFretesPorStatusRecebimento(final boolean isRecebido){
        return dao.listaPorStatusDeRecebimento(isRecebido);
    }

    public void bucaFretePorCavaloEData(
            final Long id,
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            @NonNull RepositoryCallback<List<Frete>> callback
    ){
        final BuscaPorCavaloEDataTask task = new BuscaPorCavaloEDataTask(executor, handler);
        task.solicitaBusca(dao, id, dataInicial, dataFinal,
                callback::sucesso);
    }

}

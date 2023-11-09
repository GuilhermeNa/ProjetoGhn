package br.com.transporte.appGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomDespesaComSeguroFrotaDao;
import br.com.transporte.appGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.appGhn.model.enums.TipoDespesa;
import br.com.transporte.appGhn.repository.RepositoryCallback;
import br.com.transporte.appGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.appGhn.tasks.seguroFrota.AdicionaSeguroFrotaTask;
import br.com.transporte.appGhn.tasks.seguroFrota.DeletaSeguroFrotaTask;
import br.com.transporte.appGhn.tasks.seguroFrota.EditaSeguroFrotaTask;

public class LocalSeguroFrotaDataSource {
    private final RoomDespesaComSeguroFrotaDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalSeguroFrotaDataSource(Context context) {
        this.dao = GhnDataBase.getInstance(context).getRoomDespesaComSeguroFrotaDao();
        final GhnApplication application = new GhnApplication();
        this.executor = application.getExecutorService();
        this.handler = application.getMainThreadHandler();
    }

//----------------------------------------------------------------------------------------------

    public void adiciona(
            final DespesaComSeguroFrota seguro,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaSeguroFrotaTask task = new AdicionaSeguroFrotaTask(executor, handler);
        task.solicitaAdicao(seguro, dao,
                callback::sucesso);
    }

    public void edita(
            final DespesaComSeguroFrota seguro,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final EditaSeguroFrotaTask task = new EditaSeguroFrotaTask(executor, handler);
        task.solicitaAtualizacao(dao, seguro,
                callback::quandoFinaliza);
    }

    public void deleta(
            final DespesaComSeguroFrota seguro,
            final RepositoryCallback<String> callback
    ) {
        final DeletaSeguroFrotaTask task = new DeletaSeguroFrotaTask(executor, handler);
        if (seguro != null) {
            task.solicitaRemocao(dao, seguro,
                    () -> callback.sucesso(null));
        } else {
            callback.falha("Falha ao remover");
        }

    }

    public LiveData<List<DespesaComSeguroFrota>> buscaTodos() {
        return dao.todos();
    }


    public LiveData<List<DespesaComSeguroFrota>> buscaPorTipo(final TipoDespesa tipo) {
        return dao.buscaPorTipo(tipo);
    }

    public LiveData<List<DespesaComSeguroFrota>> buscaPorStatus(boolean isValido) {
        return dao.buscaPorStatus(isValido);
    }

    public LiveData<DespesaComSeguroFrota> localizaPeloId(final Long id) {
        return dao.localizaPeloId(id);
    }
}

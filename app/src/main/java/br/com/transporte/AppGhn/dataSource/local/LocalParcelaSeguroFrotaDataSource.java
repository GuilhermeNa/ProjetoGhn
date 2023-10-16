package br.com.transporte.AppGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomParcela_seguroFrotaDao;
import br.com.transporte.AppGhn.model.parcelas.Parcela_seguroFrota;
import br.com.transporte.AppGhn.repository.RepositoryCallback;
import br.com.transporte.AppGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.AppGhn.tasks.parcelaFrota.AdicionaListaParcelasFrotaTask;
import br.com.transporte.AppGhn.tasks.parcelaFrota.AdicionaParcelaFrotaTask;
import br.com.transporte.AppGhn.tasks.parcelaFrota.EditaParcelaFrotaTask;

public class LocalParcelaSeguroFrotaDataSource {
    private final RoomParcela_seguroFrotaDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalParcelaSeguroFrotaDataSource(Context context) {
        this.dao = GhnDataBase.getInstance(context).getRoomParcela_seguroFrotaDao();
        final GhnApplication application = new GhnApplication();
        this.executor = application.getExecutorService();
        this.handler = application.getMainThreadHandler();
    }

//----------------------------------------------------------------------------------------------

    public LiveData<List<Parcela_seguroFrota>> buscaParcelasPorSeguroId(final Long seguroId) {
        return dao.listaPeloSeguroId(seguroId);
    }


    public void edita(
            final Parcela_seguroFrota parcela,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final EditaParcelaFrotaTask task = new EditaParcelaFrotaTask(executor, handler);
        task.solicitaAtualizacao(dao, parcela,
                callback::quandoFinaliza);
    }

    public void adiciona(
            final Parcela_seguroFrota parcela,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaParcelaFrotaTask task = new AdicionaParcelaFrotaTask(executor, handler);
        task.solicitaAdicao(parcela, dao,
                callback::sucesso);
    }

    public void adicionaLista(
            final List<Parcela_seguroFrota> listaParcelas,
            @NonNull final RepositoryCallbackVoid callback
            ) {
        final AdicionaListaParcelasFrotaTask task = new AdicionaListaParcelasFrotaTask(executor, handler);
        task.solicitaAdicao(listaParcelas, dao,
                callback::quandoFinaliza);

    }
}

package br.com.transporte.appGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomParcela_seguroVidaDao;
import br.com.transporte.appGhn.model.parcelas.Parcela_seguroVida;
import br.com.transporte.appGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.appGhn.tasks.parcelaVida.AdicionaListaParcelasVidaTask;
import br.com.transporte.appGhn.tasks.parcelaVida.EditaParcelaVidaTask;

public class LocalParcelaSeguroVidaDataSource {
    private final RoomParcela_seguroVidaDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalParcelaSeguroVidaDataSource(Context context) {
        this.dao = GhnDataBase.getInstance(context).getRoomParcela_seguroVidaDao();
        final GhnApplication application = new GhnApplication();
        this.executor = application.getExecutorService();
        this.handler = application.getMainThreadHandler();
    }

//----------------------------------------------------------------------------------------------

    public LiveData<List<Parcela_seguroVida>> buscaParcelasPorSeguroId(final Long seguroId) {
        return dao.listaPeloSeguroId(seguroId);
    }

    public void edita(
            final Parcela_seguroVida parcela,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final EditaParcelaVidaTask task = new EditaParcelaVidaTask(executor, handler);
        task.solicitaAtualizacao(dao, parcela,
                callback::quandoFinaliza);
    }

    public void adicionaLista(
            final List<Parcela_seguroVida> parcelasDoSeguro,
            @NonNull final RepositoryCallbackVoid callback
            ) {
        final AdicionaListaParcelasVidaTask task = new AdicionaListaParcelasVidaTask(executor, handler);
        task.solicitaAdicao(parcelasDoSeguro, dao,
                callback::quandoFinaliza);
    }

}

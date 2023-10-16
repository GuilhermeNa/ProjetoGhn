package br.com.transporte.AppGhn.dataSource.local;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.repository.RepositoryCallback;
import br.com.transporte.AppGhn.repository.RepositoryCallbackVoid;
import br.com.transporte.AppGhn.tasks.certificado.AdicionaCertificadoTask;
import br.com.transporte.AppGhn.tasks.certificado.AtualizaCertificadoTask;
import br.com.transporte.AppGhn.tasks.certificado.BuscaDuplicidadeCertificadoDespesaDiretaTask;
import br.com.transporte.AppGhn.tasks.certificado.BuscaDuplicidadeCertificadoDespesaIndiretaTask;
import br.com.transporte.AppGhn.tasks.certificado.DeletaCertificadoTask;

public class LocalCertificadoDataSource {
    private final RoomDespesaCertificadoDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public LocalCertificadoDataSource(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomDespesaCertificadoDao();
        final GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<List<DespesaCertificado>> buscaCertificados() {
        return dao.todos();
    }

    public LiveData<List<DespesaCertificado>> buscaCertificadosPorCavaloId(final long cavaloId) {
        return dao.listaPorCavaloId(cavaloId);
    }

    public LiveData<DespesaCertificado> localizaCertificado(final long id) {
        return dao.localizaPeloId(id);
    }

    public void adicionaCertificado(
            final DespesaCertificado certificado,
            final RepositoryCallback<Long> callback
    ) {
        final AdicionaCertificadoTask task = new AdicionaCertificadoTask(executor, handler);
        task.solicitaAdicao(dao, certificado,
                id -> {
                    if (id > 0) {
                        callback.sucesso(id);
                    } else {
                        callback.falha("Falha ao adicionar");
                    }
                });
    }

    public void editaCertificado(
            final DespesaCertificado certificado,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaCertificadoTask task = new AtualizaCertificadoTask(executor, handler);
        task.solicitaAtualizacao(dao, certificado,
                callback::quandoFinaliza);
    }

    public void deletaCertificado(
            final DespesaCertificado certificado,
            final RepositoryCallback<String> callback
    ) {
        final DeletaCertificadoTask task = new DeletaCertificadoTask(executor, handler);
        if (certificado != null) {
            task.solicitaRemocao(dao, certificado,
                    () -> callback.sucesso(null)
            );
        } else {
            callback.falha("Falha ao remover");
        }
    }

    public LiveData<List<DespesaCertificado>> buscaPorTipo(final TipoDespesa tipo) {
        return dao.buscaPorTipo(tipo);
    }

    public LiveData<List<DespesaCertificado>> buscaPorCavaloId(final Long cavaloId) {
        return dao.listaPorCavaloId(cavaloId);
    }

    public void buscaPorDuplicidadeQuandoDespesaIndireta(
            final TipoDespesa tipoDespesa,
            final boolean isValido,
            final TipoCertificado tipoCertificado,
            @NonNull final RepositoryCallback<List<DespesaCertificado>> callback
    ) {
        final BuscaDuplicidadeCertificadoDespesaIndiretaTask task = new BuscaDuplicidadeCertificadoDespesaIndiretaTask(executor, handler);
        task.solicitaBusca(dao, tipoDespesa, tipoCertificado, isValido,
                callback::sucesso);
    }

    public void buscaPorDuplicidadeQuandoDespesaDireta(
            final boolean isValido,
            final Long cavaloId,
            final TipoCertificado tipoCertificado,
            @NonNull final RepositoryCallback<List<DespesaCertificado>> callback
    ) {
        final BuscaDuplicidadeCertificadoDespesaDiretaTask task = new BuscaDuplicidadeCertificadoDespesaDiretaTask(executor, handler);
        task.solicitaBusca(dao, tipoCertificado, isValido, cavaloId,
                callback::sucesso);
    }

}

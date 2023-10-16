package br.com.transporte.AppGhn.tasks.certificado;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.database.dao.RoomDespesaCertificadoDao;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;
import br.com.transporte.AppGhn.tasks.BaseTask;
import br.com.transporte.AppGhn.tasks.TaskCallback;

public class BuscaDuplicidadeCertificadoDespesaDiretaTask extends BaseTask {

    public BuscaDuplicidadeCertificadoDespesaDiretaTask(ExecutorService executor, Handler handler) {
        super(executor, handler);
    }

    public void solicitaBusca(
            final RoomDespesaCertificadoDao dao,
            final TipoCertificado tipoCertificado,
            final boolean isValido,
            final Long cavaloId,
            final TaskCallback<List<DespesaCertificado>> callback
    ) {
        executor.execute(() -> {
            final List<DespesaCertificado> lista = realizaBusca(dao, tipoCertificado, isValido, cavaloId);
            notificaResultado(lista, callback);
        });
    }

    @Nullable
    @Contract(pure = true)
    private List<DespesaCertificado> realizaBusca(
            @NonNull final RoomDespesaCertificadoDao dao,
            final TipoCertificado tipoCertificado,
            final boolean isValido,
            final Long cavaloId
    ) {
        return dao.buscaDuplicidadeQuandoDespesaDireta(isValido, cavaloId, tipoCertificado);
    }

    private void notificaResultado(
            final List<DespesaCertificado> lista,
            final TaskCallback<List<DespesaCertificado>> callback
    ) {
        handler.post(() -> {
            callback.finalizado(lista);
        });
    }
}

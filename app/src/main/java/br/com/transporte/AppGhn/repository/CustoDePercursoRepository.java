package br.com.transporte.AppGhn.repository;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.AppGhn.GhnApplication;
import br.com.transporte.AppGhn.database.GhnDataBase;
import br.com.transporte.AppGhn.database.dao.RoomCustosPercursoDao;
import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.tasks.custoPercurso.AdicionaCustoDePercursoTask;
import br.com.transporte.AppGhn.tasks.custoPercurso.AtualizaCustoDePercursoTask;
import br.com.transporte.AppGhn.tasks.custoPercurso.DeletaCustoDePercursoTask;

public class CustoDePercursoRepository {
    private final RoomCustosPercursoDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public CustoDePercursoRepository(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomCustosPercursoDao();
        final GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<CustosDePercurso>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<CustosDePercurso>>> buscaCustosPercurso() {
        mediator.addSource(buscaCustosPercurso_room(),
                custosPercurso -> {
                    mediator.setValue(new Resource<>(custosPercurso, null));
                });
        return mediator;
    }

    public LiveData<Resource<List<CustosDePercurso>>> buscaCustosPercursoPorCavaloId(final long cavaloId) {
        mediator.addSource(buscaCustosPercursoPorCavaloId_room(cavaloId),
                custosPercurso -> {
                    mediator.setValue(new Resource<>(custosPercurso, null));
                });
        return mediator;
    }

    public LiveData<CustosDePercurso> localizaCustoPercurso(final long id) {
        return localizaCustoPercurso_room(id);
    }

    public LiveData<Long> adicionaCustoPercurso(final CustosDePercurso custoPercurso) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaCustoPercurso_room(custoPercurso, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long id) {
                liveData.setValue(id);
            }

            @Override
            public void falha(String ignore) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    public LiveData<Long> editCustoPercurso(final CustosDePercurso custoPercurso) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        editaCustoPercurso_room(custoPercurso,
                () -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<String> deletaCustoPercurso(final CustosDePercurso custoPercurso) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        deletaCustoPercurso_room(custoPercurso, new RepositoryCallback<String>() {
            @Override
            public void sucesso(String ignore) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String erro) {
                liveData.setValue(erro);
            }
        });
        return liveData;
    }

    //----------------------------------------------------------------------------------------------
    //                                       Busca Interna                                        ||
    //----------------------------------------------------------------------------------------------

    private LiveData<List<CustosDePercurso>> buscaCustosPercurso_room() {
        return dao.todos();
    }

    private LiveData<List<CustosDePercurso>> buscaCustosPercursoPorCavaloId_room(final long cavaloId) {
        return dao.listaPorCavaloId(cavaloId);
    }

    private LiveData<CustosDePercurso> localizaCustoPercurso_room(final long id) {
        return dao.localizaPeloId(id);
    }

    private void adicionaCustoPercurso_room(
            final CustosDePercurso custoPercurso,
            final RepositoryCallback<Long> callback
    ) {
        final AdicionaCustoDePercursoTask task = new AdicionaCustoDePercursoTask(executor, handler);
        task.solicitaAdicao(dao, custoPercurso,
                id -> {
                    if (id > 0) {
                        callback.sucesso(id);
                    } else {
                        callback.falha("Falha ao adicionar");
                    }
                }
        );
    }

    private void editaCustoPercurso_room(
            final CustosDePercurso custoPercurso,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaCustoDePercursoTask task = new AtualizaCustoDePercursoTask(executor, handler);
        task.solicitaAtualizacao(dao, custoPercurso,
                callback::quandoFinaliza);
    }

    private void deletaCustoPercurso_room(
            final CustosDePercurso custoPercurso,
            final RepositoryCallback<String> callback
    ) {
        final DeletaCustoDePercursoTask task = new DeletaCustoDePercursoTask(executor, handler);
        if (custoPercurso != null) {
            task.solicitaRemocao(dao, custoPercurso,
                    () -> callback.sucesso(null));
        } else {
            callback.falha("Falha ao remover");
        }
    }

}

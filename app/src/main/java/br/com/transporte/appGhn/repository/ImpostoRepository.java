package br.com.transporte.appGhn.repository;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomDespesaImpostoDao;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.tasks.imposto.AdicionaImpostoTask;
import br.com.transporte.appGhn.tasks.imposto.AtualizaImpostoTask;
import br.com.transporte.appGhn.tasks.imposto.DeletaImpostoTask;

public class ImpostoRepository {
    private final RoomDespesaImpostoDao dao;
    private final Handler handler;
    private final ExecutorService executor;

    public ImpostoRepository(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomDespesaImpostoDao();
        final GhnApplication app = new GhnApplication();
        executor = app.getExecutorService();
        handler = app.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<DespesasDeImposto>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<DespesasDeImposto>>> buscaImpostos() {
        mediator.addSource(buscaImpostos_room(),
                lista -> mediator.setValue(new Resource<>(lista, null)));
        return mediator;
    }

    public LiveData<DespesasDeImposto> localizaImposto(final long id) {
        return localizaImposto_room(id);
    }

    public LiveData<Long> adiciona(final DespesasDeImposto imposto) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adiciona_room(imposto, new RepositoryCallback<Long>() {
                    @Override
                    public void sucesso(Long id) {
                        liveData.setValue(id);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                }
        );
        return liveData;
    }

    public LiveData<Long> atualiza(final DespesasDeImposto imposto) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        atualiza_room(imposto,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<String> deleta(final DespesasDeImposto imposto) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        deleta_room(imposto,
                () -> liveData.setValue(null)
        );
        return liveData;
    }


    //----------------------------------------------------------------------------------------------

    private LiveData<List<DespesasDeImposto>> buscaImpostos_room() {
        return dao.todos();
    }

    private LiveData<DespesasDeImposto> localizaImposto_room(final long id) {
        return dao.localizaPeloId(id);
    }

    private void adiciona_room(
            final DespesasDeImposto imposto,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaImpostoTask task = new AdicionaImpostoTask(executor, handler);
        task.solicitaAdicao(dao, imposto,
                callback::sucesso
        );
    }

    private void atualiza_room(
            final DespesasDeImposto imposto,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaImpostoTask task = new AtualizaImpostoTask(executor, handler);
        task.solicitaAtualizacao(dao, imposto,
                callback::quandoFinaliza
        );
    }

    private void deleta_room(
            final DespesasDeImposto imposto,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final DeletaImpostoTask task = new DeletaImpostoTask(executor, handler);
        task.solicitaRemocao(dao, imposto,
                callback::quandoFinaliza
        );
    }


}

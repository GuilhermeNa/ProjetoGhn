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
import br.com.transporte.AppGhn.database.dao.RoomCustosAbastecimentoDao;
import br.com.transporte.AppGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.AppGhn.tasks.custoAbastecimento.AdicionaCustoDeAbastecimentoTask;
import br.com.transporte.AppGhn.tasks.custoAbastecimento.AtualizaCustoDeAbastecimentoTask;
import br.com.transporte.AppGhn.tasks.custoAbastecimento.DeletaCustoDeAbastecimentoTask;

public class CustoDeAbastecimentoRepository {
    private final RoomCustosAbastecimentoDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public CustoDeAbastecimentoRepository(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomCustosAbastecimentoDao();
        final GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<CustosDeAbastecimento>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<CustosDeAbastecimento>>> buscaAbastecimentos() {
        mediator.addSource(buscaAbastecimentos_room(),
                abastecimentos -> {
                    mediator.setValue(new Resource<>(abastecimentos, null));
                });
        return mediator;
    }

    public LiveData<Resource<List<CustosDeAbastecimento>>> buscaAbastecimentosPorCavaloId(final long cavaloId) {
        mediator.addSource(buscaAbastecimentosPorCavaloId_room(cavaloId),
                abastecimentos -> {
                    mediator.setValue(new Resource<>(abastecimentos, null));
                });
        return mediator;
    }

    public LiveData<CustosDeAbastecimento> localizaAbastecimento(final long id) {
        return localizaAbastecimento_room(id);
    }

    public LiveData<List<CustosDeAbastecimento>> buscaAbastecimentoTotalPorCavalo(final Long id){
        return buscaAbastecimentoTotalPorCavalo_room(id);
    }
    public LiveData<Long> adicionaAbastecimento(final CustosDeAbastecimento abastecimento) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaAbastecimento_room(abastecimento, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long id) {
                liveData.setValue(id);
            }

            @Override
            public void falha(String msg) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    public LiveData<Long> editaAbastecimento(final CustosDeAbastecimento abastecimento) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        editaAbastecimento_room(abastecimento,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<String> deletaAbastecimento(final CustosDeAbastecimento abastecimento) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        deletaAbastecimento_room(abastecimento, new RepositoryCallback<String>() {
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

    private LiveData<List<CustosDeAbastecimento>> buscaAbastecimentos_room() {
        return dao.todos();
    }

    private LiveData<List<CustosDeAbastecimento>> buscaAbastecimentosPorCavaloId_room(final long cavaloId) {
        return dao.listaPorCavaloId(cavaloId);
    }

    private LiveData<CustosDeAbastecimento> localizaAbastecimento_room(final long id) {
        return dao.localizaPeloId(id);
    }

    private void adicionaAbastecimento_room(
            final CustosDeAbastecimento abastecimento,
            final RepositoryCallback<Long> callback
    ) {
        final AdicionaCustoDeAbastecimentoTask task = new AdicionaCustoDeAbastecimentoTask(executor, handler);
        task.solicitaAdicao(dao, abastecimento,
                id -> {
                    if (id > 0) {
                        callback.sucesso(id);
                    } else {
                        callback.falha("Falha ao adicionar");
                    }
                });
    }

    private void editaAbastecimento_room(
            final CustosDeAbastecimento abastecimento,
            @NonNull final RepositoryCallbackVoid callback
    ) {
        final AtualizaCustoDeAbastecimentoTask task = new AtualizaCustoDeAbastecimentoTask(executor, handler);
        task.solicitaAtualizacao(dao, abastecimento,
                callback::quandoFinaliza);
    }

    private void deletaAbastecimento_room(
            final CustosDeAbastecimento abastecimento,
            final RepositoryCallback<String> callback
    ) {
        final DeletaCustoDeAbastecimentoTask task = new DeletaCustoDeAbastecimentoTask(executor, handler);
        if (abastecimento != null) {
            task.solicitaRemocao(dao, abastecimento,
                    () -> callback.sucesso(null)
            );
        } else {
            callback.falha("Falha ao remover");
        }
    }

    private LiveData<List<CustosDeAbastecimento>> buscaAbastecimentoTotalPorCavalo_room(final Long id){
        return dao.listaDeAbastecimentoTotal(id, true);
    }


}

package br.com.transporte.appGhn.repository;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomMotoristaDao;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.tasks.motorista.AdicionaMotoristaTask;
import br.com.transporte.appGhn.tasks.motorista.AtualizaMotoristaTask;
import br.com.transporte.appGhn.tasks.motorista.DeletaMotoristaTask;

public class MotoristaRepository {
    private final RoomMotoristaDao dao;
    private final Handler handler;
    private final ExecutorService executor;

    public MotoristaRepository(final Context context) {
        dao = GhnDataBase.getInstance(context)
                .getRoomMotoristaDao();
        GhnApplication application = new GhnApplication();
        handler = application.getMainThreadHandler();
        executor = application.getExecutorService();
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<Motorista>>> mediator = new MediatorLiveData<>();

    public MediatorLiveData<Resource<List<Motorista>>> getMediator() {
        return mediator;
    }

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        mediator.addSource(buscaMotoristas_room(), listaMotoristas -> mediator.setValue(new Resource<>(listaMotoristas, null)));
        return mediator;
    }

    public LiveData<Motorista> localizaMotorista(final Long motoristaId) {
        return dao.localizaPeloId(motoristaId);
    }

    public LiveData<Long> editaMotorista(final Motorista motorista) {
        MutableLiveData<Long> liveData = new MutableLiveData<>();
        editaMotorista_room(motorista, new RepositoryCallback<Void>() {
            @Override
            public void sucesso(Void ignore) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String msg) {

            }
        });
        return liveData;
    }

    public LiveData<Long> adicionaMotorista(final Motorista motorista) {
        MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaMotorista_room(motorista, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long idNovoMotorista) {
                liveData.setValue(idNovoMotorista);
            }

            @Override
            public void falha(String ignore) {

            }
        });
        return liveData;
    }

    public LiveData<String> deletaMotorista(final Motorista motorista) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        deletaMotorista_room(motorista, new RepositoryCallback<Void>() {
            @Override
            public void sucesso(Void resultado) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String msg) {
                liveData.setValue(msg);
            }
        });
        return liveData;
    }

    //----------------------------------------------------------------------------------------------
    //                                       Busca Interna                                        ||
    //----------------------------------------------------------------------------------------------

    private LiveData<List<Motorista>> buscaMotoristas_room() {
        return dao.todos();
    }

    private void adicionaMotorista_room(
            final Motorista motorista,
            final RepositoryCallback<Long> callBack
    ) {
        AdicionaMotoristaTask task = new AdicionaMotoristaTask(executor, handler);
        task.solicitaAdicao(dao, motorista,
                idNovoMotorista -> {
                    if (idNovoMotorista > 0) {
                        callBack.sucesso(idNovoMotorista);
                    } else {
                        callBack.falha("Falha ao adicionar");
                    }
                });
    }

    private void editaMotorista_room(
            final Motorista motorista,
            final RepositoryCallback<Void> callBack
    ) {
        AtualizaMotoristaTask task = new AtualizaMotoristaTask(executor, handler);
        task.solicitaAtualizacao(dao, motorista,
                () -> callBack.sucesso(null)
        );
    }

    private void deletaMotorista_room(
            final Motorista motorista,
            final RepositoryCallback<Void> callBack
    ) {
        DeletaMotoristaTask task = new DeletaMotoristaTask(executor, handler);
        if (motorista != null) {
            task.solicitaRemocao(dao, motorista,
                    () -> callBack.sucesso(null)
            );
        } else {
            callBack.falha("Falha ao remover");
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                          Interface                                         ||
    //----------------------------------------------------------------------------------------------

    public interface RepositoryCallback<T> {
        void sucesso(T resultado);

        void falha(String msg);
    }
}

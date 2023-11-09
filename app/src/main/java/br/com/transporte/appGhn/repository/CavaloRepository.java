package br.com.transporte.appGhn.repository;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.transporte.appGhn.GhnApplication;
import br.com.transporte.appGhn.database.GhnDataBase;
import br.com.transporte.appGhn.database.dao.RoomCavaloDao;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.tasks.cavalo.AdicionaCavaloTask;
import br.com.transporte.appGhn.tasks.cavalo.AtualizaCavaloTask;
import br.com.transporte.appGhn.tasks.cavalo.DeletaCavaloTask;
import br.com.transporte.appGhn.tasks.cavalo.LocalizaPelaPlacaTask;

public class CavaloRepository {
    private final RoomCavaloDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public CavaloRepository(final Context context) {
        dao = GhnDataBase.getInstance(context).getRoomCavaloDao();
        final GhnApplication application = new GhnApplication();
        executor = application.getExecutorService();
        handler = application.getMainThreadHandler();
    }

    public interface RepositoryCallback<T> {
        void sucesso(T resultado);

        void falha(String msg);
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<Cavalo>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<Cavalo>>> buscaCavalos() {
        mediator.addSource(buscaCavalos_room(),
                listaCavalos ->
                        mediator.setValue(new Resource<>(listaCavalos, null)));
        return mediator;
    }

    public LiveData<Cavalo> localizaCavaloPeloId(final long cavaloId) {
        return localizaCavalo_room(cavaloId);
    }

    public LiveData<Long> editaCavalo(final Cavalo cavalo) {
        MutableLiveData<Long> liveData = new MutableLiveData<>();
        editaCavalo_room(cavalo, new RepositoryCallback<Void>() {
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

    public LiveData<Long> adicionaCavalo(final Cavalo cavalo) {
        MutableLiveData<Long> liveData = new MutableLiveData<>();
        adicionaCavalo_room(cavalo, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long cavaloId) {
                liveData.setValue(cavaloId);
            }

            @Override
            public void falha(String ignore) {}
        });
        return liveData;
    }

    public LiveData<String> deletaCavalo(final Cavalo cavalo) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        deletaCavalo_room(cavalo, new RepositoryCallback<String>() {
            @Override
            public void sucesso(String resultado) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String erro) {
                liveData.setValue(erro);
            }
        });
        return null;
    }

    public LiveData<List<String>> buscaPlacas(){
        return buscaPlacas_room();
    }

    public LiveData<Cavalo> localizaPelaPlaca(final String placa){
       final MutableLiveData<Cavalo> liveData = new MutableLiveData<>();
        localizaPelaPlaca_room(placa, new RepositoryCallback<Cavalo>() {
            @Override
            public void sucesso(Cavalo cavaloRecebido) {
                liveData.setValue(cavaloRecebido);
            }

            @Override
            public void falha(String msg) {

            }
        });
        return liveData;
    }

    //----------------------------------------------------------------------------------------------
    //                                       Busca Interna                                        ||
    //----------------------------------------------------------------------------------------------

    private LiveData<List<Cavalo>> buscaCavalos_room() {
        return dao.todos();
    }

    private LiveData<Cavalo> localizaCavalo_room(final Long cavaloId) {
        return dao.localizaPeloId(cavaloId);
    }

    @Contract(pure = true)
    private void editaCavalo_room(
            final Cavalo cavalo,
            final RepositoryCallback<Void> callBack
    ) {
        AtualizaCavaloTask task = new AtualizaCavaloTask(executor, handler);
        task.solicitaAtualizacao(dao, cavalo,
                () -> callBack.sucesso(null));
    }

    private void adicionaCavalo_room(
            final Cavalo cavalo,
            final RepositoryCallback<Long> callBack
    ) {
        AdicionaCavaloTask task = new AdicionaCavaloTask(executor, handler);
        task.solicitaAdicao(dao, cavalo,
                cavaloId -> {
                    if (cavaloId > 0) {
                        callBack.sucesso(cavaloId);
                    } else {
                        callBack.falha("Falha ao adicionar");
                    }
                }
        );
    }

    private void deletaCavalo_room(
            final Cavalo cavalo,
            final RepositoryCallback<String> callBack
    ) {
        final DeletaCavaloTask task = new DeletaCavaloTask(executor, handler);
        if (cavalo != null) {
            task.solicitaRemocao(dao, cavalo,
                    () -> callBack.sucesso(null)
            );
        } else {
            callBack.falha("Falha ao remover");
        }
    }

    private LiveData<List<String>> buscaPlacas_room(){
        return dao.pegaPlacas();
    }

    private void localizaPelaPlaca_room(
            final String placa,
            @NonNull final RepositoryCallback<Cavalo> callback
            ){
        final LocalizaPelaPlacaTask task = new LocalizaPelaPlacaTask(executor, handler);
        task.solicitaLocalizacao(dao, placa,
                callback::sucesso);
    }

}

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
import br.com.transporte.AppGhn.database.dao.RoomCustosDeSalarioDao;
import br.com.transporte.AppGhn.model.custos.CustosDeSalario;
import br.com.transporte.AppGhn.tasks.custoSalario.AdicionaCustoSalarioTask;

public class CustoDeSalarioRepository {
    private final RoomCustosDeSalarioDao dao;
    private final ExecutorService executor;
    private final Handler handler;

    public CustoDeSalarioRepository(Context context) {
        dao = GhnDataBase.getInstance(context).getRoomCustosDeSalarioDao();
        final GhnApplication app = new GhnApplication();
        executor = app.getExecutorService();
        handler = app.getMainThreadHandler();
    }

    //----------------------------------------------------------------------------------------------

    final MediatorLiveData<Resource<List<CustosDeSalario>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<CustosDeSalario>>> buscaCustosSalario() {
        mediator.addSource(buscaCustosSalario_room(),
                custosDeSalarios -> mediator.setValue(new Resource<>(custosDeSalarios, null))
        );
        return mediator;
    }

    public LiveData<Long> adiciona(final CustosDeSalario salario){
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        adiciona_room(salario, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long id) {
                liveData.setValue(id);
            }

            @Override
            public void falha(String msg) {}
        });
        return liveData;
    }

    public LiveData<CustosDeSalario> localizaSalario(final Long id){
        return localizaSalario_room(id);
    }

    //----------------------------------------------------------------------------------------------

    private LiveData<List<CustosDeSalario>> buscaCustosSalario_room() {
        return dao.todos();
    }

    private void adiciona_room(
            final CustosDeSalario salario,
            @NonNull final RepositoryCallback<Long> callback
    ) {
        final AdicionaCustoSalarioTask task = new AdicionaCustoSalarioTask(executor, handler);
        task.solicitaAdicao(dao, salario,
                callback::sucesso
        );
    }

    private LiveData<CustosDeSalario> localizaSalario_room(final Long id){
        return dao.localizaPeloId(id);
    }

}

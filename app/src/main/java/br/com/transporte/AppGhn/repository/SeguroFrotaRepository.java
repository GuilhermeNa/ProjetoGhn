package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalSeguroFrotaDataSource;
import br.com.transporte.AppGhn.model.despesas.DespesaComSeguroFrota;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class SeguroFrotaRepository {
    private final LocalSeguroFrotaDataSource localSource;

    public SeguroFrotaRepository(Context context) {
        this.localSource = new LocalSeguroFrotaDataSource(context);
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<List<DespesaComSeguroFrota>> buscaPorTipo(final TipoDespesa tipo) {
        return localSource.buscaPorTipo(tipo);
    }

    public LiveData<List<DespesaComSeguroFrota>> buscaPorStatus(boolean isValido) {
        return localSource.buscaPorStatus(isValido);
    }

    public LiveData<DespesaComSeguroFrota> localizaPorId(final Long id){
        return localSource.localizaPeloId(id);

    }

    public LiveData<String> deleta(final DespesaComSeguroFrota seguro) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
         localSource.deleta(seguro, new RepositoryCallback<String>() {
            @Override
            public void sucesso(String resultado) {
                liveData.setValue(null);
            }

            @Override
            public void falha(String msg) {

            }
        });
         return liveData;
    }

    public LiveData<Long> adiciona(final DespesaComSeguroFrota seguro){
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localSource.adiciona(seguro, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long resultado) {
                liveData.setValue(resultado);
            }

            @Override
            public void falha(String msg) {

            }
        });
        return liveData;
    }

    public LiveData<Long> edita(final DespesaComSeguroFrota seguro) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localSource.edita(seguro, () -> {
            liveData.setValue(null);
        });
        return liveData;
    }



}

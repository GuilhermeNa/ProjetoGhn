package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalDespesaAdmDataSource;
import br.com.transporte.AppGhn.model.despesas.DespesaAdm;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class DespesaAdmRepository {
    private final LocalDespesaAdmDataSource localDespesaAdmDataSource;

    public DespesaAdmRepository(Context context) {
        this.localDespesaAdmDataSource = new LocalDespesaAdmDataSource(context);
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<List<DespesaAdm>> mediator = new MediatorLiveData<>();

    public LiveData<List<DespesaAdm>> buscaTodos() {
        mediator.addSource(localDespesaAdmDataSource.buscaTodos(),
                mediator::setValue);
        return mediator;
    }

    public LiveData<Long> adiciona(final DespesaAdm despesaAdm) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDespesaAdmDataSource.adiciona(despesaAdm, new RepositoryCallback<Long>() {
            @Override
            public void sucesso(Long id) {
                liveData.setValue(id);
            }

            @Override
            public void falha(String msg) {
            }
        });
        return liveData;
    }

    public LiveData<String> deleta(final DespesaAdm despesaAdm) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        localDespesaAdmDataSource.deleta(despesaAdm,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<Long> edita(final DespesaAdm despesaAdm) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDespesaAdmDataSource.edita(despesaAdm,
                () -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<DespesaAdm> localizaPeloId(final Long id) {
        return localDespesaAdmDataSource.localizaPeloId(id);
    }

    //---------------------------------------------------

    public LiveData<List<DespesaAdm>> buscaPorTipoEData(
            final TipoDespesa tipoDespesa,
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        mediator.addSource(localDespesaAdmDataSource.buscaPorTipo(tipoDespesa),
                despesaAdm -> {
                    if (despesaAdm != null) {
                        filtraPorData(dataInicial, dataFinal, despesaAdm);
                    }
                });
        return mediator;
    }

    private void filtraPorData(LocalDate dataInicial, LocalDate dataFinal, List<DespesaAdm> despesaAdm) {
        localDespesaAdmDataSource.filtraDespesaAdmPorData(despesaAdm, dataInicial, dataFinal,
                new RepositoryCallback<List<DespesaAdm>>() {
                    @Override
                    public void sucesso(List<DespesaAdm> resultado) {
                        mediator.setValue(resultado);
                    }

                    @Override
                    public void falha(String msg) {

                    }
                });
    }


}

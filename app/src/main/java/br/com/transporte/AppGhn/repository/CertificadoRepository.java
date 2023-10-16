package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalCertificadoDataSource;
import br.com.transporte.AppGhn.model.despesas.DespesaCertificado;
import br.com.transporte.AppGhn.model.enums.TipoCertificado;
import br.com.transporte.AppGhn.model.enums.TipoDespesa;

public class CertificadoRepository {
    private final LocalCertificadoDataSource localDataSource;

    public CertificadoRepository(Context context) {
        localDataSource = new LocalCertificadoDataSource(context);
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<DespesaCertificado>>> mediator = new MediatorLiveData<>();

    public LiveData<Resource<List<DespesaCertificado>>> buscaCertificados() {
        mediator.addSource(localDataSource.buscaCertificados(),
                certificados -> {
                    mediator.setValue(new Resource<>(certificados, null));
                });
        return mediator;
    }

    public LiveData<Resource<List<DespesaCertificado>>> buscaCertificadosPorCavaloId(final long cavaloId) {
        mediator.addSource(localDataSource.buscaCertificadosPorCavaloId(cavaloId),
                certificados -> {
                    mediator.setValue(new Resource<>(certificados, null));
                });
        return mediator;
    }

    public LiveData<DespesaCertificado> localizaCertificado(final long id) {
        return localDataSource.localizaCertificado(id);
    }

    public LiveData<Long> adicionaCertificado(final DespesaCertificado certificado) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.adicionaCertificado(certificado, new RepositoryCallback<Long>() {
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

    public LiveData<Long> editaCertificado(final DespesaCertificado certificado) {
        final MutableLiveData<Long> liveData = new MutableLiveData<>();
        localDataSource.editaCertificado(certificado,
                () -> liveData.setValue(null)
        );
        return liveData;
    }

    public LiveData<String> deletaCertificado(final DespesaCertificado certificado) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        localDataSource.deletaCertificado(certificado, new RepositoryCallback<String>() {
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

    public LiveData<List<DespesaCertificado>> buscaPorTipo(final TipoDespesa tipo) {
        return localDataSource.buscaPorTipo(tipo);
    }

    public LiveData<List<DespesaCertificado>> buscaPorCavaloId(final Long cavaloId) {
        return localDataSource.buscaPorCavaloId(cavaloId);
    }

    public LiveData<List<DespesaCertificado>> buscaPorDuplicidadeQuandoDespesaIndireta(
            final TipoDespesa tipoDespesa,
            final TipoCertificado tipoCertificado,
            final boolean isValido
    ) {
        final MutableLiveData<List<DespesaCertificado>> liveData = new MutableLiveData<>();
        localDataSource.buscaPorDuplicidadeQuandoDespesaIndireta(
                tipoDespesa, isValido, tipoCertificado,
                new RepositoryCallback<List<DespesaCertificado>>() {
                    @Override
                    public void sucesso(List<DespesaCertificado> resultado) {
                        liveData.setValue(resultado);
                    }

                    @Override
                    public void falha(String msg) {

                    }
                });
        return liveData;
    }

    public LiveData<List<DespesaCertificado>> buscaDuplicidadeQuandoDespesaDireta(
            final boolean isValido,
            final Long cavaloId,
            final TipoCertificado tipoCertificado
    ) {
        final MutableLiveData<List<DespesaCertificado>> liveData = new MutableLiveData<>();
        localDataSource.buscaPorDuplicidadeQuandoDespesaDireta(
                isValido, cavaloId, tipoCertificado, new RepositoryCallback<List<DespesaCertificado>>() {
                    @Override
                    public void sucesso(List<DespesaCertificado> resultado) {
                        liveData.setValue(resultado);
                    }

                    @Override
                    public void falha(String msg) {

                    }
                }
        );
        return liveData;
    }

}

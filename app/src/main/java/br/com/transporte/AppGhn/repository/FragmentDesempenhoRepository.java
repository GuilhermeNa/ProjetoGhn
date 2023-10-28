package br.com.transporte.AppGhn.repository;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.transporte.AppGhn.dataSource.local.LocalDesempenhoFragmentDataSource;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class FragmentDesempenhoRepository {
    private final LocalDesempenhoFragmentDataSource localSource;

    public FragmentDesempenhoRepository(Context context) {
        localSource = new LocalDesempenhoFragmentDataSource(context);
    }

    //----------------------------------------------------------------------------------------------

    private final MediatorLiveData<Resource<List<Cavalo>>> mediatorCavalo = new MediatorLiveData<>();
    private final MediatorLiveData<Resource<List<Motorista>>> mediatorMotorista = new MediatorLiveData<>();
    private final MutableLiveData<ResourceData> liveData = new MutableLiveData<>();

    public LiveData<ResourceData> buscaFretesPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaFretesPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaAbastecimentosPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaAbastecimentosPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaCustosPercursoPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaCustosPercursoPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaCustosManutencaoPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaManutencaoPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaDespesasCertificadoPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaCertificadosPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaDespesasImpostosPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaImpostosPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaDespesasAdmPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaDespesasAdmPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaDespesasSeguroFrotaPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaDespesaFrotaPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaDespesasSeguroVidaPorAnoEOuCavaloIdParaFragmentDesempenho(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaDespesaVidaPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }

    public LiveData<ResourceData> buscaLucroLiquidoPorAnoEOuCavaloId(
            final int ano,
            @Nullable final Long cavaloId
    ) {
        localSource.buscaLucroLiquidoPorAnoEOuCavaloId(ano, cavaloId,
                new RepositoryCallback<ResourceData>() {
                    @Override
                    public void sucesso(ResourceData resourceData) {
                        liveData.setValue(resourceData);
                    }

                    @Override
                    public void falha(String msg) {
                    }
                });
        return liveData;
    }


    public LiveData<Resource<List<Cavalo>>> buscaCavalos() {
        mediatorCavalo.addSource(localSource.buscaCavalos(),
                cavalos ->
                        mediatorCavalo.setValue(new Resource<>(cavalos, null)));
        return mediatorCavalo;
    }

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        mediatorMotorista.addSource(localSource.buscaMotoristas(),
                motoristas -> {
                    mediatorMotorista.setValue(new Resource<>(motoristas, null));
        });
        return mediatorMotorista;
    }


}

package br.com.transporte.appGhn.ui.fragment.desempenho.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.repository.FragmentDesempenhoRepository;
import br.com.transporte.appGhn.repository.Resource;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedBarChartData;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.mappers.model.MappedRecylerData;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.usecase.EncaminhaBuscaParaClasseResponsavelUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.usecase.EncaminhaMapeamentoParaClasseResponsavelPeloBarChartUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.domain.usecase.EncaminhaMapeamentoParaClasseResponsavelPeloRecyclerUseCase;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class DesempenhoFragmentViewModel extends ViewModel {
    private final FragmentDesempenhoRepository repository;
    public DataRequest dataRequest;
    public ResourceData resourceData;
    public List<Cavalo> dataSetCavalo;
    public List<Motorista> dataSetMotorista;
    public final MutableLiveData<ResourceData> _data = new MutableLiveData<>(null);
    public LiveData<ResourceData> data = _data;

    public DesempenhoFragmentViewModel(
            FragmentDesempenhoRepository repository
    ) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public void armazenaResource(ResourceData resourceData) {
        this.resourceData = resourceData;
        this.resourceData.setDataSetCavalo(dataSetCavalo);
        this.resourceData.setDataSetMotorista(dataSetMotorista);
    }

    public LiveData<ResourceData> buscaData() {
        final EncaminhaBuscaParaClasseResponsavelUseCase encaminhaBuscaUseCase =
                new EncaminhaBuscaParaClasseResponsavelUseCase(repository);
        return encaminhaBuscaUseCase.run(dataRequest);
    }

    public LiveData<Resource<List<Cavalo>>> buscaCavalos() {
        return repository.buscaCavalos();
    }

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        return repository.buscaMotoristas();
    }

    public MappedBarChartData barChartMapper() {
        final EncaminhaMapeamentoParaClasseResponsavelPeloBarChartUseCase barChartMappingUseCase =
                new EncaminhaMapeamentoParaClasseResponsavelPeloBarChartUseCase(resourceData);
        return barChartMappingUseCase.run(dataRequest);
    }

    public List<MappedRecylerData> recyclerMapper() {
        final EncaminhaMapeamentoParaClasseResponsavelPeloRecyclerUseCase recyclerMappingUseCase =
                new EncaminhaMapeamentoParaClasseResponsavelPeloRecyclerUseCase(resourceData);
        return recyclerMappingUseCase.run(dataRequest);
    }


}

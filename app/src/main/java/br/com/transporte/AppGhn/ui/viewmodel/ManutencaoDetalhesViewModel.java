package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.AppGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ManutencaoRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.repository.Resource;
import br.com.transporte.AppGhn.util.DataUtil;

public class ManutencaoDetalhesViewModel extends ViewModel {
    private final MotoristaRepository motoristaRepository;
    private final CavaloRepository cavaloRepository;
    private final ManutencaoRepository manutencaoRepository;

    public List<CustosDeManutencao> dataSet;
    public Motorista motorista;
    public long cavaloId;
    public Cavalo cavalo;

    public LocalDate dataInicial =
            DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    public LocalDate dataFinal =
            DataUtil.capturaDataDeHojeParaConfiguracaoInicial();

    public ManutencaoDetalhesViewModel(
            final MotoristaRepository motoristaRepository,
            final CavaloRepository cavaloRepository,
            final ManutencaoRepository manutencaoRepository
    ) {
        this.motoristaRepository = motoristaRepository;
        this.cavaloRepository = cavaloRepository;
        this.manutencaoRepository = manutencaoRepository;
    }

    //----------------------------------------------------------------------------------------------

    private final MutableLiveData<List<CustosDeManutencao>> _dataSetFiltrada = new MutableLiveData<>(null);
    public LiveData<List<CustosDeManutencao>> dataSetFiltrada = _dataSetFiltrada;

    //--------------------------------------------------------------------------------------------------

    public LiveData<Cavalo> localizaCavalo() {
        if (cavaloId > 0)
            return cavaloRepository.localizaCavaloPeloId(cavaloId);
        else
            return new MutableLiveData<>(null);
    }

    public LiveData<Motorista> localizaMotorista() {
        final Long motoristaId = cavalo.getRefMotoristaId();
        if (motoristaId != null)
            return motoristaRepository.localizaMotorista(motoristaId);
        else
            return new MutableLiveData<>(null);
    }

    public LiveData<Resource<List<CustosDeManutencao>>> buscaManutencaoPorCavaloId() {
        if (this.cavaloId > 0) {
            return manutencaoRepository.buscaManutencaoPorCavaloId(this.cavaloId);
        } else {
            return new MutableLiveData<>(new Resource<>(null, "Cavalo não encontrado"));
        }
    }

    public void aplicaFiltroDeData() {
        if (dataSet != null
                && !dataSet.isEmpty()
        ) {
            final List<CustosDeManutencao> dataSetFiltrada =
                    FiltraCustosManutencao.listaPorData(dataSet, dataInicial, dataFinal);
            _dataSetFiltrada.setValue(dataSetFiltrada);
        }
    }

    public void atualizaData(
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }


}

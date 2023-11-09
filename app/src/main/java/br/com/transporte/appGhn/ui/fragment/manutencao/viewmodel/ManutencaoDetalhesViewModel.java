package br.com.transporte.appGhn.ui.fragment.manutencao.viewmodel;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraCustosManutencao;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.custos.CustosDeManutencao;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.ManutencaoRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.Resource;
import br.com.transporte.appGhn.util.DataUtil;

public class ManutencaoDetalhesViewModel extends ViewModel {
    private final MotoristaRepository motoristaRepository;
    private final CavaloRepository cavaloRepository;
    private final ManutencaoRepository manutencaoRepository;

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

    public LocalDate dataInicial =
            DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    public LocalDate dataFinal =
            DataUtil.capturaDataDeHojeParaConfiguracaoInicial();

    public List<CustosDeManutencao> dataSetTodasManutencoesDoCavalo;
    public long cavaloId;
    public Cavalo cavalo;
    public Motorista motorista;


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

    public void buscaPorData() {
        if (dataSetTodasManutencoesDoCavalo != null) {
            final List<CustosDeManutencao> dataSetFiltrada = getCustoDeManutencaoNoRangeIndicadoPeloUsuario();
            _dataSetFiltrada.setValue(dataSetFiltrada);
        }
    }

    public List<CustosDeManutencao> getCustoDeManutencaoNoRangeIndicadoPeloUsuario() {
        return FiltraCustosManutencao.listaPorData(dataSetTodasManutencoesDoCavalo, dataInicial, dataFinal);
    }

    public void atualizaData(@NonNull final Pair<Long, Long> selection) {
        final LocalDate dataInicial = DataUtil.milliToLocalDate(selection.first);
        final LocalDate dataFinal = DataUtil.milliToLocalDate(selection.second);

        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public Pair<Long, Long> getSelection() {
        return new Pair<>(
                DataUtil.localDateToMilli(dataInicial),
                DataUtil.localDateToMilli(dataFinal)
        );
    }

    public boolean verificaSeEstaNoRangeDate(@NonNull final CustosDeManutencao manutencao) {
        return DataUtil.verificaSeEstaNoRange(manutencao.getData(), dataInicial, dataFinal);
    }

}

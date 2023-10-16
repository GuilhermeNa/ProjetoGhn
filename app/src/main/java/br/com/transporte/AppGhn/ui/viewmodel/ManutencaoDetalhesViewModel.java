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

    public ManutencaoDetalhesViewModel(MotoristaRepository motoristaRepository, CavaloRepository cavaloRepository, ManutencaoRepository manutencaoRepository) {
        this.motoristaRepository = motoristaRepository;
        this.cavaloRepository = cavaloRepository;
        this.manutencaoRepository = manutencaoRepository;
    }

    //----------------------------------------------------------------------------------------------
    private List<CustosDeManutencao> dataSet_base;
    private LocalDate dataInicial = DataUtil.capturaPrimeiroDiaDoMesParaConfiguracaoInicial();
    private LocalDate dataFinal = DataUtil.capturaDataDeHojeParaConfiguracaoInicial();

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public List<CustosDeManutencao> getDataSet_base() {
        return dataSet_base;
    }

    public void setDataSet_base(List<CustosDeManutencao> dataSet_base) {
        this.dataSet_base = dataSet_base;
    }

    //--------------------------------------------------------------------------------------------------

    public LiveData<Cavalo> localizaCavalo(final long cavaloId) {
        if (cavaloId > 0)
            return cavaloRepository.localizaCavaloPeloId(cavaloId);
        else
            return new MutableLiveData<>(null);
    }

    public LiveData<Motorista> localizaMotorista(final long motoristaId) {
        if (motoristaId > 0)
            return motoristaRepository.localizaMotorista(motoristaId);
        else
            return new MutableLiveData<>(null);
    }

    public LiveData<Resource<List<CustosDeManutencao>>> buscaManutencaoPorCavaloId(final long cavaloId) {
        if (cavaloId > 0) {
            return manutencaoRepository.buscaManutencaoPorCavaloId(cavaloId);
        } else {
            return new MutableLiveData<>(new Resource<>(null, "Cavalo não encontrado"));
        }
    }

    public List<CustosDeManutencao> filtraPorData() {
        return FiltraCustosManutencao.listaPorData(getDataSet_base(), getDataInicial(), getDataFinal());
    }
}

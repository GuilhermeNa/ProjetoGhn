package br.com.transporte.appGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraDespesasImposto;
import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.ImpostoRepository;
import br.com.transporte.appGhn.repository.Resource;
import br.com.transporte.appGhn.util.DataUtil;

public class ImpostosViewModel extends ViewModel {
    private final ImpostoRepository repository;
    private final CavaloRepository cavaloRepository;

    public ImpostosViewModel(ImpostoRepository repository, CavaloRepository cavaloRepository) {
        this.repository = repository;
        this.cavaloRepository = cavaloRepository;
    }

    //----------------------------------------------------------------------------------------------

    private List<DespesasDeImposto> dataSet_base;
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

    public List<DespesasDeImposto> getDataSet_base() {
        return dataSet_base;
    }

    public void setDataSet_base(List<DespesasDeImposto> dataSet_base) {
        this.dataSet_base = dataSet_base;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Resource<List<DespesasDeImposto>>> buscaImpostos() {
        return repository.buscaImpostos();
    }

    public LiveData<Resource<List<Cavalo>>> buscaCavalos(){
        return cavaloRepository.buscaCavalos();
    }

    public List<DespesasDeImposto> filtraPorData(){
        return FiltraDespesasImposto.listaFiltradaPorData(dataSet_base, dataInicial, dataFinal);
    }



}

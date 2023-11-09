package br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraCustosAbastecimento;
import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;

public class AreaMotoristaActViewModel_abastecimentoDataHelper {
    private final List<CustosDeAbastecimento> DATASET;
    private List<CustosDeAbastecimento> fragmentsSharedData;

    public AreaMotoristaActViewModel_abastecimentoDataHelper(List<CustosDeAbastecimento> dataset, LocalDate dataInicial, LocalDate dataFinal) {
        DATASET = dataset;
        atualizaSharedData(dataInicial, dataFinal);
    }

    //----------------------------------------------------------------------------------------------

    public List<CustosDeAbastecimento> getFragmentsSharedData() {
        return new ArrayList<>(fragmentsSharedData);
    }

    public List<CustosDeAbastecimento> getDataSet() {
        return new ArrayList<>(DATASET);
    }

    public void atualizaSharedData(LocalDate sharedDataInicial, LocalDate sharedDataFinal) {
        List<CustosDeAbastecimento> dataSet = new ArrayList<>(DATASET);
        Comparator<CustosDeAbastecimento> comparing = Comparator.comparing(CustosDeAbastecimento::getMarcacaoKm);
        dataSet.sort(comparing);
        Collections.reverse(dataSet);
        this.fragmentsSharedData = FiltraCustosAbastecimento.listaPorData(dataSet, sharedDataInicial, sharedDataFinal);
    }
}

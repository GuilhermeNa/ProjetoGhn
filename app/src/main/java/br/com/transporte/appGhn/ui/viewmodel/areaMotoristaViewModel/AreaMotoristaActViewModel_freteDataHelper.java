package br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraFrete;
import br.com.transporte.appGhn.model.Frete;

public class AreaMotoristaActViewModel_freteDataHelper {

    private final List<Frete> DATASET;
    private List<Frete> fragmentsSharedData;

    public AreaMotoristaActViewModel_freteDataHelper(List<Frete> dataset, LocalDate dataInicial, LocalDate dataFinal) {
        DATASET = dataset;
        atualizaSharedData(dataInicial, dataFinal);
    }

    //----------------------------------------------------------------------------------------------

    public List<Frete> getFragmentsSharedData() {
        return fragmentsSharedData;
    }

    public void atualizaSharedData(LocalDate sharedDataInicial, LocalDate sharedDataFinal) {
        List<Frete> dataSet = new ArrayList<>(DATASET);
        Comparator<Frete> comparing = Comparator.comparing(Frete::getData);
        dataSet.sort(comparing);
        Collections.reverse(dataSet);
        this.fragmentsSharedData = FiltraFrete.listaPorData(dataSet, sharedDataInicial, sharedDataFinal);
    }
}

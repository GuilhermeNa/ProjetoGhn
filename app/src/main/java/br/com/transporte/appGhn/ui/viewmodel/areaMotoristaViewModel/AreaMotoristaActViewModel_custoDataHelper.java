package br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.transporte.appGhn.filtros.FiltraCustosPercurso;
import br.com.transporte.appGhn.model.custos.CustosDePercurso;

public class AreaMotoristaActViewModel_custoDataHelper {
    private final List<CustosDePercurso> DATASET;
    private List<CustosDePercurso> fragmentsSharedData;

    public AreaMotoristaActViewModel_custoDataHelper(List<CustosDePercurso> dataset, LocalDate dataInicial, LocalDate dataFinal) {
        DATASET = dataset;
        atualizaSharedData(dataInicial, dataFinal);
    }

    //----------------------------------------------------------------------------------------------

    public List<CustosDePercurso> getFragmentsSharedData() {
        return fragmentsSharedData;
    }

    public void atualizaSharedData(LocalDate sharedDataInicial, LocalDate sharedDataFinal) {
        List<CustosDePercurso> dataSet = new ArrayList<>(DATASET);
        Comparator<CustosDePercurso> comparing = Comparator.comparing(CustosDePercurso::getData);
        dataSet.sort(comparing);
        Collections.reverse(dataSet);
        this.fragmentsSharedData = FiltraCustosPercurso.listaPorData(dataSet, sharedDataInicial, sharedDataFinal);
    }
}

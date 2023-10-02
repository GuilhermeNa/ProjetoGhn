package br.com.transporte.AppGhn.ui.viewmodel.areaMotoristaViewModel;

import java.util.List;

import br.com.transporte.AppGhn.model.Adiantamento;

public class AreaMotoristaActViewModel_adiantamentoDataHelper {
    private final List<Adiantamento> DATASET;
    private List<Adiantamento> fragmentsSharedData;

    public AreaMotoristaActViewModel_adiantamentoDataHelper(List<Adiantamento> dataset) {
        DATASET = dataset;
        fragmentsSharedData = DATASET;
    }

    //----------------------------------------------------------------------------------------------

    public List<Adiantamento> getFragmentsSharedData() {
        return fragmentsSharedData;
    }

}

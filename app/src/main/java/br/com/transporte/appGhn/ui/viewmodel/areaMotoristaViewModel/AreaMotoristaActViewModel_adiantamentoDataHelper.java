package br.com.transporte.appGhn.ui.viewmodel.areaMotoristaViewModel;

import java.util.List;

import br.com.transporte.appGhn.model.Adiantamento;

public class AreaMotoristaActViewModel_adiantamentoDataHelper {
    private final List<Adiantamento> fragmentsSharedData;

    public AreaMotoristaActViewModel_adiantamentoDataHelper(List<Adiantamento> dataset) {
        fragmentsSharedData = dataset;
    }

    //----------------------------------------------------------------------------------------------

    public List<Adiantamento> getFragmentsSharedData() {
        return fragmentsSharedData;
    }

}

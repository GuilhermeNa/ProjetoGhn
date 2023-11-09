package br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.SeguroFrotaRepository;
import br.com.transporte.appGhn.ui.fragment.seguros.seguroFrota.domain.model.DespesaSeguroFrotaObject;

public class SeguroFrotaViewModel extends ViewModel {
    private final CavaloRepository cavaloRepository;
    private final SeguroFrotaRepository frotaRepository;

    public SeguroFrotaViewModel(CavaloRepository cavaloRepository, SeguroFrotaRepository frotaRepository) {
        this.cavaloRepository = cavaloRepository;
        this.frotaRepository = frotaRepository;
    }

    //----------------------------------------------------------------------------------------------

    private List<DespesaSeguroFrotaObject> dataSet = new ArrayList<>();


    public List<DespesaSeguroFrotaObject> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    public void setDataSet(List<DespesaSeguroFrotaObject> dataSet) {
        this.dataSet = dataSet;
    }
}

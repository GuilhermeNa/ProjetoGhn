package br.com.transporte.appGhn.ui.fragment.despesasAdm.indireta.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.model.despesas.DespesaAdm;

public class DespesaAdmIndiretaViewModel extends ViewModel {

    private List<DespesaAdm> dataSet = new ArrayList<>();

    public List<DespesaAdm> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    public void setDataSet(List<DespesaAdm> dataSet) {
        this.dataSet = dataSet;
    }
}

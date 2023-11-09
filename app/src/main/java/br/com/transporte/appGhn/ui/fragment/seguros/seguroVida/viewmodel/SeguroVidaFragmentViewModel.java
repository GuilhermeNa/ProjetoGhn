package br.com.transporte.appGhn.ui.fragment.seguros.seguroVida.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.model.despesas.DespesaComSeguroDeVida;

public class SeguroVidaFragmentViewModel extends ViewModel {
    private List<DespesaComSeguroDeVida> dataSet = new ArrayList<>();

    public List<DespesaComSeguroDeVida> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<DespesaComSeguroDeVida> dataSet) {
        this.dataSet = dataSet;
    }
}

package br.com.transporte.AppGhn.ui.fragment.despesasAdm.indireta.viewmodel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.AppGhn.model.despesas.DespesaAdm;

public class DespesaAdmIndiretaViewModel extends ViewModel {

    private List<DespesaAdm> dataSet = new ArrayList<>();

    public List<DespesaAdm> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    public void setDataSet(List<DespesaAdm> dataSet) {
        this.dataSet = dataSet;
    }
}

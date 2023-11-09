package br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.ui.fragment.despesasAdm.direta.domain.model.DespesaAdmDiretaObject;

public class DespesaAdmDiretaViewModel extends ViewModel {

    private List<DespesaAdmDiretaObject> dataSet = new ArrayList<>();
    private boolean aguardandoAtualizacao = false;


    public List<DespesaAdmDiretaObject> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    public void setDataSet(List<DespesaAdmDiretaObject> dataSet) {
        this.dataSet = dataSet;
    }

    public boolean isAguardandoAtualizacao() {
        return aguardandoAtualizacao;
    }

    public void setAguardandoAtualizacao(boolean aguardandoAtualizacao) {
        this.aguardandoAtualizacao = aguardandoAtualizacao;
    }
}

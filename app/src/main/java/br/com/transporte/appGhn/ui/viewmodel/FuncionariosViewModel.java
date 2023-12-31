package br.com.transporte.appGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.Resource;

public class FuncionariosViewModel extends ViewModel {
    private final MotoristaRepository repository;
    private List<Motorista> dataSet = new ArrayList<>();

    public FuncionariosViewModel(MotoristaRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        return repository.buscaMotoristas();
    }

    public List<Motorista> getDataSet() {
        return new ArrayList<>(dataSet);
    }

    public void setDataSet(List<Motorista> dataSet) {
        this.dataSet = dataSet;
    }
}

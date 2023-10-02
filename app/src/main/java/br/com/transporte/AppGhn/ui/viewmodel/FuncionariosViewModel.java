package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.repository.Resource;

public class FuncionariosViewModel extends ViewModel {
    private final MotoristaRepository repository;

    public FuncionariosViewModel(MotoristaRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        return repository.buscaMotoristas();
    }

}

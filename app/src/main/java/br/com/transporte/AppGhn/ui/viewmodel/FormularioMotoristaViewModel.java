package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.repository.MotoristaRepository;

public class FormularioMotoristaViewModel extends ViewModel {
    private final MotoristaRepository repository;

    public FormularioMotoristaViewModel(@NonNull MotoristaRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public Motorista motoristaArmazenado;

    public LiveData<Motorista> localizaMotorista(final Long id) {
        return repository.localizaMotorista(id);
    }

    public LiveData<Long> salvaMotorista(@NonNull final Motorista motorista) {
        if (motorista.getId() == null) {
            return repository.adicionaMotorista(motorista);
        } else {
            return repository.editaMotorista(motorista);
        }
    }

    public LiveData<String> deleta() {
        if(motoristaArmazenado != null){
            return repository.deletaMotorista(motoristaArmazenado);
        } else {
            return new MutableLiveData<>("Motorista não encontrado");
        }
    }

}

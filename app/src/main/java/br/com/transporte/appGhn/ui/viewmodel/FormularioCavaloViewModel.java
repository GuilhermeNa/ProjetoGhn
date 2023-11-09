package br.com.transporte.appGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.repository.CavaloRepository;

public class FormularioCavaloViewModel extends ViewModel {
    private final CavaloRepository repository;

    public FormularioCavaloViewModel(CavaloRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public Cavalo cavaloArmazenado;

    public LiveData<Cavalo> localizaCavalo(final long cavaloId) {
        return repository.localizaCavaloPeloId(cavaloId);
    }

    public LiveData<Long> salvaCavalo(@NonNull final Cavalo cavalo) {
        if (cavalo.getId() == null) {
            return repository.adicionaCavalo(cavalo);
        } else {
            return repository.editaCavalo(cavalo);
        }
    }

    public LiveData<String> deleta() {
        if(cavaloArmazenado != null){
            return repository.deletaCavalo(cavaloArmazenado);
        } else {
            return new MutableLiveData<>("Cavalo não encontrado");
        }
    }

}

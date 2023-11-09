package br.com.transporte.appGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.repository.CavaloRepository;

public class FormularioBaseViewModel extends ViewModel {
    private final CavaloRepository repository;

    public FormularioBaseViewModel(final CavaloRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Cavalo> localizaCavalo(final long cavaloId){
        return repository.localizaCavaloPeloId(cavaloId);
    }

}

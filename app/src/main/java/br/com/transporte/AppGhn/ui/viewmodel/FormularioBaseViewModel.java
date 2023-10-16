package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.repository.CavaloRepository;

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

package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.viewmodel.MainActViewModel;

public class MainActViewModelFactory implements ViewModelProvider.Factory{
    private final CavaloRepository cavaloRepository;
    private final MotoristaRepository motoristaRepository;

    public MainActViewModelFactory(CavaloRepository repository, MotoristaRepository motoristaRepository) {
        this.cavaloRepository = repository;
        this.motoristaRepository = motoristaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActViewModel(cavaloRepository, motoristaRepository);
    }
}

package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.ui.viewmodel.FormularioBaseViewModel;

public class FormularioBaseViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository repository;

    public FormularioBaseViewModelFactory(CavaloRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioBaseViewModel(repository);
    }
}

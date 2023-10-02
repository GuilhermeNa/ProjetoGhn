package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioBaseViewModel;

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

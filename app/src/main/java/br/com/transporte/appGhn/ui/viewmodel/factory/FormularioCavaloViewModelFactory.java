package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.ui.viewmodel.FormularioCavaloViewModel;

public class FormularioCavaloViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository repository;

    public FormularioCavaloViewModelFactory(CavaloRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioCavaloViewModel(repository);
    }
}

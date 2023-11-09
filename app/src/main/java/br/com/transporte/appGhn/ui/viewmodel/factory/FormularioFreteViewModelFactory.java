package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.FreteRepository;
import br.com.transporte.appGhn.ui.viewmodel.FormularioFreteViewModel;

public class FormularioFreteViewModelFactory implements ViewModelProvider.Factory {
    private final FreteRepository repository;

    public FormularioFreteViewModelFactory(FreteRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioFreteViewModel(repository);
    }

}

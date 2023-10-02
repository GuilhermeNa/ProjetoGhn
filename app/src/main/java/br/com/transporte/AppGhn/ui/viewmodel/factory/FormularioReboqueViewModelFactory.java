package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.ReboqueRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioReboqueViewModel;

public class FormularioReboqueViewModelFactory implements ViewModelProvider.Factory {
    private final ReboqueRepository repository;

    public FormularioReboqueViewModelFactory(ReboqueRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioReboqueViewModel(repository);
    }

}

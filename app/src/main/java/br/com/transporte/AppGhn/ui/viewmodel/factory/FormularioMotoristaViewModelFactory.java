package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioMotoristaViewModel;

public class FormularioMotoristaViewModelFactory implements ViewModelProvider.Factory {
    private final MotoristaRepository repository;

    public FormularioMotoristaViewModelFactory(MotoristaRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioMotoristaViewModel(repository);
    }

}

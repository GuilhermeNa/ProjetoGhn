package br.com.transporte.appGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.ManutencaoRepository;

public class FormularioManutencaoViewModelFactory implements ViewModelProvider.Factory {
    private final ManutencaoRepository repository;

    public FormularioManutencaoViewModelFactory(ManutencaoRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioManutencaoViewModel(repository);
    }
}

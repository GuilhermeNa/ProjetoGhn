package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ImpostoRepository;
import br.com.transporte.AppGhn.ui.viewmodel.ImpostosViewModel;

public class ImpostosViewModelFactory implements ViewModelProvider.Factory {
    private final ImpostoRepository repository;
    private final CavaloRepository cavaloRepository;

    public ImpostosViewModelFactory(ImpostoRepository repository, CavaloRepository cavaloRepository) {
        this.repository = repository;
        this.cavaloRepository = cavaloRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ImpostosViewModel(repository, cavaloRepository);
    }

}

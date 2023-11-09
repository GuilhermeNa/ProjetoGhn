package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.ImpostoRepository;
import br.com.transporte.appGhn.ui.viewmodel.FormularioImpostoViewModel;

public class FormularioImpostoViewModelFactory implements ViewModelProvider.Factory {
    private final ImpostoRepository impostoRepository;
    private final CavaloRepository cavaloRepository;
    public FormularioImpostoViewModelFactory(ImpostoRepository repository, CavaloRepository cavaloRepository) {
        this.impostoRepository = repository;
        this.cavaloRepository = cavaloRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioImpostoViewModel(impostoRepository, cavaloRepository);
    }
}

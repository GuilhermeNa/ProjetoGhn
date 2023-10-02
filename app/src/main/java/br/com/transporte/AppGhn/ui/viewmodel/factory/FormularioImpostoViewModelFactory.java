package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ImpostoRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioImpostoViewModel;

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

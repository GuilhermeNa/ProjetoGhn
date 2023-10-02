package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioCustoPercursoViewModel;

public class FormularioCustoPercursoViewModelFactory implements ViewModelProvider.Factory {
    private final CustoDePercursoRepository repository;

    public FormularioCustoPercursoViewModelFactory(CustoDePercursoRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioCustoPercursoViewModel(repository);
    }
}

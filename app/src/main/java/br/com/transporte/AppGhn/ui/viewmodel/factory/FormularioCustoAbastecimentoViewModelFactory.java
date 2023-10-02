package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioCustoAbastecimentoViewModel;

public class FormularioCustoAbastecimentoViewModelFactory implements ViewModelProvider.Factory {
    private final CustoDeAbastecimentoRepository repository;

    public FormularioCustoAbastecimentoViewModelFactory(CustoDeAbastecimentoRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioCustoAbastecimentoViewModel(repository);
    }

}

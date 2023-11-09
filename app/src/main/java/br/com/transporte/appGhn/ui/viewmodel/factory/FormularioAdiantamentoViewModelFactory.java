package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.AdiantamentoRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.viewmodel.FormularioAdiantamentoViewModel;

public class FormularioAdiantamentoViewModelFactory implements ViewModelProvider.Factory {
    private final MotoristaRepository motoristaRepository;
    private final AdiantamentoRepository adiantamentoRepository;

    public FormularioAdiantamentoViewModelFactory(MotoristaRepository motoristaRepository, AdiantamentoRepository adiantamentoRepository) {
        this.motoristaRepository = motoristaRepository;
        this.adiantamentoRepository = adiantamentoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioAdiantamentoViewModel(motoristaRepository, adiantamentoRepository);
    }
}

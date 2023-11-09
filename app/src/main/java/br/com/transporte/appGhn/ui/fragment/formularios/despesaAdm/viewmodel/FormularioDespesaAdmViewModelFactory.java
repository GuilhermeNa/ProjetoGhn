package br.com.transporte.appGhn.ui.fragment.formularios.despesaAdm.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.DespesaAdmRepository;

public class FormularioDespesaAdmViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository cavaloRepository;
    private final DespesaAdmRepository despesaAdmRepository;

    public FormularioDespesaAdmViewModelFactory(CavaloRepository cavaloRepository, DespesaAdmRepository despesaAdmRepository) {
        this.cavaloRepository = cavaloRepository;
        this.despesaAdmRepository = despesaAdmRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioDespesaAdmViewModel(cavaloRepository, despesaAdmRepository);
    }
}

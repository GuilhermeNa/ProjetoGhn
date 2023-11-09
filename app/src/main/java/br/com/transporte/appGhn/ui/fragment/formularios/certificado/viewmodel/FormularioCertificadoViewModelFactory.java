package br.com.transporte.appGhn.ui.fragment.formularios.certificado.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CertificadoRepository;

public class FormularioCertificadoViewModelFactory implements ViewModelProvider.Factory {
    private final CertificadoRepository repository;
    private final CavaloRepository repositoryCavalo;

    public FormularioCertificadoViewModelFactory(CertificadoRepository repository, CavaloRepository repositoryCavalo) {
        this.repository = repository;
        this.repositoryCavalo = repositoryCavalo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioCertificadoViewModel(repository, repositoryCavalo);
    }
}

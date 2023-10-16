package br.com.transporte.AppGhn.ui.fragment.formularios.certificado.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.CertificadoRepository;
import br.com.transporte.AppGhn.ui.fragment.formularios.certificado.viewmodel.FormularioCertificadoViewModel;

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

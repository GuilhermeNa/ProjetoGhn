package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CertificadoRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.ui.viewmodel.CertificadoActViewModel;

public class CertificadosActViewModelFactory implements ViewModelProvider.Factory {
    private final CertificadoRepository certificadoRepository;
    private final CavaloRepository cavaloRepository;
    private final MotoristaRepository motoristaRepository;

    public CertificadosActViewModelFactory(
            CertificadoRepository certificadoRepository,
            CavaloRepository cavaloRepository,
            MotoristaRepository motoristaRepository

    ) {
        this.certificadoRepository = certificadoRepository;
        this.cavaloRepository = cavaloRepository;
        this.motoristaRepository = motoristaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CertificadoActViewModel(certificadoRepository, cavaloRepository, motoristaRepository);
    }
}

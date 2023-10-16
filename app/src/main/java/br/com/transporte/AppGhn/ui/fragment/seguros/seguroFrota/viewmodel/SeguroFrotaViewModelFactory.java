package br.com.transporte.AppGhn.ui.fragment.seguros.seguroFrota.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.SeguroFrotaRepository;

public class SeguroFrotaViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository cavaloRepository;
    private final SeguroFrotaRepository frotaRepository;

    public SeguroFrotaViewModelFactory(CavaloRepository cavaloRepository, SeguroFrotaRepository frotaRepository) {
        this.cavaloRepository = cavaloRepository;
        this.frotaRepository = frotaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SeguroFrotaViewModel(cavaloRepository, frotaRepository);
    }
}

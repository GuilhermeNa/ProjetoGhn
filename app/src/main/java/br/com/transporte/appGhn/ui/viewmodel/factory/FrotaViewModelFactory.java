package br.com.transporte.appGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.ReboqueRepository;
import br.com.transporte.appGhn.ui.viewmodel.FrotaViewModel;

public class FrotaViewModelFactory implements ViewModelProvider.Factory {
    private final ReboqueRepository reboqueRepository;
    private final MotoristaRepository motoristaRepository;
    private final CavaloRepository cavaloRepository;

    public FrotaViewModelFactory(ReboqueRepository reboqueRepository, MotoristaRepository motoristaRepository, CavaloRepository cavaloRepository) {
       this.reboqueRepository = reboqueRepository;
       this.motoristaRepository = motoristaRepository;
        this.cavaloRepository = cavaloRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FrotaViewModel(cavaloRepository, motoristaRepository, reboqueRepository);
    }
}

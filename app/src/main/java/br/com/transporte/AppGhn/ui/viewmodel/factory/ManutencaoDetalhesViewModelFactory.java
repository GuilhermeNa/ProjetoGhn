package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ManutencaoRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.viewmodel.ManutencaoDetalhesViewModel;

public class ManutencaoDetalhesViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository cavaloRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final MotoristaRepository motoristaRepository;

    public ManutencaoDetalhesViewModelFactory(
            MotoristaRepository motoristaRepository, CavaloRepository cavaloRepository, ManutencaoRepository manutencaoRepository
    ) {
        this.motoristaRepository = motoristaRepository;
        this.cavaloRepository = cavaloRepository;
        this.manutencaoRepository = manutencaoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ManutencaoDetalhesViewModel(motoristaRepository, cavaloRepository, manutencaoRepository);
    }

}

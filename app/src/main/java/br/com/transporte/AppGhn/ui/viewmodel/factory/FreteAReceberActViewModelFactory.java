package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.RecebimentoDeFreteRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FreteAReceberActViewModel;

public class FreteAReceberActViewModelFactory implements ViewModelProvider.Factory {
    private final FreteRepository freteRepository;
    private final CavaloRepository cavaloRepository;
    private final RecebimentoDeFreteRepository recebimentoRepository;

    public FreteAReceberActViewModelFactory(
            FreteRepository freteRepository,
            CavaloRepository cavaloRepository,
            RecebimentoDeFreteRepository recebimentoRepository
    ) {
        this.freteRepository = freteRepository;
        this.cavaloRepository = cavaloRepository;
        this.recebimentoRepository = recebimentoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FreteAReceberActViewModel(freteRepository, cavaloRepository, recebimentoRepository);
    }

}

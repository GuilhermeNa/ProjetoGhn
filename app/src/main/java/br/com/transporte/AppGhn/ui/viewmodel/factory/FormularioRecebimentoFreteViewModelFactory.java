package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.RecebimentoDeFreteRepository;
import br.com.transporte.AppGhn.ui.viewmodel.FormularioRecebimentoFreteViewModel;

public class FormularioRecebimentoFreteViewModelFactory implements ViewModelProvider.Factory {
    final FreteRepository freteRepository;
    final RecebimentoDeFreteRepository recebimentoRepository;

    public FormularioRecebimentoFreteViewModelFactory(FreteRepository freteRepository, RecebimentoDeFreteRepository recebimentoRepository) {
        this.freteRepository = freteRepository;
        this.recebimentoRepository = recebimentoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioRecebimentoFreteViewModel(freteRepository, recebimentoRepository);
    }

}

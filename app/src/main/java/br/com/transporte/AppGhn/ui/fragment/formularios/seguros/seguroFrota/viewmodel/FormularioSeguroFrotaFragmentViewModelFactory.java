package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroFrota.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ParcelaSeguroFrotaRepository;
import br.com.transporte.AppGhn.repository.SeguroFrotaRepository;

public class FormularioSeguroFrotaFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository cavaloRepository;
    private final SeguroFrotaRepository seguroRepository;
    private final ParcelaSeguroFrotaRepository parcelaRepository;

    public FormularioSeguroFrotaFragmentViewModelFactory(
            CavaloRepository cavaloRepository,
            SeguroFrotaRepository seguroRepository,
            ParcelaSeguroFrotaRepository parcelaRepository
    ) {
        this.cavaloRepository = cavaloRepository;
        this.seguroRepository = seguroRepository;
        this.parcelaRepository = parcelaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioSeguroFrotaViewModel(cavaloRepository, seguroRepository, parcelaRepository);
    }


}

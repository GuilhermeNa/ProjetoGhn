package br.com.transporte.AppGhn.ui.fragment.formularios.seguros.seguroVida.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.ParcelaSeguroVidaRepository;
import br.com.transporte.AppGhn.repository.SeguroVidaRepository;

public class FormularioSeguroVidaViewModelFactory implements ViewModelProvider.Factory {
    private final SeguroVidaRepository seguroRepository;
    private final ParcelaSeguroVidaRepository parcelaRepository;

    public FormularioSeguroVidaViewModelFactory(SeguroVidaRepository seguroRepository, ParcelaSeguroVidaRepository parcelaRepository) {
        this.seguroRepository = seguroRepository;
        this.parcelaRepository = parcelaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FormularioSeguroVidaViewModel(seguroRepository, parcelaRepository);
    }
}

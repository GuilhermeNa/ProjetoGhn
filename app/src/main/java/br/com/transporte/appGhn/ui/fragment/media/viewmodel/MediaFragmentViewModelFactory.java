package br.com.transporte.appGhn.ui.fragment.media.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.appGhn.repository.CustoDePercursoRepository;
import br.com.transporte.appGhn.repository.FreteRepository;

public class MediaFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository cavaloRepository;
    private final CustoDeAbastecimentoRepository abastecimentoRepository;
    private final FreteRepository freteRepository;
    private final CustoDePercursoRepository custosRepository;

    public MediaFragmentViewModelFactory(
            CavaloRepository repository,
            CustoDeAbastecimentoRepository abastecimentoRepository,
            FreteRepository freteRepository,
            CustoDePercursoRepository custosRepository
            ) {
        this.cavaloRepository = repository;
        this.abastecimentoRepository = abastecimentoRepository;
        this.freteRepository = freteRepository;
        this.custosRepository = custosRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MediaFragmentViewModel(cavaloRepository, abastecimentoRepository, freteRepository, custosRepository);
    }
}

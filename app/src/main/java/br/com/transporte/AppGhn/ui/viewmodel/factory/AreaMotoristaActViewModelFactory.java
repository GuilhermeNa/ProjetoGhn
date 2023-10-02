package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.AdiantamentoRepository;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.viewmodel.areaMotoristaViewModel.AreaMotoristaActViewModel;

public class AreaMotoristaActViewModelFactory implements ViewModelProvider.Factory {
    private final CustoDeAbastecimentoRepository abastecimentoRepository;
    private final CustoDePercursoRepository custoRepository;
    private final MotoristaRepository motoristaRepository;
    private final CavaloRepository cavaloRepository;
    private final FreteRepository freteRepository;
    private final AdiantamentoRepository adiantamentoRepository;

    public AreaMotoristaActViewModelFactory(
            FreteRepository freteRepository,
            CustoDeAbastecimentoRepository abastecimentoRepository,
            CustoDePercursoRepository custoRepository,
            CavaloRepository cavaloRepository,
            MotoristaRepository motoristaRepository,
            AdiantamentoRepository adiantamentoRepository

    ) {
        this.freteRepository = freteRepository;
        this.abastecimentoRepository = abastecimentoRepository;
        this.custoRepository = custoRepository;
        this.cavaloRepository = cavaloRepository;
        this.motoristaRepository = motoristaRepository;
        this.adiantamentoRepository = adiantamentoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AreaMotoristaActViewModel(freteRepository, abastecimentoRepository, custoRepository, cavaloRepository, motoristaRepository, adiantamentoRepository);
    }
}

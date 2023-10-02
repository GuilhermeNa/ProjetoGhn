package br.com.transporte.AppGhn.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.transporte.AppGhn.repository.AdiantamentoRepository;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;
import br.com.transporte.AppGhn.repository.CustoDeSalarioRepository;
import br.com.transporte.AppGhn.repository.FreteRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.ui.viewmodel.ComissaoActViewModel;

public class ComissaoActViewModelFactory implements ViewModelProvider.Factory {
    private final CavaloRepository cavaloRepository;
    private final MotoristaRepository motoristaRepository;
    private final FreteRepository freteRepository;
    private final AdiantamentoRepository adiantamentoRepository;
    private final CustoDePercursoRepository custoDePercursoRepository;
    private final CustoDeSalarioRepository salarioRepository;

    public ComissaoActViewModelFactory(
            CavaloRepository cavaloRepository,
            MotoristaRepository motoristaRepository,
            FreteRepository freteRepository,
            AdiantamentoRepository adiantamentoRepository,
            CustoDePercursoRepository custoDePercursoRepository,
            CustoDeSalarioRepository salarioRepository
    ) {
        this.cavaloRepository = cavaloRepository;
        this.motoristaRepository = motoristaRepository;
        this.freteRepository = freteRepository;
        this.adiantamentoRepository = adiantamentoRepository;
        this.custoDePercursoRepository = custoDePercursoRepository;
        this.salarioRepository = salarioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ComissaoActViewModel(
                cavaloRepository,
                motoristaRepository,
                freteRepository,
                adiantamentoRepository,
                custoDePercursoRepository,
                salarioRepository
        );
    }
}

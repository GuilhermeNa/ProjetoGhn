package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.AppGhn.model.Adiantamento;
import br.com.transporte.AppGhn.model.Motorista;
import br.com.transporte.AppGhn.repository.AdiantamentoRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;

public class FormularioAdiantamentoViewModel extends ViewModel {
    private final MotoristaRepository motoristaRepository;
    private final AdiantamentoRepository adiantamentoRepository;

    public FormularioAdiantamentoViewModel(MotoristaRepository motoristaRepository, AdiantamentoRepository adiantamentoRepository) {
        this.motoristaRepository = motoristaRepository;
        this.adiantamentoRepository = adiantamentoRepository;
    }

    //----------------------------------------------------------------------------------------------

    public Adiantamento adiantamentoArmazenado;

    public LiveData<Motorista> localizaMotorista(final Long id){
        return motoristaRepository.localizaMotorista(id);
    }

    public LiveData<Adiantamento> localizaAdiantamento(final Long id) {
        return adiantamentoRepository.localizaAdiantamento(id);
    }

    public LiveData<Long> salva(@NonNull final Adiantamento adiantamento){
        if(adiantamento.getId() == null){
            return adiantamentoRepository.adiciona(adiantamento);
        } else {
            return adiantamentoRepository.altera(adiantamento);
        }
    }

    public LiveData<String> deleta() {
        if(adiantamentoArmazenado != null){
            return adiantamentoRepository.deleta(adiantamentoArmazenado);
        } else {
            return new MutableLiveData<>("Adiantamento não encontrado");
        }
    }

}

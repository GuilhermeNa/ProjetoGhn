package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.AppGhn.model.Frete;
import br.com.transporte.AppGhn.repository.FreteRepository;

public class FormularioFreteViewModel extends ViewModel {
    private final FreteRepository repository;

    public FormularioFreteViewModel(FreteRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public Frete freteArmazenado;

    public LiveData<Frete> localizaFrete(final long freteId){
        return repository.localizaFrete(freteId);
    }

    public LiveData<Long> salva(@NonNull final Frete frete) {
        if(frete.getId() == null){
            return repository.adicionaFrete(frete);
        } else {
            return repository.editaFrete(frete);
        }
    }

    public LiveData<String> deleta() {
        if(freteArmazenado != null){
           return repository.deletaFrete(freteArmazenado);
        } else {
            return new MutableLiveData<>("Frete não encontrado");
        }
    }

}

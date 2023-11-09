package br.com.transporte.appGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.repository.ReboqueRepository;

public class FormularioReboqueViewModel extends ViewModel {
    private final ReboqueRepository repository;

    public FormularioReboqueViewModel(ReboqueRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public SemiReboque reboqueArmazenado;

    public LiveData<SemiReboque> localizaReboque(final long id) {
        return repository.localizaReboque(id);
    }

    public LiveData<String> deleta() {
        if (reboqueArmazenado != null) {
            return repository.deletaReboque(reboqueArmazenado);
        } else {
            return new MutableLiveData<>("Reboque não encontrado");
        }
    }

    public LiveData<Long> salva(@NonNull final SemiReboque reboque){
        if(reboque.getId() == null){
            return repository.adicionaReboque(reboque);
        } else {
            return repository.substituiReboque(reboque);
        }
    }





}

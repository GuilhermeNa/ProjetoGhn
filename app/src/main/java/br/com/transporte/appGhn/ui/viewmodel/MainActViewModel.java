package br.com.transporte.appGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.Resource;

public class MainActViewModel extends ViewModel {
    private final CavaloRepository cavaloRepository;
    private final MotoristaRepository motoristaRepository;

    public MainActViewModel(CavaloRepository repository, MotoristaRepository motoristaRepository) {
        this.cavaloRepository = repository;
        this.motoristaRepository = motoristaRepository;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Resource<List<Cavalo>>> buscaCavalos(){
        return cavaloRepository.buscaCavalos();
    }



}

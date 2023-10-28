package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.MotoristaRepository;
import br.com.transporte.AppGhn.repository.Resource;

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

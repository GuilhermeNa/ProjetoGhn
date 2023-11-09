package br.com.transporte.appGhn.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.transporte.appGhn.model.Cavalo;
import br.com.transporte.appGhn.model.Motorista;
import br.com.transporte.appGhn.model.SemiReboque;
import br.com.transporte.appGhn.repository.CavaloRepository;
import br.com.transporte.appGhn.repository.MotoristaRepository;
import br.com.transporte.appGhn.repository.ReboqueRepository;
import br.com.transporte.appGhn.repository.Resource;

public class FrotaViewModel extends ViewModel {
    private final CavaloRepository repositoryCavalo;
    private final MotoristaRepository repositoryMotorista;
    private final ReboqueRepository repositoryReboque;
    private List<Cavalo> dataSetCavalo = new ArrayList<>();
    private List<Motorista> dataSetMotorista = new ArrayList<>();
    private List<SemiReboque> dataSetReboque = new ArrayList<>();

    public FrotaViewModel(CavaloRepository repositoryCavalo, MotoristaRepository repositoryMotorista, ReboqueRepository repositoryReboque) {
        this.repositoryCavalo = repositoryCavalo;
        this.repositoryMotorista = repositoryMotorista;
        this.repositoryReboque = repositoryReboque;
    }

    //----------------------------------------------------------------------------------------------


    public List<Cavalo> getDataSetCavalo() {
        return dataSetCavalo;
    }

    public void setDataSetCavalo(List<Cavalo> dataSetCavalo) {
        this.dataSetCavalo = dataSetCavalo;
    }

    public List<Motorista> getDataSetMotorista() {
        return new ArrayList<>(dataSetMotorista);
    }

    public void setDataSetMotorista(List<Motorista> dataSetMotorista) {
        this.dataSetMotorista = dataSetMotorista;
    }

    public List<SemiReboque> getDataSetReboque() {
        return dataSetReboque;
    }

    public void setDataSetReboque(List<SemiReboque> dataSetReboque) {
        this.dataSetReboque = dataSetReboque;
    }

    public LiveData<Resource<List<Cavalo>>> buscaCavalos() {
        return repositoryCavalo.buscaCavalos();
    }

    public LiveData<Resource<List<Motorista>>> buscaMotoristas() {
        return repositoryMotorista.buscaMotoristas();
    }

    public LiveData<Resource<List<SemiReboque>>> buscaReboques() {
        return repositoryReboque.buscaReboques();
    }




}

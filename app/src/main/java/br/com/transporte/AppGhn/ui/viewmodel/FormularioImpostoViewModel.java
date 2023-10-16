package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.AppGhn.model.Cavalo;
import br.com.transporte.AppGhn.model.despesas.DespesasDeImposto;
import br.com.transporte.AppGhn.repository.CavaloRepository;
import br.com.transporte.AppGhn.repository.ImpostoRepository;
import br.com.transporte.AppGhn.repository.Resource;

public class FormularioImpostoViewModel extends ViewModel {
    private final ImpostoRepository impostoRepository;
    private final CavaloRepository cavaloRepository;

    public FormularioImpostoViewModel(ImpostoRepository impostoRepository, CavaloRepository cavaloRepository) {
        this.impostoRepository = impostoRepository;
        this.cavaloRepository = cavaloRepository;
    }

    //----------------------------------------------------------------------------------------------

    public DespesasDeImposto impostoArmazenado;

    public LiveData<DespesasDeImposto> localizaImposto(final long id){
        if(id > 0){
            return impostoRepository.localizaImposto(id);
        } else {
            return new MutableLiveData<>(null);
        }
    }

    public LiveData<Long> salva(@NonNull final DespesasDeImposto imposto){
        if(imposto.getId() == null){
            return impostoRepository.adiciona(imposto);
        } else {
            return impostoRepository.atualiza(imposto);
        }
    }

    public LiveData<String> deleta() {
        if(impostoArmazenado != null){
            return impostoRepository.deleta(impostoArmazenado);
        } else {
            return new MutableLiveData<>("Imposto não localizado");
        }
    }

    public LiveData<Cavalo> localizaCavalo(final long id){
        if(id > 0){
            return cavaloRepository.localizaCavaloPeloId(id);
        } else {
            return new MutableLiveData<>(null);
        }
    }

    public LiveData<Resource<List<Cavalo>>> buscaCavalos(){
        return cavaloRepository.buscaCavalos();
    }

}

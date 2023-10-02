package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.AppGhn.model.custos.CustosDeManutencao;
import br.com.transporte.AppGhn.repository.ManutencaoRepository;

public class FormularioManutencaoViewModel extends ViewModel {
    private final ManutencaoRepository repository;

    public FormularioManutencaoViewModel(ManutencaoRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public CustosDeManutencao manutencaoArmazenada;

    public LiveData<CustosDeManutencao> localizaManutencao(final long id) {
        if (id > 0) {
            return repository.localizaManutencao(id);
        } else {
            return new MutableLiveData<>(null);
        }
    }

    public LiveData<Long> salva(@NonNull final CustosDeManutencao manutencao){
        if(manutencao.getId() != null){
            return repository.atualizaManutencao(manutencao);
        } else {
            return repository.adicionaManutencao(manutencao);
        }
    }

    public LiveData<String> deleta(){
        if (manutencaoArmazenada != null){
            return repository.deletaManutencao(manutencaoArmazenada);
        } else {
            return new MutableLiveData<>("Manutencao não encontrada");
        }
    }

}

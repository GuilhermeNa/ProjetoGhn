package br.com.transporte.AppGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.transporte.AppGhn.model.custos.CustosDePercurso;
import br.com.transporte.AppGhn.repository.CustoDePercursoRepository;

public class FormularioCustoPercursoViewModel extends ViewModel {
    private final CustoDePercursoRepository repository;
    public FormularioCustoPercursoViewModel(CustoDePercursoRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public CustosDePercurso custoArmazenado;

    public LiveData<CustosDePercurso> localizaPeloId(final Long id){
        return repository.localizaCustoPercurso(id);
    }

    public LiveData<Long> salva(@NonNull final CustosDePercurso custo){
        if(custo.getId() == null){
            return repository.adicionaCustoPercurso(custo);
        } else {
            return repository.editCustoPercurso(custo);
        }
    }

    public LiveData<String> deleta(){
        if(custoArmazenado != null){
            return repository.deletaCustoPercurso(custoArmazenado);
        } else {
            return new MutableLiveData<>("Custo não encontrado");
        }
    }


}

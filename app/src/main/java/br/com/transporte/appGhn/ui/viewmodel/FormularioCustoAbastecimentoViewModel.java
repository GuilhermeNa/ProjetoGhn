package br.com.transporte.appGhn.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.transporte.appGhn.model.custos.CustosDeAbastecimento;
import br.com.transporte.appGhn.repository.CustoDeAbastecimentoRepository;
import br.com.transporte.appGhn.repository.Resource;

public class FormularioCustoAbastecimentoViewModel extends ViewModel {
    private final CustoDeAbastecimentoRepository repository;

    public FormularioCustoAbastecimentoViewModel(CustoDeAbastecimentoRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    public CustosDeAbastecimento abastecimentoArmazenado;

    public LiveData<CustosDeAbastecimento> localizaPeloId(final long id){
        return repository.localizaAbastecimento(id);
    }

    public LiveData<Long> salva(@NonNull final CustosDeAbastecimento abastecimento){
        if(abastecimento.getId() == null){
            return repository.adicionaAbastecimento(abastecimento);
        } else {
            return repository.editaAbastecimento(abastecimento);
        }
    }

    public LiveData<String> deleta(){
        if(abastecimentoArmazenado != null){
            return repository.deletaAbastecimento(abastecimentoArmazenado);
        } else {
            return new MutableLiveData<>("Abastecimento não encontrado");
        }
    }

    public LiveData<Resource<List<CustosDeAbastecimento>>> buscaAbastecimentosPorCavaloId(Long id) {
        return repository.buscaAbastecimentosPorCavaloId(id);
    }


}

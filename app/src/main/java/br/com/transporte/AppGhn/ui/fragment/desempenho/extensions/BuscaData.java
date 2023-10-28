package br.com.transporte.AppGhn.ui.fragment.desempenho.extensions;

import androidx.annotation.NonNull;

import br.com.transporte.AppGhn.repository.FragmentDesempenhoRepository;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;

public abstract class BuscaData {
    protected final FragmentDesempenhoRepository repository;

    public BuscaData( FragmentDesempenhoRepository repository) {
        this.repository = repository;
    }

    //----------------------------------------------------------------------------------------------

    protected boolean verificaSeRequisitaTodosOsCavalos(@NonNull DataRequest dataRequest){
        if(dataRequest.getCavaloId() == null) return true;
        else return false;
    }

}

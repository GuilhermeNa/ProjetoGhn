package br.com.transporte.appGhn.ui.fragment.desempenho.domain.buscaData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import br.com.transporte.appGhn.repository.FragmentDesempenhoRepository;
import br.com.transporte.appGhn.ui.fragment.desempenho.extensions.BuscaData;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.appGhn.ui.fragment.desempenho.model.ResourceData;

public class SolicitaParcelaVidaUseCase extends BuscaData {
    public SolicitaParcelaVidaUseCase(FragmentDesempenhoRepository repository) {
        super(repository);
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<ResourceData> run(@NonNull final DataRequest dataRequest) {
        final boolean buscaTodos = verificaSeRequisitaTodosOsCavalos(dataRequest);

        if (buscaTodos) {
            return repository
                    .buscaDespesasSeguroVidaPorAnoEOuCavaloIdParaFragmentDesempenho(
                            dataRequest.getAno(), null);
        } else {
            return repository
                    .buscaDespesasSeguroVidaPorAnoEOuCavaloIdParaFragmentDesempenho(
                            dataRequest.getAno(), dataRequest.getCavaloId());
        }
    }

}

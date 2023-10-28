package br.com.transporte.AppGhn.ui.fragment.desempenho.domain.buscaData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import br.com.transporte.AppGhn.repository.FragmentDesempenhoRepository;
import br.com.transporte.AppGhn.ui.fragment.desempenho.extensions.BuscaData;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.DataRequest;
import br.com.transporte.AppGhn.ui.fragment.desempenho.model.ResourceData;

public class SolicitaLucroLiquidoUseCase extends BuscaData {
    public SolicitaLucroLiquidoUseCase(FragmentDesempenhoRepository repository) {
        super(repository);
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<ResourceData> run(@NonNull final DataRequest dataRequest) {
        final boolean buscaTodos = verificaSeRequisitaTodosOsCavalos(dataRequest);

        if (buscaTodos) {
            return repository
                    .buscaLucroLiquidoPorAnoEOuCavaloId(
                            dataRequest.getAno(), null);
        } else {
            return repository
                    .buscaLucroLiquidoPorAnoEOuCavaloId(
                            dataRequest.getAno(), dataRequest.getCavaloId());
        }
    }

}

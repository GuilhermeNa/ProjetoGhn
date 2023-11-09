package br.com.transporte.appGhn.ui.fragment.despesasAdm.indireta.domain;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import java.time.LocalDate;
import java.util.List;

import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.repository.DespesaAdmRepository;

public class BuscaDespesasAdmIndiretasUseCase {
    private final DespesaAdmRepository despesaAdmRepository;
    private final LifecycleOwner lifecycleOwner;

    public BuscaDespesasAdmIndiretasUseCase(
            Context context,
            LifecycleOwner lifecycleOwner
    ) {
        this.despesaAdmRepository = new DespesaAdmRepository(context);
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface DespesaAdmDiretaObjectsUseCaseCallback {

        void quandoFinaliza(List<DespesaAdm> dataSet);
    }

    //----------------------------------------------------------------------------------------------
    public void buscaDataRelacionadaAsDespesasAdmIndiretas(
            final LocalDate dataInicial,
            final LocalDate dataFinal,
            final DespesaAdmDiretaObjectsUseCaseCallback callback
    ) {
        despesaAdmRepository.buscaPorTipoEData(
                INDIRETA,
                dataInicial,
                dataFinal
        ).observe(lifecycleOwner,
                despesaAdm -> {
                    if (despesaAdm != null) {
                        callback.quandoFinaliza(despesaAdm);
                    }
                });
    }

    public void solicitaAtualizacaoDeDados(
            final LocalDate dataInicial,
            final LocalDate dataFinal
    ) {
        despesaAdmRepository.buscaPorTipoEData(
                INDIRETA,
                dataInicial,
                dataFinal
        );
    }
}

package br.com.transporte.appGhn.ui.fragment.formularios.despesaAdm.domain;

import static br.com.transporte.appGhn.model.enums.TipoDespesa.DIRETA;
import static br.com.transporte.appGhn.model.enums.TipoDespesa.INDIRETA;

import br.com.transporte.appGhn.model.despesas.DespesaAdm;
import br.com.transporte.appGhn.model.enums.TipoDespesa;

public class FDespesaAdmPreparaAmbienteUseCase {
    private final TipoDespesa tipoDespesa;

    public FDespesaAdmPreparaAmbienteUseCase(TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public interface ConfiguraAmbienteFormularioDespesaUseCase {
        void adicionadoDespesaDireta();

        void editandoDespesaDireta();

        void adicionandoDespesaIndireta();

        void editandoDespesaIndireta();
    }

    //----------------------------------------------------------------------------------------------

    public void run(
            final DespesaAdm despesaAdm,
            final ConfiguraAmbienteFormularioDespesaUseCase callback
    ) {
        if (despesaAdm == null) {
            quandoAdicionando(callback);
        } else {
            quandoEditando(callback);
        }
    }

    private void quandoAdicionando(final ConfiguraAmbienteFormularioDespesaUseCase callback) {
        if (tipoDespesa == DIRETA) {
            callback.adicionadoDespesaDireta();
        } else if (tipoDespesa == INDIRETA) {
            callback.adicionandoDespesaIndireta();
        }
    }

    private void quandoEditando(final ConfiguraAmbienteFormularioDespesaUseCase callback) {
        if (tipoDespesa == DIRETA) {
            callback.editandoDespesaDireta();
        } else if (tipoDespesa == INDIRETA) {
            callback.editandoDespesaIndireta();
        }
    }

}
